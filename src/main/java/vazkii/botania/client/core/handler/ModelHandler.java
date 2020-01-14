/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.TileEntityRendererAnimation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.client.render.entity.RenderCorporeaSpark;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderManaStorm;
import vazkii.botania.client.render.entity.RenderPinkWither;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.entity.RenderPoolMinecart;
import vazkii.botania.client.render.entity.RenderSpark;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileAnimatedTorch;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.block.tile.TileStarfield;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringComparator;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModelHandler {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		ModelLoaderRegistry.registerLoader(FloatingFlowerModel.Loader.INSTANCE);
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(prefix("block/corporea_crystal_cube_glass"));
		ModelLoader.addSpecialModel(prefix("block/mana_pump_head"));
		registerSubtiles();

		RenderTileFloatingFlower renderTileFloatingFlower = new RenderTileFloatingFlower();
		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, RenderTileAltar::new);
		ClientRegistry.bindTileEntityRenderer(TileSpreader.TYPE, RenderTileSpreader::new);
		ClientRegistry.bindTileEntityRenderer(TilePool.TYPE, RenderTilePool::new);
		ClientRegistry.bindTileEntityRenderer(TileRuneAltar.TYPE, RenderTileRuneAltar::new);
		ClientRegistry.bindTileEntityRenderer(TilePylon.TYPE, RenderTilePylon::new);
		ClientRegistry.bindTileEntityRenderer(TileEnchanter.TYPE, new RenderTileEnchanter());
		ClientRegistry.bindTileEntityRenderer(TileAlfPortal.TYPE, RenderTileAlfPortal::new);
		ClientRegistry.bindTileEntityRenderer(TileFloatingFlower.TYPE, renderTileFloatingFlower);
		// TODO 1.14 this seems highly questionable.
		ClientRegistry.bindTileEntityRenderer(TileEntitySpecialFlower.class, renderTileFloatingFlower);
		ClientRegistry.bindTileEntityRenderer(TileTinyPotato.TYPE, new RenderTileTinyPotato());
		ClientRegistry.bindTileEntityRenderer(TileStarfield.TYPE, new RenderTileStarfield());
		ClientRegistry.bindTileEntityRenderer(TileBrewery.TYPE, RenderTileBrewery::new);
		ClientRegistry.bindTileEntityRenderer(TileTerraPlate.TYPE, new RenderTileTerraPlate());
		ClientRegistry.bindTileEntityRenderer(TileRedStringComparator.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TileRedStringContainer.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TileRedStringDispenser.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TileRedStringFertilizer.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TileRedStringInterceptor.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TileRedStringRelay.TYPE, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(TilePrism.TYPE, RenderTilePrism::new);
		ClientRegistry.bindTileEntityRenderer(TileCorporeaIndex.TYPE, RenderTileCorporeaIndex::new);
		ClientRegistry.bindTileEntityRenderer(TilePump.TYPE, RenderTilePump::new);
		ClientRegistry.bindTileEntityRenderer(TileCorporeaCrystalCube.TYPE, RenderTileCorporeaCrystalCube::new);
		ClientRegistry.bindTileEntityRenderer(TileIncensePlate.TYPE, RenderTileIncensePlate::new);
		ClientRegistry.bindTileEntityRenderer(TileHourglass.TYPE, RenderTileHourglass::new);
		ClientRegistry.bindTileEntityRenderer(TileSparkChanger.TYPE, RenderTileSparkChanger::new);
		ClientRegistry.bindTileEntityRenderer(TileCocoon.TYPE, RenderTileCocoon::new);
		ClientRegistry.bindTileEntityRenderer(TileLightRelay.TYPE, new RenderTileLightRelay());
		ClientRegistry.bindTileEntityRenderer(TileBellows.TYPE, RenderTileBellows::new);
		ClientRegistry.bindTileEntityRenderer(TileGaiaHead.TYPE, new RenderTileGaiaHead());
		ClientRegistry.bindTileEntityRenderer(TileTeruTeruBozu.TYPE, RenderTileTeruTeruBozu::new);
		ClientRegistry.bindTileEntityRenderer(TileAvatar.TYPE, RenderTileAvatar::new);
		ClientRegistry.bindTileEntityRenderer(TileAnimatedTorch.TYPE, RenderTileAnimatedTorch::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.TYPE, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoppleganger.TYPE, RenderDoppleganger::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpark.TYPE, RenderSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCorporeaSpark.TYPE, RenderCorporeaSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPoolMinecart.TYPE, RenderPoolMinecart::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPinkWither.TYPE, RenderPinkWither::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityManaStorm.TYPE, RenderManaStorm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabylonWeapon.TYPE, RenderBabylonWeapon::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityThornChakram.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityVineBall.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderAirBottle.TYPE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
	}

	private static void registerSubtiles() {
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, prefix("block/islands/island_grass"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, prefix("block/islands/island_podzol"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, prefix("block/islands/island_mycel"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, prefix("block/islands/island_snow"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.DRY, prefix("block/islands/island_dry"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, prefix("block/islands/island_golden"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, prefix("block/islands/island_vivid"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, prefix("block/islands/island_scorched"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, prefix("block/islands/island_infused"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, prefix("block/islands/island_mutated"));
	}

	private ModelHandler() {}
}
