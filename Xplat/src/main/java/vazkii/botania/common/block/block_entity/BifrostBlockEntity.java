/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.tile.ModTiles;

public class BifrostBlockEntity extends BotaniaBlockEntity {
	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	public BifrostBlockEntity(BlockPos pos, BlockState state) {
		super(ModTiles.BIFROST, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, BifrostBlockEntity self) {
		if (self.ticks <= 0) {
			level.removeBlock(worldPosition, false);
		} else {
			self.ticks--;
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt(TAG_TICKS, ticks);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);
		ticks = tag.getInt(TAG_TICKS);
	}

}
