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

import vazkii.botania.client.render.block_entity.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.block_entity.GaiaHeadBlockEntity;
import vazkii.botania.common.block.tile.ModTiles;
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
		consumer.accept(ModEntities.MAGIC_LANDMINE, MagicLandmineRenderer::new);
		consumer.accept(ModEntities.MAGIC_MISSILE, NoopRenderer::new);
		consumer.accept(ModEntities.FALLING_STAR, NoopRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR, NoopRenderer::new);
		consumer.accept(ModEntities.THROWN_ITEM, ItemEntityRenderer::new);
		consumer.accept(ModEntities.PIXIE, PixieRenderer::new);
		consumer.accept(ModEntities.DOPPLEGANGER, GaiaGuardianRenderer::new);
		consumer.accept(ModEntities.SPARK, ManaSparkRenderer::new);
		consumer.accept(ModEntities.CORPOREA_SPARK, CorporeaSparkRenderer::new);
		consumer.accept(ModEntities.POOL_MINECART, ManaPoolMinecartRenderer::new);
		consumer.accept(ModEntities.PINK_WITHER, PinkWitherRenderer::new);
		consumer.accept(ModEntities.MANA_STORM, ManaStormRenderer::new);
		consumer.accept(ModEntities.BABYLON_WEAPON, BabylonWeaponRenderer::new);

		consumer.accept(ModEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		consumer.accept(ModEntities.VINE_BALL, ThrownItemRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR_BOTTLE, ThrownItemRenderer::new);
	}

	public static void addAuxiliaryPlayerRenders(PlayerRenderer renderer,
			Consumer<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> consumer) {
		consumer.accept(new ContributorFancinessHandler(renderer));
		consumer.accept(new ManaTabletRenderHandler(renderer));
		consumer.accept(new TerrasteelHelmetLayer(renderer));
	}

	public interface BERConsumer {
		<E extends BlockEntity> void register(BlockEntityType<E> type, BlockEntityRendererProvider<? super E> factory);
	}

	public static void registerBlockEntityRenderers(BERConsumer consumer) {
		consumer.register(ModTiles.ALTAR, PetalApothecaryBlockEntityRenderer::new);
		consumer.register(ModTiles.SPREADER, ManaSpreaderBlockEntityRenderer::new);
		consumer.register(ModTiles.POOL, ManaPoolBlockEntityRenderer::new);
		consumer.register(ModTiles.RUNE_ALTAR, RunicAltarBlockEntityRenderer::new);
		consumer.register(ModTiles.PYLON, PylonBlockEntityRenderer::new);
		consumer.register(ModTiles.ENCHANTER, ManaEnchanterBlockEntityRenderer::new);
		consumer.register(ModTiles.ALF_PORTAL, AlfheimPortalBlockEntityRenderer::new);
		consumer.register(ModTiles.MINI_ISLAND, FloatingFlowerBlockEntityRenderer::new);
		consumer.register(ModTiles.TINY_POTATO, TinyPotatoBlockEntityRenderer::new);
		consumer.register(ModTiles.STARFIELD, StarfieldCreatorBlockEntityRenderer::new);
		consumer.register(ModTiles.BREWERY, BotanicalBreweryBlockEntityRenderer::new);
		consumer.register(ModTiles.TERRA_PLATE, TerrestrialAgglomerationPlateBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_COMPARATOR, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_CONTAINER, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_DISPENSER, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_FERTILIZER, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_INTERCEPTOR, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.RED_STRING_RELAY, RedStringBlockEntityRenderer::new);
		consumer.register(ModTiles.PRISM, ManaPrismBlockEntityRenderer::new);
		consumer.register(ModTiles.CORPOREA_INDEX, CorporeaIndexBlockEntityRenderer::new);
		consumer.register(ModTiles.PUMP, ManaPumpBlockEntityRenderer::new);
		consumer.register(ModTiles.CORPOREA_CRYSTAL_CUBE, CorporeaCrystalCubeBlockEntityRenderer::new);
		consumer.register(ModTiles.INCENSE_PLATE, IncensePlateBlockEntityRenderer::new);
		consumer.register(ModTiles.HOURGLASS, HoveringHourglassBlockEntityRenderer::new);
		consumer.register(ModTiles.SPARK_CHANGER, SparkTinkererBlockEntityRenderer::new);
		consumer.register(ModTiles.COCOON, CocoonBlockEntityRenderer::new);
		consumer.register(ModTiles.LIGHT_RELAY, LuminizerBlockEntityRenderer::new);
		consumer.register(ModTiles.BELLOWS, BellowsBlockEntityRenderer::new);
		@SuppressWarnings("unchecked")
		BlockEntityRendererProvider<GaiaHeadBlockEntity> gaia = ctx -> (BlockEntityRenderer<GaiaHeadBlockEntity>) (BlockEntityRenderer<?>) new GaiaHeadBlockEntityRenderer(ctx);
		consumer.register(ModTiles.GAIA_HEAD, gaia);
		consumer.register(ModTiles.TERU_TERU_BOZU, TeruTeruBozuBlockEntityRenderer::new);
		consumer.register(ModTiles.AVATAR, AvatarBlockEntityRenderer::new);
		consumer.register(ModTiles.ANIMATED_TORCH, AnimatedTorchBlockEntityRenderer::new);

		consumer.register(ModSubtiles.PURE_DAISY, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.MANASTAR, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.HYDROANGEAS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.ENDOFLAME, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.THERMALILY, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.ROSA_ARCANA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.MUNCHDEW, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.ENTROPINNYUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.KEKIMURUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.GOURMARYLLIS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.NARSLIMMUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.SPECTROLUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.DANDELIFEON, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.RAFFLOWSIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.SHULK_ME_NOT, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.BELLETHORNE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.BELLETHORNE_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.BERGAMUTE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.DREADTHORN, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.HEISEI_DREAM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.TIGERSEYE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.JADED_AMARANTHUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.ORECHID, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.FALLEN_KANADE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.EXOFLAME, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.AGRICARNATION, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.AGRICARNATION_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.HOPPERHOCK, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.HOPPERHOCK_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.TANGLEBERRIE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.TANGLEBERRIE_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.JIYUULIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.JIYUULIA_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.RANNUNCARPUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.RANNUNCARPUS_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.HYACIDUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.POLLIDISIAC, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.CLAYCONIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.CLAYCONIA_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.LOONIUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.DAFFOMILL, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.VINCULOTUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.SPECTRANTHEMUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.MEDUMONE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.MARIMORPHOSIS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.MARIMORPHOSIS_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.BUBBELL, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.BUBBELL_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.SOLEGNOLIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.SOLEGNOLIA_CHIBI, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.ORECHID_IGNEM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(ModSubtiles.LABELLIA, SpecialFlowerBlockEntityRenderer::new);
	}

	public static final Map<Block, Function<Block, TEISR>> BE_ITEM_RENDERER_FACTORIES = Map.of(
			ModBlocks.manaPylon, PylonBlockEntityRenderer.ItemRenderer::new,
			ModBlocks.naturaPylon, PylonBlockEntityRenderer.ItemRenderer::new,
			ModBlocks.gaiaPylon, PylonBlockEntityRenderer.ItemRenderer::new,
			ModBlocks.teruTeruBozu, TEISR::new,
			ModBlocks.avatar, TEISR::new,
			ModBlocks.bellows, TEISR::new,
			ModBlocks.brewery, TEISR::new,
			ModBlocks.corporeaIndex, TEISR::new,
			ModBlocks.hourglass, TEISR::new
	);

	private EntityRenderers() {}
}
