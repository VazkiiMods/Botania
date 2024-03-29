/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

public class RedStringComparatorBlockEntity extends RedStringBlockEntity {
	private int comparatorValue = 0;

	public RedStringComparatorBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.RED_STRING_COMPARATOR, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, RedStringComparatorBlockEntity self) {
		RedStringBlockEntity.commonTick(level, worldPosition, state, self);
		BlockPos binding = self.getBinding();
		BlockState bindState = self.getStateAtBinding();
		int origVal = self.comparatorValue;

		if (bindState.hasAnalogOutputSignal()) {
			self.comparatorValue = bindState.getAnalogOutputSignal(level, binding);
		} else {
			self.comparatorValue = 0;
		}

		if (origVal != self.comparatorValue) {
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}
	}

	public int getComparatorValue() {
		return comparatorValue;
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return level.getBlockState(pos).hasAnalogOutputSignal();
	}

}
