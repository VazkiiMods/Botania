package vazkii.botania.client.render.entity;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.entity.ModEntities;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EntityRenderers {
	public interface EntityRendererConsumer {
		<E extends Entity> void accept(EntityType<? extends E> entityType,
				EntityRendererProvider<E> entityRendererFactory);
	}

	public static void registerEntityRenderers(EntityRendererConsumer consumer) {
		consumer.accept(ModEntities.MANA_BURST, NoopRenderer::new);
		consumer.accept(ModEntities.PLAYER_MOVER, NoopRenderer::new);
		consumer.accept(ModEntities.FLAME_RING, NoopRenderer::new);
		consumer.accept(ModEntities.MAGIC_LANDMINE, RenderMagicLandmine::new);
		consumer.accept(ModEntities.MAGIC_MISSILE, NoopRenderer::new);
		consumer.accept(ModEntities.FALLING_STAR, NoopRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR, NoopRenderer::new);
		consumer.accept(ModEntities.THROWN_ITEM, ItemEntityRenderer::new);
		consumer.accept(ModEntities.PIXIE, RenderPixie::new);
		consumer.accept(ModEntities.DOPPLEGANGER, RenderDoppleganger::new);
		consumer.accept(ModEntities.SPARK, RenderManaSpark::new);
		consumer.accept(ModEntities.CORPOREA_SPARK, RenderCorporeaSpark::new);
		consumer.accept(ModEntities.POOL_MINECART, RenderPoolMinecart::new);
		consumer.accept(ModEntities.PINK_WITHER, RenderPinkWither::new);
		consumer.accept(ModEntities.MANA_STORM, RenderManaStorm::new);
		consumer.accept(ModEntities.BABYLON_WEAPON, RenderBabylonWeapon::new);

		consumer.accept(ModEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		consumer.accept(ModEntities.VINE_BALL, ThrownItemRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR_BOTTLE, ThrownItemRenderer::new);
	}

	public static void addAuxiliaryPlayerRenders(PlayerRenderer renderer,
			Consumer<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> consumer) {
		consumer.accept(new ContributorFancinessHandler(renderer));
		consumer.accept(new ManaTabletRenderHandler(renderer));
		consumer.accept(new LayerTerraHelmet(renderer));
	}

	public interface BERConsumer {
		<E extends BlockEntity> void register(BlockEntityType<E> type, BlockEntityRendererProvider<? super E> factory);
	}

	public static void registerBlockEntityRenderers(BERConsumer consumer) {
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

	public static final Map<Block, Function<Block, TEISR>> BE_ITEM_RENDERER_FACTORIES = Map.of(
			ModBlocks.manaPylon, RenderTilePylon.ItemRenderer::new,
			ModBlocks.naturaPylon, RenderTilePylon.ItemRenderer::new,
			ModBlocks.gaiaPylon, RenderTilePylon.ItemRenderer::new,
			ModBlocks.teruTeruBozu, TEISR::new,
			ModBlocks.avatar, TEISR::new,
			ModBlocks.bellows, TEISR::new,
			ModBlocks.brewery, TEISR::new,
			ModBlocks.corporeaIndex, TEISR::new,
			ModBlocks.hourglass, TEISR::new
	);

	private EntityRenderers() {}
}
