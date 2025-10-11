package vazkii.botania.common.block.block_entity;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.spark.SparkAttachable;

import java.util.Set;

public final class BlockEntityConstants {
	public static final Set<BlockEntityType<? extends Wandable>> SELF_WANDABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.ALF_PORTAL, BotaniaBlockEntities.ANIMATED_TORCH,
			BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, BotaniaBlockEntities.CORPOREA_RETAINER,
			BotaniaBlockEntities.CRAFT_CRATE, BotaniaBlockEntities.ENCHANTER, BotaniaBlockEntities.FOREST_EYE,
			BotaniaBlockEntities.HOURGLASS, BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.POOL,
			BotaniaBlockEntities.RUNE_ALTAR, BotaniaBlockEntities.SPREADER, BotaniaBlockEntities.TURNTABLE,
			BotaniaBlockEntities.DAFFOMILL, BotaniaBlockEntities.HOPPERHOCK, BotaniaBlockEntities.HOPPERHOCK_CHIBI,
			BotaniaBlockEntities.POLLIDISIAC, BotaniaBlockEntities.RANNUNCARPUS, BotaniaBlockEntities.RANNUNCARPUS_CHIBI
	);

	public static final Set<BlockEntityType<? extends WandBindable>> SELF_WAND_BINDABLE_BES = ImmutableSet.of(
			// generating flowers
			BotaniaBlockEntities.DANDELIFEON, BotaniaBlockEntities.ENDOFLAME, BotaniaBlockEntities.ENTROPINNYUM,
			BotaniaBlockEntities.GOURMARYLLIS, BotaniaBlockEntities.KEKIMURUS, BotaniaBlockEntities.MUNCHDEW,
			BotaniaBlockEntities.NARSLIMMUS, BotaniaBlockEntities.RAFFLOWSIA, BotaniaBlockEntities.ROSA_ARCANA,
			BotaniaBlockEntities.SHULK_ME_NOT, BotaniaBlockEntities.SPECTROLUS,

			// functional flowers
			BotaniaBlockEntities.AGRICARNATION, BotaniaBlockEntities.AGRICARNATION_CHIBI,
			BotaniaBlockEntities.BELLETHORNE, BotaniaBlockEntities.BELLETHORNE_CHIBI, BotaniaBlockEntities.BUBBELL,
			BotaniaBlockEntities.BUBBELL_CHIBI, BotaniaBlockEntities.CLAYCONIA, BotaniaBlockEntities.CLAYCONIA_CHIBI,
			BotaniaBlockEntities.DAFFOMILL, BotaniaBlockEntities.DREADTHORN, BotaniaBlockEntities.EXOFLAME,
			BotaniaBlockEntities.FALLEN_KANADE, BotaniaBlockEntities.HEISEI_DREAM, BotaniaBlockEntities.HOPPERHOCK,
			BotaniaBlockEntities.HOPPERHOCK_CHIBI, BotaniaBlockEntities.HYACIDUS, BotaniaBlockEntities.JADED_AMARANTHUS,
			BotaniaBlockEntities.JIYUULIA, BotaniaBlockEntities.JIYUULIA_CHIBI, BotaniaBlockEntities.LABELLIA,
			BotaniaBlockEntities.LOONIUM, BotaniaBlockEntities.MARIMORPHOSIS, BotaniaBlockEntities.MARIMORPHOSIS_CHIBI,
			BotaniaBlockEntities.MEDUMONE, BotaniaBlockEntities.ORECHID, BotaniaBlockEntities.ORECHID_IGNEM,
			BotaniaBlockEntities.POLLIDISIAC, BotaniaBlockEntities.RANNUNCARPUS,
			BotaniaBlockEntities.RANNUNCARPUS_CHIBI, BotaniaBlockEntities.SOLEGNOLIA,
			BotaniaBlockEntities.SOLEGNOLIA_CHIBI, BotaniaBlockEntities.SPECTRANTHEMUM,
			BotaniaBlockEntities.TANGLEBERRIE, BotaniaBlockEntities.TANGLEBERRIE_CHIBI, BotaniaBlockEntities.TIGERSEYE,
			BotaniaBlockEntities.VINCULOTUS,

			// other
			BotaniaBlockEntities.LIGHT_RELAY, BotaniaBlockEntities.SPREADER
	);

	public static final Set<BlockEntityType<? extends PhantomInkableBlock>> SELF_PHANTOM_INKABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.LIGHT_RELAY, BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE
	);

	public static final Set<BlockEntityType<? extends ManaTrigger>> SELF_MANA_TRIGGER_BES = ImmutableSet.of(
			BotaniaBlockEntities.ANIMATED_TORCH, BotaniaBlockEntities.HOURGLASS, BotaniaBlockEntities.PRISM
	);

	public static final Set<BlockEntityType<? extends ManaReceiver>> SELF_MANA_RECEIVER_BES = ImmutableSet.of(
			BotaniaBlockEntities.AVATAR, BotaniaBlockEntities.BREWERY, BotaniaBlockEntities.DISTRIBUTOR, BotaniaBlockEntities.ENCHANTER,
			BotaniaBlockEntities.POOL, BotaniaBlockEntities.FLUXFIELD, BotaniaBlockEntities.RUNE_ALTAR,
			BotaniaBlockEntities.SPAWNER_CLAW, BotaniaBlockEntities.SPREADER, BotaniaBlockEntities.TERRA_PLATE
	);

	public static final Set<BlockEntityType<? extends SparkAttachable>> SELF_SPARK_ATTACHABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.ENCHANTER, BotaniaBlockEntities.POOL, BotaniaBlockEntities.TERRA_PLATE
	);

	public static final Set<BlockEntityType<? extends WorldlyContainer>> SELF_WORLDLY_CONTAINERS = ImmutableSet.of(
			BotaniaBlockEntities.PRISM, BotaniaBlockEntities.SPREADER, BotaniaBlockEntities.HOURGLASS,
			BotaniaBlockEntities.INCENSE_PLATE, BotaniaBlockEntities.OPEN_CRATE, BotaniaBlockEntities.SPARK_CHANGER,
			BotaniaBlockEntities.TINY_POTATO
	);

	private BlockEntityConstants() {}
}
