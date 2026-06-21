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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BifrostBlockEntity extends BlockEntity {
	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	public BifrostBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.BIFROST_BRIDGE, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, BifrostBlockEntity self) {
		if (self.ticks <= 0) {
			level.removeBlock(pos, false);
		} else {
			self.ticks--;
			level.blockEntityChanged(pos);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putInt(TAG_TICKS, ticks);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		ticks = tag.getInt(TAG_TICKS);
	}

}
