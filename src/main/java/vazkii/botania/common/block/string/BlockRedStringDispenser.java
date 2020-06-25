/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;

import javax.annotation.Nonnull;

public class BlockRedStringDispenser extends BlockRedString {

	public BlockRedStringDispenser(Block.Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.FACING, Direction.DOWN).with(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (power && !powered) {
			((TileRedStringDispenser) world.getTileEntity(pos)).tickDispenser();
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Nonnull
	@Override
	public TileRedString createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileRedStringDispenser();
	}
}
