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
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.api.BotaniaFabricClientCapabilities;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.ColorHandler;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.client.core.handler.LayerTerraHelmet;
import vazkii.botania.client.core.handler.ManaTabletRenderHandler;
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
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.client.render.entity.RenderCorporeaSpark;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.client.render.entity.RenderManaSpark;
import vazkii.botania.client.render.entity.RenderManaStorm;
import vazkii.botania.client.render.entity.RenderPinkWither;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.entity.RenderPoolMinecart;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.subtile.functional.SubTileHopperhock;
import vazkii.botania.common.block.subtile.functional.SubTileRannuncarpus;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.mana.TileTurntable;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.world.WorldTypeSkyblock;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.mixin.client.AccessorRenderBuffers;
import vazkii.botania.mixin.client.AccessorWorldPreset;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenCallback;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.SortedMap;
import java.util.function.Function;

import static vazkii.botania.common.block.ModSubtiles.*;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricPacketHandler.initClient();

		ScreenRegistry.register(ModItems.FLOWER_BAG_CONTAINER, GuiFlowerBag::new);
		ScreenRegistry.register(ModItems.BAUBLE_BOX_CONTAINER, GuiBaubleBox::new);
		ClientLifecycleEvents.CLIENT_STARTED.register(this::loadComplete);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::initAuxiliaryRender);
		ModelLoadingRegistry.INSTANCE.registerModelProvider(MiscellaneousIcons.INSTANCE::onModelRegister);
		ModelLoadingRegistry.INSTANCE.registerModelProvider(ModelHandler::registerModels);
		ModelHandler.registerRenderers(BlockEntityRendererRegistry::register);
		ModelHandler.registerBuiltinItemRenderers((item, teisr) -> BuiltinItemRendererRegistry.INSTANCE.register(item, teisr::render));
		ModParticles.FactoryHandler.registerFactories(new ModParticles.FactoryHandler.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				ParticleFactoryRegistry.getInstance().register(type, constructor::apply);
			}
		});
		ItemTooltipCallback.EVENT.register(TooltipHandler::onTooltipEvent);
		ClientTickEvents.END_CLIENT_TICK.register(KonamiHandler::clientTick);
		BookDrawScreenCallback.EVENT.register(KonamiHandler::renderBook);
		HudRenderCallback.EVENT.register(HUDHandler::onDrawScreenPost);
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd);
		TooltipComponentCallback.EVENT.register(ManaBarTooltipComponent::tryConvert);
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ScreenKeyboardEvents.beforeKeyPress(screen)
				.register((screen2, key, scancode, modifiers) -> CorporeaInputHandler.buttonPressed(key, scancode)));

		if (BotaniaConfig.client().enableSeasonalFeatures()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2) {
				ClientProxy.jingleTheBells = true;
			}
			if (now.getMonth() == Month.OCTOBER) {
				ClientProxy.dootDoot = true;
			}
		}

		registerRenderTypes();
		registerEntityRenderers();
		registerEntityModels();

		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			AccessorWorldPreset.getAllTypes().add(WorldTypeSkyblock.INSTANCE);
		}

		ClientProxy.CORPOREA_REQUEST = new KeyMapping("key.botania_corporea_request", GLFW.GLFW_KEY_C, LibMisc.MOD_NAME);
		KeyBindingHelper.registerKeyBinding(ClientProxy.CORPOREA_REQUEST);
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

	private static void registerRenderTypes() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.defaultAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.forestAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.plainsAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mountainAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.fungalAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.swampAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.desertAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.taigaAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mesaAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mossyAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ghostRail, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.solidVines, RenderType.cutout());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.corporeaCrystalCube, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.manaGlass, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.managlassPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.elfGlass, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.alfglassPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrost, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.bifrostPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrostPerm, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.prism, RenderType.translucent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.starfield, RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.abstrusePlatform, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infrangiblePlatform, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.spectralPlatform, RenderType.translucent());

		Registry.BLOCK.stream().filter(b -> Registry.BLOCK.getKey(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof BlockFloatingFlower || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BlockModMushroom) {
						BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutout());
					}
				});
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.register(ModEntities.MANA_BURST, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.PLAYER_MOVER, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.FLAME_RING, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_LANDMINE, RenderMagicLandmine::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_MISSILE, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.FALLING_STAR, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.ENDER_AIR, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.THROWN_ITEM, ItemEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.PIXIE, RenderPixie::new);
		EntityRendererRegistry.register(ModEntities.DOPPLEGANGER, RenderDoppleganger::new);
		EntityRendererRegistry.register(ModEntities.SPARK, RenderManaSpark::new);
		EntityRendererRegistry.register(ModEntities.CORPOREA_SPARK, RenderCorporeaSpark::new);
		EntityRendererRegistry.register(ModEntities.POOL_MINECART, RenderPoolMinecart::new);
		EntityRendererRegistry.register(ModEntities.PINK_WITHER, RenderPinkWither::new);
		EntityRendererRegistry.register(ModEntities.MANA_STORM, RenderManaStorm::new);
		EntityRendererRegistry.register(ModEntities.BABYLON_WEAPON, RenderBabylonWeapon::new);

		EntityRendererRegistry.register(ModEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.VINE_BALL, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.ENDER_AIR_BOTTLE, ThrownItemRenderer::new);
	}

	private static void registerEntityModels() {
		ModLayerDefinitions.init((loc, def) -> EntityModelLayerRegistry.registerModelLayer(loc, () -> def));
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
		if (type == EntityType.PLAYER && renderer instanceof PlayerRenderer) {
			helper.register(new ContributorFancinessHandler((PlayerRenderer) renderer));
			helper.register(new ManaTabletRenderHandler((PlayerRenderer) renderer));
			helper.register(new LayerTerraHelmet((PlayerRenderer) renderer));
		}
	}
}
