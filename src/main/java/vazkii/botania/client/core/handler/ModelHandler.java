/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.render.entity.*;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModelHandler {
	static boolean registeredModels = false;

	public static void registerModels(ModelRegistryEvent evt) {
		registeredModels = true;

		ModelLoaderRegistry.registerLoader(FloatingFlowerModel.Loader.ID, FloatingFlowerModel.Loader.INSTANCE);
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(prefix("block/corporea_crystal_cube_glass"));
		ModelLoader.addSpecialModel(prefix("block/pump_head"));
		ModelLoader.addSpecialModel(prefix("block/elven_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/gaia_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/mana_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/redstone_spreader_inside"));
		registerSubtiles();

		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, RenderTileAltar::new);
		ClientRegistry.bindTileEntityRenderer(TileSpreader.TYPE, RenderTileSpreader::new);
		ClientRegistry.bindTileEntityRenderer(TilePool.TYPE, RenderTilePool::new);
		ClientRegistry.bindTileEntityRenderer(TileRuneAltar.TYPE, RenderTileRuneAltar::new);
		ClientRegistry.bindTileEntityRenderer(TilePylon.TYPE, RenderTilePylon::new);
		ClientRegistry.bindTileEntityRenderer(TileEnchanter.TYPE, RenderTileEnchanter::new);
		ClientRegistry.bindTileEntityRenderer(TileAlfPortal.TYPE, RenderTileAlfPortal::new);
		ClientRegistry.bindTileEntityRenderer(TileFloatingFlower.TYPE, RenderTileFloatingFlower::new);
		// TODO 1.14 this seems highly questionable.
		ModSubtiles.getTypes().stream()
				.map(Pair::getSecond)
				.map(rl -> Registry.BLOCK_ENTITY_TYPE.getValue(rl).get())
				.forEach(typ -> ClientRegistry.bindTileEntityRenderer(typ, RenderTileFloatingFlower::new));
		ClientRegistry.bindTileEntityRenderer(TileTinyPotato.TYPE, RenderTileTinyPotato::new);
		ClientRegistry.bindTileEntityRenderer(TileStarfield.TYPE, RenderTileStarfield::new);
		ClientRegistry.bindTileEntityRenderer(TileBrewery.TYPE, RenderTileBrewery::new);
		ClientRegistry.bindTileEntityRenderer(TileTerraPlate.TYPE, RenderTileTerraPlate::new);
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
		ClientRegistry.bindTileEntityRenderer(TileLightRelay.TYPE, RenderTileLightRelay::new);
		ClientRegistry.bindTileEntityRenderer(TileBellows.TYPE, RenderTileBellows::new);
		ClientRegistry.bindTileEntityRenderer(TileGaiaHead.TYPE, RenderTileGaiaHead::new);
		ClientRegistry.bindTileEntityRenderer(TileTeruTeruBozu.TYPE, RenderTileTeruTeruBozu::new);
		ClientRegistry.bindTileEntityRenderer(TileAvatar.TYPE, RenderTileAvatar::new);
		ClientRegistry.bindTileEntityRenderer(TileAnimatedTorch.TYPE, RenderTileAnimatedTorch::new);

		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MANA_BURST, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.PLAYER_MOVER, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.SIGNAL_FLARE, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLAME_RING, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MAGIC_LANDMINE, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MAGIC_MISSILE, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.FALLING_STAR, RenderNoop::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWN_ITEM, m -> new ItemRenderer(m, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.PIXIE, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.DOPPLEGANGER, RenderDoppleganger::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPARK, RenderSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.CORPOREA_SPARK, RenderCorporeaSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.POOL_MINECART, RenderPoolMinecart::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.PINK_WITHER, RenderPinkWither::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MANA_STORM, RenderManaStorm::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABYLON_WEAPON, RenderBabylonWeapon::new);

		RenderingRegistry.registerEntityRenderingHandler(ModEntities.THORN_CHAKRAM, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.VINE_BALL, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENDER_AIR_BOTTLE, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
	}

	private static void registerSubtiles() {
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, prefix("block/islands/island_grass"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, prefix("block/islands/island_podzol"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, prefix("block/islands/island_mycel"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, prefix("block/islands/island_snow"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.DRY, prefix("block/islands/island_dry"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, prefix("block/islands/island_golden"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, prefix("block/islands/island_vivid"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, prefix("block/islands/island_scorched"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, prefix("block/islands/island_infused"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, prefix("block/islands/island_mutated"));
	}

	private ModelHandler() {}
}
