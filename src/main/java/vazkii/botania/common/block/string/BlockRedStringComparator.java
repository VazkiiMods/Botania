/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 16, 2014, 10:19:28 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringComparator;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockRedStringComparator extends BlockRedString {

	public BlockRedStringComparator() {
		super(LibBlockNames.RED_STRING_COMPARATOR);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.FACING, EnumFacing.DOWN));
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileRedStringComparator) world.getTileEntity(pos)).getComparatorValue();
	}

	@Nonnull
	@Override
	public TileRedString createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileRedStringComparator();
	}

}
