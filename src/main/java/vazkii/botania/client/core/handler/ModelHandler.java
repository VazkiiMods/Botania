/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;

import net.minecraft.resource.ResourceManager;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.Consumer;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModelHandler {
	static boolean registeredModels = false;

	public static void registerModels(ResourceManager rm, Consumer<ModelIdentifier> consumer) {
		if (!registeredModels) {
			registeredModels = true;
			// todo fabric ModelLoaderRegistry.registerLoader(FloatingFlowerModel.Loader.ID, FloatingFlowerModel.Loader.INSTANCE);
		}
		consumer.accept(new ModelIdentifier(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		consumer.accept(new ModelIdentifier(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		consumer.accept(new ModelIdentifier(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/corporea_crystal_cube_glass"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/pump_head"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/elven_spreader_inside"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/gaia_spreader_inside"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/mana_spreader_inside"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("block/redstone_spreader_inside"), "inventory"));

		registerIslands();
	}

	public static void registerRenderers() {
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.ALTAR, RenderTileAltar::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.SPREADER, RenderTileSpreader::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.POOL, RenderTilePool::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RUNE_ALTAR, RenderTileRuneAltar::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.PYLON, RenderTilePylon::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.ENCHANTER, RenderTileEnchanter::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.ALF_PORTAL, RenderTileAlfPortal::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.MINI_ISLAND, RenderTileFloatingFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.TINY_POTATO, RenderTileTinyPotato::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.STARFIELD, RenderTileStarfield::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.BREWERY, RenderTileBrewery::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.TERRA_PLATE, RenderTileTerraPlate::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_COMPARATOR, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_CONTAINER, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_DISPENSER, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_FERTILIZER, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_INTERCEPTOR, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.RED_STRING_RELAY, RenderTileRedString::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.PRISM, RenderTilePrism::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.CORPOREA_INDEX, RenderTileCorporeaIndex::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.PUMP, RenderTilePump::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.CORPOREA_CRYSTAL_CUBE, RenderTileCorporeaCrystalCube::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.INCENSE_PLATE, RenderTileIncensePlate::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.HOURGLASS, RenderTileHourglass::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.SPARK_CHANGER, RenderTileSparkChanger::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.COCOON, RenderTileCocoon::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.LIGHT_RELAY, RenderTileLightRelay::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.BELLOWS, RenderTileBellows::new);
		@SuppressWarnings("unchecked")
		Function<BlockEntityRenderDispatcher, BlockEntityRenderer<TileGaiaHead>> gaia = manager -> (BlockEntityRenderer<TileGaiaHead>) (BlockEntityRenderer<?>) new RenderTileGaiaHead(manager);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.GAIA_HEAD, gaia);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.TERU_TERU_BOZU, RenderTileTeruTeruBozu::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.AVATAR, RenderTileAvatar::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModTiles.ANIMATED_TORCH, RenderTileAnimatedTorch::new);

		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.PURE_DAISY, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.MANASTAR, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.HYDROANGEAS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.ENDOFLAME, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.THERMALILY, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.ROSA_ARCANA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.MUNCHDEW, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.ENTROPINNYUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.KEKIMURUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.GOURMARYLLIS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.NARSLIMMUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.SPECTROLUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.DANDELIFEON, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.RAFFLOWSIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.SHULK_ME_NOT, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.BELLETHORNE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.BELLETHORNE_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.BERGAMUTE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.DREADTHORN, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.HEISEI_DREAM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.TIGERSEYE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.JADED_AMARANTHUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.ORECHID, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.FALLEN_KANADE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.EXOFLAME, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.AGRICARNATION, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.AGRICARNATION_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.HOPPERHOCK, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.HOPPERHOCK_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.TANGLEBERRIE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.JIYUULIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.RANNUNCARPUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.RANNUNCARPUS_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.HYACIDUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.POLLIDISIAC, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.CLAYCONIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.CLAYCONIA_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.LOONIUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.DAFFOMILL, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.VINCULOTUS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.SPECTRANTHEMUM, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.MEDUMONE, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.MARIMORPHOSIS, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.MARIMORPHOSIS_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.BUBBELL, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.BUBBELL_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.SOLEGNOLIA, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.SOLEGNOLIA_CHIBI, RenderTileSpecialFlower::new);
		BlockEntityRendererRegistry.INSTANCE.register(ModSubtiles.ORECHID_IGNEM, RenderTileSpecialFlower::new);
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

	private ModelHandler() {}
}
