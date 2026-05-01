package vazkii.botania.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.BotaniaFabricClientCapabilities;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.TooltipHandler;
import vazkii.botania.client.gui.bag.ColoredContentsPouchScreen;
import vazkii.botania.client.gui.box.BaubleBoxGui;
import vazkii.botania.client.gui.monocle.MonocleHUDs;
import vazkii.botania.client.integration.ears.EarsIntegration;
import vazkii.botania.client.model.BotaniaLayerDefinitions;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.ColorHandler;
import vazkii.botania.client.render.entity.*;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.xplat.ClientXplatAbstractions;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenCallback;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// ensure API implementations are loaded
		BotaniaAPI.LOGGER.debug("Client API instances: {}",
				List.of(BotaniaAPIClient.instance(), ClientXplatAbstractions.instance()));

		CoreShaderRegistrationCallback.EVENT.register(ctx -> CoreShaders.init((id, vertexFormat, onLoaded) -> {
			try {
				ctx.register(id, vertexFormat, onLoaded);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		));
		FabricPacketHandler.initClient();

		// Guis
		MenuScreens.register(BotaniaItems.COLORED_CONTENTS_POUCH_CONTAINER, ColoredContentsPouchScreen::new);
		MenuScreens.register(BotaniaItems.BAUBLE_BOX_CONTAINER, BaubleBoxGui::new);

		// Blocks and Items
		ModelLoadingPlugin.register(pluginContext -> {
			MiscellaneousModels.INSTANCE.onModelRegister(Minecraft.getInstance().getResourceManager(), pluginContext::addModels);
			pluginContext.modifyModelAfterBake().register((bakedModel, context) -> MiscellaneousModels.INSTANCE.modifyModelAfterbake(bakedModel, context.resourceId()));
		});
		BlockRenderLayers.init(BlockRenderLayerMap.INSTANCE::putBlock);
		BotaniaItemProperties.init((i, id, propGetter) -> ItemProperties.register(i.asItem(), id, propGetter));

		// BE/Entity Renderer
		BotaniaLayerDefinitions.init((loc, supplier) -> EntityModelLayerRegistry.registerModelLayer(loc, supplier::get));
		EntityRenderers.registerBlockEntityRenderers(BlockEntityRenderers::register);
		for (var pair : EntityRenderers.BE_ITEM_RENDERER_FACTORIES.entrySet()) {
			var block = pair.getKey();
			var renderer = pair.getValue().apply(block);
			BuiltinItemRendererRegistry.INSTANCE.register(block, renderer::render);
		}
		EntityRenderers.registerEntityRenderers(EntityRendererRegistry::register);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::initAuxiliaryRender);

		BotaniaParticles.FactoryHandler.registerFactories(new BotaniaParticles.FactoryHandler.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				ParticleFactoryRegistry.getInstance().register(type, constructor::apply);
			}
		});

		// Events
		BookDrawScreenCallback.EVENT.register(KonamiHandler::renderBook);
		ClientLifecycleEvents.CLIENT_STARTED.register(this::loadComplete);
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd);
		ClientTickEvents.END_CLIENT_TICK.register(KonamiHandler::clientTick);
		HudRenderCallback.EVENT.register(HUDHandler::onDrawScreenPost);
		ItemTooltipCallback.EVENT.register(TooltipHandler::onTooltipEvent);
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ScreenKeyboardEvents.beforeKeyPress(screen)
				.register((screen2, key, scancode, modifiers) -> CorporeaInputHandler.buttonPressed(key, scancode)));
		TooltipComponentCallback.EVENT.register(ManaBarTooltipComponent::tryConvert);

		// Etc
		ClientProxy.initSeasonal();
		ClientProxy.initKeybindings(KeyBindingHelper::registerKeyBinding);

		registerArmors();
		registerCapabilities();

		if (XplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
	}

	private static void registerCapabilities() {
		BotaniaEntities.registerWandHudCaps(getECapConsumer(BotaniaFabricClientCapabilities.ENTITY_WAND_HUD));
		MonocleHUDs.registerMonocleHudCaps(getECapConsumer(BotaniaFabricClientCapabilities.ENTITY_MONOCLE_HUD), false);
		MonocleHUDs.registerMonocleHudFallbackCaps(getEntityFallbackCapsConsumer(BotaniaFabricClientCapabilities.ENTITY_MONOCLE_HUD));

		BotaniaBlockEntities.registerWandHudCaps(
				(factory, types) -> BotaniaFabricClientCapabilities.BLOCK_WAND_HUD.registerForBlockEntities(
						(be, c) -> factory.apply(be), types));

		MonocleHUDs.registerMonocleHudCaps(getBCapConsumer(BotaniaFabricClientCapabilities.BLOCK_MONOCLE_HUD), null);
		MonocleHUDs.registerMonocleHudFallbackCaps(getBFallbackCapConsumer(BotaniaFabricClientCapabilities.BLOCK_MONOCLE_HUD));
	}

	private static <T, C> BotaniaBlocks.BCapConsumer<T> getBCapConsumer(BlockApiLookup<T, C> apiLookup) {
		return (factory, blocks) -> apiLookup.registerForBlocks(
				(world, pos, state, blockEntity, context) -> factory.apply(state), blocks);
	}

	private static <T, C> BotaniaBlocks.BCapFallbackConsumer<T> getBFallbackCapConsumer(BlockApiLookup<T, C> apiLookup) {
		return factory -> apiLookup.registerFallback(
				(world, pos, state, blockEntity, context) -> factory.apply(state));
	}

	private static <T, C> BotaniaEntities.ECapConsumer<T> getECapConsumer(EntityApiLookup<T, C> apiLookup) {
		return (factory, types) -> apiLookup.registerForTypes(
				(e, c) -> factory.apply(e), types);
	}

	private static <T, C> BotaniaEntities.ECapFallbackConsumer<T> getEntityFallbackCapsConsumer(EntityApiLookup<T, C> apiLookup) {
		return factory -> apiLookup.registerFallback((e, c) -> factory.apply(e));
	}

	private static void registerArmors() {
		Map<Item, ArmorMaterial.Layer> armors = new LinkedHashMap<>();
		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			Item item = entry.getValue();
			ResourceLocation id = entry.getKey().location();
			if (item instanceof ManasteelArmorItem armor
					&& id.getNamespace().equals(BotaniaAPI.MODID)) {
				armors.put(armor, armor.getMaterial().value().layers().getFirst());
			}
		}

		ArmorRenderer renderer = (matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
			ManasteelArmorItem armor = (ManasteelArmorItem) stack.getItem();
			var model = ArmorModels.get(stack);
			var texture = armor.getArmorTexture(stack, entity, slot, armors.get(stack.getItem()), false);
			if (model != null) {
				contextModel.copyPropertiesTo(model);
				ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture);
			}
		};
		ArmorRenderer.register(renderer, armors.keySet().toArray(Item[]::new));
	}

	private void loadComplete(Minecraft mc) {
		ColorHandler.submitBlocks(ColorProviderRegistry.BLOCK::register);
		ColorHandler.submitItems(ColorProviderRegistry.ITEM::register);
	}

	private void initAuxiliaryRender(EntityType<? extends LivingEntity> type, LivingEntityRenderer<?, ?> renderer,
			LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper helper, EntityRendererProvider.Context ctx) {
		if (type == EntityType.PLAYER && renderer instanceof PlayerRenderer playerRenderer) {
			EntityRenderers.addAuxiliaryPlayerRenders(playerRenderer, helper::register);
		}
	}
}
