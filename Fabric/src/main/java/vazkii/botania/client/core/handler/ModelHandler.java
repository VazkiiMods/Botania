/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.IFloatingFlower;
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

	public interface BERConsumer {
		<E extends BlockEntity> void register(BlockEntityType<E> type, BlockEntityRendererProvider<? super E> factory);
	}

	public static void registerRenderers(BERConsumer consumer) {
		consumer.register(ModTiles.ALTAR, RenderTileAltar::new);
		consumer.register(ModTiles.SPREADER, RenderTileSpreader::new);
		consumer.register(ModTiles.POOL, RenderTilePool::new);
		consumer.register(ModTiles.RUNE_ALTAR, RenderTileRuneAltar::new);
		consumer.register(ModTiles.PYLON, RenderTilePylon::new);
		consumer.register(ModTiles.ENCHANTER, RenderTileEnchanter::new);
		consumer.register(ModTiles.ALF_PORTAL, RenderTileAlfPortal::new);
		consumer.register(ModTiles.MINI_ISLAND, RenderTileFloatingFlower::new);
		consumer.register(ModTiles.TINY_POTATO, RenderTileTinyPotato::new);
		consumer.register(ModTiles.STARFIELD, RenderTileStarfield::new);
		consumer.register(ModTiles.BREWERY, RenderTileBrewery::new);
		consumer.register(ModTiles.TERRA_PLATE, RenderTileTerraPlate::new);
		consumer.register(ModTiles.RED_STRING_COMPARATOR, RenderTileRedString::new);
		consumer.register(ModTiles.RED_STRING_CONTAINER, RenderTileRedString::new);
		consumer.register(ModTiles.RED_STRING_DISPENSER, RenderTileRedString::new);
		consumer.register(ModTiles.RED_STRING_FERTILIZER, RenderTileRedString::new);
		consumer.register(ModTiles.RED_STRING_INTERCEPTOR, RenderTileRedString::new);
		consumer.register(ModTiles.RED_STRING_RELAY, RenderTileRedString::new);
		consumer.register(ModTiles.PRISM, RenderTilePrism::new);
		consumer.register(ModTiles.CORPOREA_INDEX, RenderTileCorporeaIndex::new);
		consumer.register(ModTiles.PUMP, RenderTilePump::new);
		consumer.register(ModTiles.CORPOREA_CRYSTAL_CUBE, RenderTileCorporeaCrystalCube::new);
		consumer.register(ModTiles.INCENSE_PLATE, RenderTileIncensePlate::new);
		consumer.register(ModTiles.HOURGLASS, RenderTileHourglass::new);
		consumer.register(ModTiles.SPARK_CHANGER, RenderTileSparkChanger::new);
		consumer.register(ModTiles.COCOON, RenderTileCocoon::new);
		consumer.register(ModTiles.LIGHT_RELAY, RenderTileLightRelay::new);
		consumer.register(ModTiles.BELLOWS, RenderTileBellows::new);
		@SuppressWarnings("unchecked")
		BlockEntityRendererProvider<TileGaiaHead> gaia = ctx -> (BlockEntityRenderer<TileGaiaHead>) (BlockEntityRenderer<?>) new RenderTileGaiaHead(ctx);
		consumer.register(ModTiles.GAIA_HEAD, gaia);
		consumer.register(ModTiles.TERU_TERU_BOZU, RenderTileTeruTeruBozu::new);
		consumer.register(ModTiles.AVATAR, RenderTileAvatar::new);
		consumer.register(ModTiles.ANIMATED_TORCH, RenderTileAnimatedTorch::new);

		consumer.register(ModSubtiles.PURE_DAISY, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.MANASTAR, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.HYDROANGEAS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.ENDOFLAME, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.THERMALILY, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.ROSA_ARCANA, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.MUNCHDEW, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.ENTROPINNYUM, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.KEKIMURUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.GOURMARYLLIS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.NARSLIMMUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.SPECTROLUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.DANDELIFEON, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.RAFFLOWSIA, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.SHULK_ME_NOT, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.BELLETHORNE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.BELLETHORNE_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.BERGAMUTE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.DREADTHORN, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.HEISEI_DREAM, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.TIGERSEYE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.JADED_AMARANTHUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.ORECHID, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.FALLEN_KANADE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.EXOFLAME, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.AGRICARNATION, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.AGRICARNATION_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.HOPPERHOCK, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.HOPPERHOCK_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.TANGLEBERRIE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.TANGLEBERRIE_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.JIYUULIA, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.JIYUULIA_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.RANNUNCARPUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.RANNUNCARPUS_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.HYACIDUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.POLLIDISIAC, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.CLAYCONIA, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.CLAYCONIA_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.LOONIUM, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.DAFFOMILL, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.VINCULOTUS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.SPECTRANTHEMUM, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.MEDUMONE, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.MARIMORPHOSIS, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.MARIMORPHOSIS_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.BUBBELL, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.BUBBELL_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.SOLEGNOLIA, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.SOLEGNOLIA_CHIBI, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.ORECHID_IGNEM, RenderTileSpecialFlower::new);
		consumer.register(ModSubtiles.LABELLIA, RenderTileSpecialFlower::new);
	}

	public static void registerBuiltinItemRenderers() {
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
