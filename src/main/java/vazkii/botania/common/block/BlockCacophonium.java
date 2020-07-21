/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileCacophonium;

import javax.annotation.Nonnull;

public class BlockCacophonium extends BlockMod implements BlockEntityProvider {
	protected BlockCacophonium(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (power && !powered) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TileCacophonium) {
				((TileCacophonium) tile).annoyDirewolf();
			}
			world.setBlockState(pos, state.with(Properties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(Properties.POWERED, false), 4);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TileCacophonium) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((TileCacophonium) te).stack);
			}
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileCacophonium();
	}

}
