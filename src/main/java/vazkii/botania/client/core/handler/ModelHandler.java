/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModelHandler {
	static boolean registeredModels = false;

	public static void registerModels(ResourceManager rm, Consumer<ResourceLocation> consumer) {
		if (!registeredModels) {
			registeredModels = true;
		}
		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		consumer.accept(prefix("block/corporea_crystal_cube_glass"));
		consumer.accept(prefix("block/pump_head"));
		consumer.accept(prefix("block/elven_spreader_inside"));
		consumer.accept(prefix("block/gaia_spreader_inside"));
		consumer.accept(prefix("block/mana_spreader_inside"));
		consumer.accept(prefix("block/redstone_spreader_inside"));

		registerIslands();
		registerTaters(rm, consumer);
	}

	public static void registerRenderers() {
		BlockEntityRendererRegistry.register(ModTiles.ALTAR, RenderTileAltar::new);
		BlockEntityRendererRegistry.register(ModTiles.SPREADER, RenderTileSpreader::new);
		BlockEntityRendererRegistry.register(ModTiles.POOL, RenderTilePool::new);
		BlockEntityRendererRegistry.register(ModTiles.RUNE_ALTAR, RenderTileRuneAltar::new);
		BlockEntityRendererRegistry.register(ModTiles.PYLON, RenderTilePylon::new);
		BlockEntityRendererRegistry.register(ModTiles.ENCHANTER, RenderTileEnchanter::new);
		BlockEntityRendererRegistry.register(ModTiles.ALF_PORTAL, RenderTileAlfPortal::new);
		BlockEntityRendererRegistry.register(ModTiles.MINI_ISLAND, RenderTileFloatingFlower::new);
		BlockEntityRendererRegistry.register(ModTiles.TINY_POTATO, RenderTileTinyPotato::new);
		BlockEntityRendererRegistry.register(ModTiles.STARFIELD, RenderTileStarfield::new);
		BlockEntityRendererRegistry.register(ModTiles.BREWERY, RenderTileBrewery::new);
		BlockEntityRendererRegistry.register(ModTiles.TERRA_PLATE, RenderTileTerraPlate::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_COMPARATOR, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_CONTAINER, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_DISPENSER, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_FERTILIZER, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_INTERCEPTOR, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.RED_STRING_RELAY, RenderTileRedString::new);
		BlockEntityRendererRegistry.register(ModTiles.PRISM, RenderTilePrism::new);
		BlockEntityRendererRegistry.register(ModTiles.CORPOREA_INDEX, RenderTileCorporeaIndex::new);
		BlockEntityRendererRegistry.register(ModTiles.PUMP, RenderTilePump::new);
		BlockEntityRendererRegistry.register(ModTiles.CORPOREA_CRYSTAL_CUBE, RenderTileCorporeaCrystalCube::new);
		BlockEntityRendererRegistry.register(ModTiles.INCENSE_PLATE, RenderTileIncensePlate::new);
		BlockEntityRendererRegistry.register(ModTiles.HOURGLASS, RenderTileHourglass::new);
		BlockEntityRendererRegistry.register(ModTiles.SPARK_CHANGER, RenderTileSparkChanger::new);
		BlockEntityRendererRegistry.register(ModTiles.COCOON, RenderTileCocoon::new);
		BlockEntityRendererRegistry.register(ModTiles.LIGHT_RELAY, RenderTileLightRelay::new);
		BlockEntityRendererRegistry.register(ModTiles.BELLOWS, RenderTileBellows::new);
		@SuppressWarnings("unchecked")
		BlockEntityRendererProvider<TileGaiaHead> gaia = ctx -> (BlockEntityRenderer<TileGaiaHead>) (BlockEntityRenderer<?>) new RenderTileGaiaHead(ctx);
		BlockEntityRendererRegistry.register(ModTiles.GAIA_HEAD, gaia);
		BlockEntityRendererRegistry.register(ModTiles.TERU_TERU_BOZU, RenderTileTeruTeruBozu::new);
		BlockEntityRendererRegistry.register(ModTiles.AVATAR, RenderTileAvatar::new);
		BlockEntityRendererRegistry.register(ModTiles.ANIMATED_TORCH, RenderTileAnimatedTorch::new);

		BlockEntityRendererRegistry.register(ModSubtiles.PURE_DAISY, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.MANASTAR, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.HYDROANGEAS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.ENDOFLAME, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.THERMALILY, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.ROSA_ARCANA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.MUNCHDEW, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.ENTROPINNYUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.KEKIMURUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.GOURMARYLLIS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.NARSLIMMUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.SPECTROLUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.DANDELIFEON, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.RAFFLOWSIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.SHULK_ME_NOT, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.BELLETHORNE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.BELLETHORNE_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.BERGAMUTE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.DREADTHORN, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.HEISEI_DREAM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.TIGERSEYE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.JADED_AMARANTHUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.ORECHID, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.FALLEN_KANADE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.EXOFLAME, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.AGRICARNATION, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.AGRICARNATION_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.HOPPERHOCK, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.HOPPERHOCK_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.TANGLEBERRIE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.JIYUULIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.RANNUNCARPUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.RANNUNCARPUS_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.HYACIDUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.POLLIDISIAC, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.CLAYCONIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.CLAYCONIA_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.LOONIUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.DAFFOMILL, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.VINCULOTUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.SPECTRANTHEMUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.MEDUMONE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.MARIMORPHOSIS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.MARIMORPHOSIS_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.BUBBELL, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.BUBBELL_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.SOLEGNOLIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.SOLEGNOLIA_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.ORECHID_IGNEM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.register(ModSubtiles.LABELLIA, RenderTileSpecialFlower::new);
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.manaPylon.asItem(), new RenderTilePylon.TEISR());
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.naturaPylon.asItem(), new RenderTilePylon.TEISR());
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.gaiaPylon.asItem(), new RenderTilePylon.TEISR());
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.teruTeruBozu.asItem(), new TEISR(ModBlocks.teruTeruBozu));
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.avatar.asItem(), new TEISR(ModBlocks.avatar));
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.bellows.asItem(), new TEISR(ModBlocks.bellows));
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.brewery.asItem(), new TEISR(ModBlocks.brewery));
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.corporeaIndex.asItem(), new TEISR(ModBlocks.corporeaIndex));
		BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.hourglass.asItem(), new TEISR(ModBlocks.hourglass));
	}

	private static void registerIslands() {
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

	private static void registerTaters(ResourceManager rm, Consumer<ResourceLocation> consumer) {
		for (ResourceLocation model : rm.listResources(LibResources.PREFIX_MODELS + LibResources.PREFIX_TINY_POTATO, s -> s.endsWith(LibResources.ENDING_JSON))) {
			if (LibMisc.MOD_ID.equals(model.getNamespace())) {
				String path = model.getPath();
				path = path.substring(LibResources.PREFIX_MODELS.length(), path.length() - LibResources.ENDING_JSON.length());
				consumer.accept(new ResourceLocation(LibMisc.MOD_ID, path));
			}
		}
	}

	private ModelHandler() {}
}
