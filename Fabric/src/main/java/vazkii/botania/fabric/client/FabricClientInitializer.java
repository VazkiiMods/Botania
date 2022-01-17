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
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.ColorHandler;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.client.model.ModLayerDefinitions;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.entity.*;
import vazkii.botania.common.block.subtile.functional.SubTileHopperhock;
import vazkii.botania.common.block.subtile.functional.SubTileRannuncarpus;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.mana.TileTurntable;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.world.WorldTypeSkyblock;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.mixin.client.AccessorRenderBuffers;
import vazkii.botania.mixin.client.AccessorWorldPreset;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenCallback;

import java.util.SortedMap;
import java.util.function.Function;

import static vazkii.botania.common.block.ModSubtiles.*;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricPacketHandler.initClient();

		// Guis
		ScreenRegistry.register(ModItems.FLOWER_BAG_CONTAINER, GuiFlowerBag::new);
		ScreenRegistry.register(ModItems.BAUBLE_BOX_CONTAINER, GuiBaubleBox::new);

		// Models
		ModelLoadingRegistry.INSTANCE.registerModelProvider((rm, consumer) -> MiscellaneousIcons.INSTANCE.onModelRegister(consumer));
		ModelLoadingRegistry.INSTANCE.registerModelProvider(ModelHandler::registerModels);
		BlockRenderLayers.init(BlockRenderLayerMap.INSTANCE::putBlock);

		// BE/Entity Renderer
		ModLayerDefinitions.init((loc, supplier) -> EntityModelLayerRegistry.registerModelLayer(loc, supplier::get));
		ModelHandler.registerRenderers(BlockEntityRendererRegistry::register);
		ModelHandler.registerBuiltinItemRenderers((item, teisr) -> BuiltinItemRendererRegistry.INSTANCE.register(item, teisr::render));
		EntityRenderers.init(EntityRendererRegistry::register);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::initAuxiliaryRender);

		ModParticles.FactoryHandler.registerFactories(new ModParticles.FactoryHandler.Consumer() {
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

		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			AccessorWorldPreset.getAllTypes().add(WorldTypeSkyblock.INSTANCE);
		}

		BotaniaItemProperties.init((i, id, propGetter) -> FabricModelPredicateProviderRegistry.register(i.asItem(), id, propGetter));
		registerArmors();
		registerCapabilities();
	}

	private static void registerCapabilities() {
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileAnimatedTorch.WandHud((TileAnimatedTorch) be), ModTiles.ANIMATED_TORCH);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileBrewery.WandHud((TileBrewery) be), ModTiles.BREWERY);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileCorporeaRetainer.WandHud((TileCorporeaRetainer) be), ModTiles.CORPOREA_RETAINER);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileCraftCrate.WandHud((TileCraftCrate) be), ModTiles.CRAFT_CRATE);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileEnchanter.WandHud((TileEnchanter) be), ModTiles.ENCHANTER);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileHourglass.WandHud((TileHourglass) be), ModTiles.HOURGLASS);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TilePool.WandHud((TilePool) be), ModTiles.POOL);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TilePrism.WandHud((TilePrism) be), ModTiles.PRISM);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileSpreader.WandHud((TileSpreader) be), ModTiles.SPREADER);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileTurntable.WandHud((TileTurntable) be), ModTiles.TURNTABLE);

		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new SubTileSpectrolus.WandHud((SubTileSpectrolus) be), SPECTROLUS);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileEntityGeneratingFlower.GeneratingWandHud<>((TileEntityGeneratingFlower) be),
				HYDROANGEAS, ENDOFLAME, THERMALILY, ROSA_ARCANA, MUNCHDEW, ENTROPINNYUM, KEKIMURUS, GOURMARYLLIS, NARSLIMMUS,
				DANDELIFEON, RAFFLOWSIA, SHULK_ME_NOT);

		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new SubTileHopperhock.WandHud((SubTileHopperhock) be), HOPPERHOCK, HOPPERHOCK_CHIBI);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new SubTileRannuncarpus.WandHud((SubTileRannuncarpus) be), RANNUNCARPUS, RANNUNCARPUS_CHIBI);
		BotaniaFabricClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileEntityFunctionalFlower.FunctionalWandHud<>((TileEntityFunctionalFlower) be),
				BELLETHORNE, BELLETHORNE_CHIBI, BERGAMUTE, DREADTHORN, HEISEI_DREAM, TIGERSEYE,
				JADED_AMARANTHUS, ORECHID, FALLEN_KANADE, EXOFLAME, AGRICARNATION, AGRICARNATION_CHIBI,
				TANGLEBERRIE, TANGLEBERRIE_CHIBI, JIYUULIA, JIYUULIA_CHIBI, HYACIDUS, POLLIDISIAC,
				CLAYCONIA, CLAYCONIA_CHIBI, LOONIUM, DAFFOMILL, VINCULOTUS, SPECTRANTHEMUM, MEDUMONE,
				MARIMORPHOSIS, MARIMORPHOSIS_CHIBI, BUBBELL, BUBBELL_CHIBI, SOLEGNOLIA, SOLEGNOLIA_CHIBI,
				ORECHID_IGNEM, LABELLIA);
	}

	private static void registerArmors() {
		Item[] armors = Registry.ITEM.stream()
				.filter(i -> i instanceof ItemManasteelArmor
						&& Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
				.toArray(Item[]::new);

		ArmorRenderer renderer = (matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
			ItemManasteelArmor armor = (ItemManasteelArmor) stack.getItem();
			var model = ArmorModels.get(stack);
			var texture = armor.getArmorTexture(stack, slot);
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
