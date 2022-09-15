package vazkii.botania.common.block.block_entity;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.Set;

public final class BlockEntityConstants {
	public static final Set<BlockEntityType<?>> SELF_WANDADBLE_BES = ImmutableSet.of(BotaniaBlockEntities.ALF_PORTAL, BotaniaBlockEntities.ANIMATED_TORCH, BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, BotaniaBlockEntities.CORPOREA_RETAINER,
			BotaniaBlockEntities.CRAFT_CRATE, BotaniaBlockEntities.ENCHANTER, BotaniaBlockEntities.HOURGLASS, BotaniaBlockEntities.PLATFORM, BotaniaBlockEntities.POOL,
			BotaniaBlockEntities.RUNE_ALTAR, BotaniaBlockEntities.SPREADER, BotaniaBlockEntities.TURNTABLE,
			BotaniaFlowerBlocks.DAFFOMILL, BotaniaFlowerBlocks.HOPPERHOCK, BotaniaFlowerBlocks.HOPPERHOCK_CHIBI,
			BotaniaFlowerBlocks.RANNUNCARPUS, BotaniaFlowerBlocks.RANNUNCARPUS_CHIBI
	);

	public static final Set<BlockEntityType<?>> SELF_MANA_TRIGGER_BES = ImmutableSet.of(
			BotaniaBlockEntities.ANIMATED_TORCH, BotaniaBlockEntities.HOURGLASS, BotaniaBlockEntities.PRISM
	);

	public static final Set<BlockEntityType<?>> SELF_MANA_RECEIVER_BES = ImmutableSet.of(
			BotaniaBlockEntities.AVATAR, BotaniaBlockEntities.BREWERY, BotaniaBlockEntities.DISTRIBUTOR, BotaniaBlockEntities.ENCHANTER,
			BotaniaBlockEntities.POOL, BotaniaBlockEntities.FLUXFIELD, BotaniaBlockEntities.RUNE_ALTAR,
			BotaniaBlockEntities.SPAWNER_CLAW, BotaniaBlockEntities.SPREADER, BotaniaBlockEntities.TERRA_PLATE
	);

	public static final Set<BlockEntityType<?>> SELF_SPARK_ATTACHABLE_BES = ImmutableSet.of(
			BotaniaBlockEntities.ENCHANTER, BotaniaBlockEntities.POOL, BotaniaBlockEntities.TERRA_PLATE
	);

	private BlockEntityConstants() {}
}
