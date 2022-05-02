package vazkii.botania.common.block.tile;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.common.block.ModSubtiles;

import java.util.Set;

public final class BlockEntityConstants {
	public static final Set<BlockEntityType<?>> SELF_WANDADBLE_BES = ImmutableSet.of(ModTiles.ALF_PORTAL, ModTiles.ANIMATED_TORCH, ModTiles.CORPOREA_CRYSTAL_CUBE, ModTiles.CORPOREA_RETAINER,
			ModTiles.CRAFT_CRATE, ModTiles.ENCHANTER, ModTiles.HOURGLASS, ModTiles.PLATFORM, ModTiles.POOL,
			ModTiles.RUNE_ALTAR, ModTiles.SPREADER, ModTiles.TURNTABLE,
			ModSubtiles.DAFFOMILL, ModSubtiles.HOPPERHOCK, ModSubtiles.HOPPERHOCK_CHIBI,
			ModSubtiles.RANNUNCARPUS, ModSubtiles.RANNUNCARPUS_CHIBI
	);

	public static final Set<BlockEntityType<?>> SELF_MANA_TRIGGER_BES = ImmutableSet.of(
			ModTiles.ANIMATED_TORCH, ModTiles.HOURGLASS, ModTiles.PRISM
	);

	public static final Set<BlockEntityType<?>> SELF_MANA_RECEIVER_BES = ImmutableSet.of(
			ModTiles.AVATAR, ModTiles.BREWERY, ModTiles.DISTRIBUTOR, ModTiles.ENCHANTER,
			ModTiles.POOL, ModTiles.FLUXFIELD, ModTiles.RUNE_ALTAR,
			ModTiles.SPAWNER_CLAW, ModTiles.SPREADER, ModTiles.TERRA_PLATE
	);

	public static final Set<BlockEntityType<?>> SELF_SPARK_ATTACHABLE_BES = ImmutableSet.of(
			ModTiles.ENCHANTER, ModTiles.POOL, ModTiles.TERRA_PLATE
	);

	private BlockEntityConstants() {}
}
