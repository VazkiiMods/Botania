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
		RenderTilePylon renderTilePylon = new RenderTilePylon();
		ClientRegistry.bindTileEntityRenderer(TileAltar.TYPE, new RenderTileAltar());
		ClientRegistry.bindTileEntityRenderer(TileSpreader.TYPE, new RenderTileSpreader());
		ClientRegistry.bindTileEntityRenderer(TilePool.TYPE, new RenderTilePool());
		ClientRegistry.bindTileEntityRenderer(TileRuneAltar.TYPE, new RenderTileRuneAltar());
		ClientRegistry.bindTileEntityRenderer(TilePylon.TYPE, renderTilePylon);
		ClientRegistry.bindTileEntityRenderer(TileEnchanter.TYPE, new RenderTileEnchanter());
		ClientRegistry.bindTileEntityRenderer(TileAlfPortal.TYPE, new RenderTileAlfPortal());
		ClientRegistry.bindTileEntityRenderer(TileFloatingFlower.TYPE, renderTileFloatingFlower);
		// TODO 1.14 this seems highly questionable.
		ClientRegistry.bindTileEntityRenderer(TileEntitySpecialFlower.class, renderTileFloatingFlower);
		ClientRegistry.bindTileEntityRenderer(TileTinyPotato.TYPE, new RenderTileTinyPotato());
		ClientRegistry.bindTileEntityRenderer(TileStarfield.TYPE, new RenderTileStarfield());
		ClientRegistry.bindTileEntityRenderer(TileBrewery.TYPE, new RenderTileBrewery());
		ClientRegistry.bindTileEntityRenderer(TileTerraPlate.TYPE, new RenderTileTerraPlate());
		ClientRegistry.bindTileEntityRenderer(TileRedString.TYPE, new RenderTileRedString());
		ClientRegistry.bindTileEntityRenderer(TilePrism.TYPE, new RenderTilePrism());
		ClientRegistry.bindTileEntityRenderer(TileCorporeaIndex.TYPE, new RenderTileCorporeaIndex());
		ClientRegistry.bindTileEntityRenderer(TilePump.TYPE, new RenderTilePump());
		ClientRegistry.bindTileEntityRenderer(TileCorporeaCrystalCube.TYPE, new RenderTileCorporeaCrystalCube());
		ClientRegistry.bindTileEntityRenderer(TileIncensePlate.TYPE, new RenderTileIncensePlate());
		ClientRegistry.bindTileEntityRenderer(TileHourglass.TYPE, new RenderTileHourglass());
		ClientRegistry.bindTileEntityRenderer(TileSparkChanger.TYPE, new RenderTileSparkChanger());
		ClientRegistry.bindTileEntityRenderer(TileCocoon.TYPE, new RenderTileCocoon());
		ClientRegistry.bindTileEntityRenderer(TileLightRelay.TYPE, new RenderTileLightRelay());
		ClientRegistry.bindTileEntityRenderer(TileBellows.TYPE, new RenderTileBellows());
		ClientRegistry.bindTileEntityRenderer(TileGaiaHead.TYPE, new RenderTileGaiaHead());
		ClientRegistry.bindTileEntityRenderer(TileTeruTeruBozu.TYPE, new RenderTileTeruTeruBozu());
		ClientRegistry.bindTileEntityRenderer(TileAvatar.TYPE, new RenderTileAvatar());
		ClientRegistry.bindTileEntityRenderer(TileAnimatedTorch.TYPE, new RenderTileAnimatedTorch());

		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoppleganger.class, RenderDoppleganger::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpark.class, RenderSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCorporeaSpark.class, RenderCorporeaSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPoolMinecart.class, RenderPoolMinecart::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPinkWither.class, RenderPinkWither::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityManaStorm.class, RenderManaStorm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabylonWeapon.class, RenderBabylonWeapon::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityThornChakram.class, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityVineBall.class, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderAirBottle.class, renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
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
