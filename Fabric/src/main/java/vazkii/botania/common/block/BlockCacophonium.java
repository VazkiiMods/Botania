/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.tile.TileCacophonium;

import javax.annotation.Nonnull;

public class BlockCacophonium extends BlockMod implements EntityBlock {
	protected BlockCacophonium(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof TileCacophonium) {
				((TileCacophonium) tile).annoyDirewolf();
			}
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TileCacophonium) {
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((TileCacophonium) te).stack);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileCacophonium(pos, state);
	}

}
