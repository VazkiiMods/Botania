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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileRedStringComparator extends TileRedString {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.RED_STRING_COMPARATOR) public static TileEntityType<TileRedStringComparator> TYPE;
	private int comparatorValue = 0;

	public TileRedStringComparator() {
		super(TYPE);
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
