/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

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
			BotaniaBlockEntities.ALFHEIM_PORTAL, BotaniaBlockEntities.ANIMATED_TORCH,
			BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, BotaniaBlockEntities.CORPOREA_RETAINER,
			BotaniaBlockEntities.CRAFTY_CRATE, BotaniaBlockEntities.MANA_ENCHANTER,
			BotaniaBlockEntities.EYE_OF_THE_ANCIENTS, BotaniaBlockEntities.HOVERING_HOURGLASS,
			BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.MANA_POOL, BotaniaBlockEntities.RUNIC_ALTAR,
			BotaniaBlockEntities.MANA_SPREADER, BotaniaBlockEntities.SPREADER_TURNTABLE, BotaniaBlockEntities.DAFFOMILL,
			BotaniaBlockEntities.HOPPERHOCK, BotaniaBlockEntities.HOPPERHOCK_PETITE, BotaniaBlockEntities.POLLIDISIAC,
			BotaniaBlockEntities.RANNUNCARPUS, BotaniaBlockEntities.RANNUNCARPUS_PETITE
	);

	public static final Set<BlockEntityType<? extends WandBindable>> SELF_WAND_BINDABLE_BES = ImmutableSet.of(
			// generating flowers
			BotaniaBlockEntities.DANDELIFEON, BotaniaBlockEntities.ENDOFLAME, BotaniaBlockEntities.ENTROPINNYUM,
			BotaniaBlockEntities.GOURMARYLLIS, BotaniaBlockEntities.KEKIMURUS, BotaniaBlockEntities.MUNCHDEW,
			BotaniaBlockEntities.NARSLIMMUS, BotaniaBlockEntities.RAFFLOWSIA, BotaniaBlockEntities.ROSA_ARCANA,
			BotaniaBlockEntities.SHULK_ME_NOT, BotaniaBlockEntities.SPECTROLUS,

			// functional flowers
			BotaniaBlockEntities.AGRICARNATION, BotaniaBlockEntities.AGRICARNATION_PETITE,
			BotaniaBlockEntities.BELLETHORNE, BotaniaBlockEntities.BELLETHORNE_PETITE, BotaniaBlockEntities.BUBBELL,
			BotaniaBlockEntities.BUBBELL_PETITE, BotaniaBlockEntities.CLAYCONIA, BotaniaBlockEntities.CLAYCONIA_PETITE,
			BotaniaBlockEntities.DAFFOMILL, BotaniaBlockEntities.DREADTHORN, BotaniaBlockEntities.EXOFLAME,
			BotaniaBlockEntities.FALLEN_KANADE, BotaniaBlockEntities.HEISEI_DREAM, BotaniaBlockEntities.HOPPERHOCK,
			BotaniaBlockEntities.HOPPERHOCK_PETITE, BotaniaBlockEntities.HYACIDUS,
			BotaniaBlockEntities.JADED_AMARANTHUS, BotaniaBlockEntities.JIYUULIA, BotaniaBlockEntities.JIYUULIA_PETITE,
			BotaniaBlockEntities.LABELLIA, BotaniaBlockEntities.LOONIUM, BotaniaBlockEntities.MARIMORPHOSIS,
			BotaniaBlockEntities.MARIMORPHOSIS_PETITE, BotaniaBlockEntities.MEDUMONE, BotaniaBlockEntities.ORECHID,
			BotaniaBlockEntities.ORECHID_IGNEM, BotaniaBlockEntities.POLLIDISIAC, BotaniaBlockEntities.RANNUNCARPUS,
			BotaniaBlockEntities.RANNUNCARPUS_PETITE, BotaniaBlockEntities.SPECTRANTHEMUM,
			BotaniaBlockEntities.TANGLEBERRIE, BotaniaBlockEntities.TANGLEBERRIE_PETITE, BotaniaBlockEntities.TIGERSEYE,
			BotaniaBlockEntities.VINCULOTUS,

			// other
			BotaniaBlockEntities.LUMINIZER, BotaniaBlockEntities.MANA_SPREADER
	);

	public static final Set<BlockEntityType<? extends PhantomInkableBlock>> SELF_PHANTOM_INKABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.LUMINIZER, BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE
	);

	public static final Set<BlockEntityType<? extends ManaTrigger>> SELF_MANA_TRIGGER_BES = ImmutableSet.of(
			BotaniaBlockEntities.ANIMATED_TORCH, BotaniaBlockEntities.HOVERING_HOURGLASS,
			BotaniaBlockEntities.MANA_PRISM
	);

	public static final Set<BlockEntityType<? extends ManaReceiver>> SELF_MANA_RECEIVER_BES = ImmutableSet.of(
			BotaniaBlockEntities.AVATAR, BotaniaBlockEntities.BOTANICAL_BREWERY, BotaniaBlockEntities.MANA_SPLITTER,
			BotaniaBlockEntities.MANA_ENCHANTER, BotaniaBlockEntities.MANA_POOL, BotaniaBlockEntities.MANA_FLUXFIELD,
			BotaniaBlockEntities.RUNIC_ALTAR, BotaniaBlockEntities.LIFE_IMBUER, BotaniaBlockEntities.MANA_SPREADER,
			BotaniaBlockEntities.TERRESTRIAL_AGGLOMERATION_PLATE
	);

	public static final Set<BlockEntityType<? extends SparkAttachable>> SELF_SPARK_ATTACHABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.MANA_ENCHANTER, BotaniaBlockEntities.MANA_POOL,
			BotaniaBlockEntities.TERRESTRIAL_AGGLOMERATION_PLATE
	);

	public static final Set<BlockEntityType<? extends WorldlyContainer>> SELF_WORLDLY_CONTAINERS = ImmutableSet.of(
			BotaniaBlockEntities.MANA_PRISM, BotaniaBlockEntities.MANA_SPREADER,
			BotaniaBlockEntities.HOVERING_HOURGLASS, BotaniaBlockEntities.INCENSE_PLATE,
			BotaniaBlockEntities.OPEN_CRATE, BotaniaBlockEntities.SPARK_TINKERER, BotaniaBlockEntities.TINY_POTATO
	);

	private BlockEntityConstants() {}
}
