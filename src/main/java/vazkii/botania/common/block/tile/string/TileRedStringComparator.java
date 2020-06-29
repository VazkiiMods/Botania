/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.common.block.tile.ModTiles;

public class TileRedStringComparator extends TileRedString {
	private int comparatorValue = 0;

	public TileRedStringComparator() {
		super(ModTiles.RED_STRING_COMPARATOR);
	}

	@Override
	public void tick() {
		super.tick();
		BlockPos binding = getBinding();
		BlockState state = getStateAtBinding();
		int origVal = comparatorValue;

		if (state.hasComparatorInputOverride()) {
			comparatorValue = state.getComparatorInputOverride(world, binding);
		} else {
			comparatorValue = 0;
		}

		if (origVal != comparatorValue) {
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}
	}

	public int getComparatorValue() {
		return comparatorValue;
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockState(pos).hasComparatorInputOverride();
	}

}
