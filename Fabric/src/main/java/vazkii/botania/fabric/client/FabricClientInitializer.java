package vazkii.botania.fabric.client;

import com.mojang.blaze3d.vertex.BufferBuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import vazkii.botania.api.BotaniaFabricClientCapabilities;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.TooltipHandler;
import vazkii.botania.client.gui.bag.FlowerPouchGui;
import vazkii.botania.client.gui.box.BaubleBoxGui;
import vazkii.botania.client.integration.ears.EarsIntegration;
import vazkii.botania.client.model.BotaniaLayerDefinitions;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.ColorHandler;
import vazkii.botania.client.render.entity.*;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.mixin.client.AccessorRenderBuffers;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenCallback;

import java.util.SortedMap;
import java.util.function.Function;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricPacketHandler.initClient();

		// Guis
		ScreenRegistry.register(ModItems.FLOWER_BAG_CONTAINER, FlowerPouchGui::new);
		ScreenRegistry.register(ModItems.BAUBLE_BOX_CONTAINER, BaubleBoxGui::new);

		// Blocks and Items
		ModelLoadingRegistry.INSTANCE.registerModelProvider(MiscellaneousModels.INSTANCE::onModelRegister);
		BlockRenderLayers.init(BlockRenderLayerMap.INSTANCE::putBlock);
		BotaniaItemProperties.init((i, id, propGetter) -> FabricModelPredicateProviderRegistry.register(i.asItem(), id, propGetter));

		// BE/Entity Renderer
		BotaniaLayerDefinitions.init((loc, supplier) -> EntityModelLayerRegistry.registerModelLayer(loc, supplier::get));
		EntityRenderers.registerBlockEntityRenderers(BlockEntityRendererRegistry::register);
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

		if (IXplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
	}

	private static void registerCapabilities() {
		BotaniaBlockEntities.registerWandHudCaps((factory, types) -> BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> factory.apply(be), types));
		BotaniaFlowerBlocks.registerWandHudCaps((factory, types) -> BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> factory.apply(be), types));
	}

	private static void registerArmors() {
		Item[] armors = Registry.ITEM.stream()
				.filter(i -> i instanceof ManasteelArmorItem
						&& Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
				.toArray(Item[]::new);

		ArmorRenderer renderer = (matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
			ManasteelArmorItem armor = (ManasteelArmorItem) stack.getItem();
			var model = ArmorModels.get(stack);
			var texture = armor.getArmorTexture(stack, entity, slot, "");
			if (model != null) {
				contextModel.copyPropertiesTo(model);
				ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, new ResourceLocation(texture));
			}
		};
		ArmorRenderer.register(renderer, armors);
	}

	private void loadComplete(Minecraft mc) {
		ColorHandler.submitBlocks(ColorProviderRegistry.BLOCK::register);
		ColorHandler.submitItems(ColorProviderRegistry.ITEM::register);

		// Needed to prevent mana pools on carts from X-raying through the cart
		SortedMap<RenderType, BufferBuilder> layers = ((AccessorRenderBuffers) mc.renderBuffers()).getEntityBuilders();
		layers.put(RenderHelper.MANA_POOL_WATER, new BufferBuilder(RenderHelper.MANA_POOL_WATER.bufferSize()));
	}

	private void initAuxiliaryRender(EntityType<? extends LivingEntity> type, LivingEntityRenderer<?, ?> renderer,
			LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper helper, EntityRendererProvider.Context ctx) {
		if (type == EntityType.PLAYER && renderer instanceof PlayerRenderer playerRenderer) {
			EntityRenderers.addAuxiliaryPlayerRenders(playerRenderer, helper::register);
		}
	}
}
