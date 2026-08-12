/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.render.entity;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
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
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.entity.BotaniaEntities;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EntityRenderers {
	public interface EntityRendererConsumer {
		<E extends Entity> void accept(EntityType<? extends E> entityType,
				EntityRendererProvider<E> entityRendererFactory);
	}

	public static void registerEntityRenderers(EntityRendererConsumer consumer) {
		consumer.accept(BotaniaEntities.MANA_BURST, NoopRenderer::new);
		consumer.accept(BotaniaEntities.LUMINIZER_BEAM, NoopRenderer::new);
		consumer.accept(BotaniaEntities.FLAME_RING, NoopRenderer::new);
		consumer.accept(BotaniaEntities.GAIA_TRAP, MagicLandmineRenderer::new);
		consumer.accept(BotaniaEntities.MAGIC_MISSILE, NoopRenderer::new);
		consumer.accept(BotaniaEntities.FALLING_STAR, NoopRenderer::new);
		consumer.accept(BotaniaEntities.PURE_ENDER_ESSENCE_CLOUD, NoopRenderer::new);
		consumer.accept(BotaniaEntities.DILUTED_ENDER_ESSENCE_CLOUD, NoopRenderer::new);
		consumer.accept(BotaniaEntities.THROWN_ITEM, ItemEntityRenderer::new);
		consumer.accept(BotaniaEntities.PIXIE, PixieRenderer::new);
		consumer.accept(BotaniaEntities.GAIA_GUARDIAN, GaiaGuardianRenderer::new);
		consumer.accept(BotaniaEntities.SPARK, ManaSparkRenderer::new);
		consumer.accept(BotaniaEntities.CORPOREA_SPARK, CorporeaSparkRenderer::new);
		consumer.accept(BotaniaEntities.MANA_POOL_MINECART, ManaPoolMinecartRenderer::new);
		consumer.accept(BotaniaEntities.PINK_WITHER, PinkWitherRenderer::new);
		consumer.accept(BotaniaEntities.MANASTORM_EPICENTER, ManaStormRenderer::new);
		consumer.accept(BotaniaEntities.TREASURY_WEAPON, BabylonWeaponRenderer::new);

		consumer.accept(BotaniaEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		consumer.accept(BotaniaEntities.VINE_BALL, ThrownItemRenderer::new);
		consumer.accept(BotaniaEntities.ENDER_ESSENCE_FLASK, ThrownItemRenderer::new);
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
		consumer.register(BotaniaBlockEntities.PETAL_APOTHECARY, PetalApothecaryBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_SPREADER, ManaSpreaderBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_POOL, ManaPoolBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RUNIC_ALTAR, RunicAltarBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_PYLON, PylonBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_ENCHANTER, ManaEnchanterBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ALFHEIM_PORTAL, AlfheimPortalBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.FLOATING_MUNDANE_FLOWER, FloatingFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TINY_POTATO, TinyPotatoBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.STARFIELD_CREATOR, StarfieldCreatorBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BOTANICAL_BREWERY, BotanicalBreweryBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TERRESTRIAL_AGGLOMERATION_PLATE, TerrestrialAgglomerationPlateBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRINGED_COMPARATOR, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRINGED_CONTAINER, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRINGED_DISPENSER, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRINGED_NUTRIFIER, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRING_INTERCEPTOR, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RED_STRINGED_SPOOFER, RedStringBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_PRISM, ManaPrismBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.CORPOREA_INDEX, CorporeaIndexBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANA_PUMP, ManaPumpBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, CorporeaCrystalCubeBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.INCENSE_PLATE, IncensePlateBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HOVERING_HOURGLASS, HoveringHourglassBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SPARK_TINKERER, SparkTinkererBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.COCOON_OF_CAPRICE, CocoonBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.LUMINIZER, LuminizerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANATIDE_BELLOWS, BellowsBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.GAIA_HEAD, GaiaHeadBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TERU_TERU_BOZU, TeruTeruBozuBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.AVATAR, AvatarBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ANIMATED_TORCH, AnimatedTorchBlockEntityRenderer::new);

		consumer.register(BotaniaBlockEntities.PURE_DAISY, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MANASTAR, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HYDROANGEAS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ENDOFLAME, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.THERMALILY, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ROSA_ARCANA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MUNCHDEW, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ENTROPINNYUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.KEKIMURUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.GOURMARYLLIS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.NARSLIMMUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SPECTROLUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.DANDELIFEON, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RAFFLOWSIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SHULK_ME_NOT, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BELLETHORNE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BELLETHORNE_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BERGAMUTE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.DREADTHORN, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HEISEI_DREAM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TIGERSEYE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.JADED_AMARANTHUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ORECHID, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.FALLEN_KANADE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.EXOFLAME, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.AGRICARNATION, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.AGRICARNATION_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HOPPERHOCK, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HOPPERHOCK_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TANGLEBERRIE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.TANGLEBERRIE_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.JIYUULIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.JIYUULIA_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RANNUNCARPUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.RANNUNCARPUS_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.HYACIDUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.POLLIDISIAC, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.CLAYCONIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.CLAYCONIA_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.LOONIUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.DAFFOMILL, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.VINCULOTUS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SPECTRANTHEMUM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MEDUMONE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MARIMORPHOSIS, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.MARIMORPHOSIS_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BUBBELL, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.BUBBELL_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SOLEGNOLIA, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.SOLEGNOLIA_PETITE, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.ORECHID_IGNEM, SpecialFlowerBlockEntityRenderer::new);
		consumer.register(BotaniaBlockEntities.LABELLIA, SpecialFlowerBlockEntityRenderer::new);
	}

	public static final Map<Block, Function<Block, BlockEntityItemRenderer>> BE_ITEM_RENDERER_FACTORIES = Map.of(
			BotaniaBlocks.MANA_PYLON, PylonBlockEntityRenderer.ItemRenderer::new,
			BotaniaBlocks.NATURA_PYLON, PylonBlockEntityRenderer.ItemRenderer::new,
			BotaniaBlocks.GAIA_PYLON, PylonBlockEntityRenderer.ItemRenderer::new,
			BotaniaBlocks.TERU_TERU_BOZU, BlockEntityItemRenderer::new,
			BotaniaBlocks.LIVINGWOOD_AVATAR, BlockEntityItemRenderer::new,
			BotaniaBlocks.MANATIDE_BELLOWS, BlockEntityItemRenderer::new,
			BotaniaBlocks.BOTANICAL_BREWERY, BlockEntityItemRenderer::new,
			BotaniaBlocks.CORPOREA_INDEX, BlockEntityItemRenderer::new,
			BotaniaBlocks.HOVERING_HOURGLASS, BlockEntityItemRenderer::new
	);

	private EntityRenderers() {}
}
