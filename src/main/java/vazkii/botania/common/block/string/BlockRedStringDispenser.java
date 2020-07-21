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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;

import javax.annotation.Nonnull;

public class BlockRedStringDispenser extends BlockRedString {

	public BlockRedStringDispenser(Block.Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.FACING, Direction.DOWN).with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (power && !powered) {
			((TileRedStringDispenser) world.getBlockEntity(pos)).tickDispenser();
			world.setBlockState(pos, state.with(Properties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(Properties.POWERED, false), 4);
		}
	}

	@Nonnull
	@Override
	public TileRedString createBlockEntity(@Nonnull BlockView world) {
		return new TileRedStringDispenser();
	}
}
