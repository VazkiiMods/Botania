/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 16, 2014, 10:22:01 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class TileRedStringComparator extends TileRedString {

	private int comparatorValue = 0;

	@Override
	public void update() {
		super.update();
		BlockPos binding = getBinding();
		IBlockState state = getStateAtBinding();
		int origVal = comparatorValue;

		if(state.hasComparatorInputOverride()) {
			comparatorValue = state.getComparatorInputOverride(world, binding);
		} else comparatorValue = 0;

		if(origVal != comparatorValue)
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}

	public int getComparatorValue() {
		return comparatorValue;
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockState(pos).hasComparatorInputOverride();
	}

}
