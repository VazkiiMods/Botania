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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.block_entity.CacophoniumBlockEntity;

public class CacophoniumBlock extends BotaniaEntityBlock {
	protected CacophoniumBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		boolean power = level.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			if (level.getBlockEntity(pos) instanceof CacophoniumBlockEntity cacophonium) {
				cacophonium.annoyDirewolf();
			}
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), Block.UPDATE_INVISIBLE);
		} else if (!power && powered) {
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), Block.UPDATE_INVISIBLE);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof CacophoniumBlockEntity cacophonium) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cacophonium.stack);
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CacophoniumBlockEntity(pos, state);
	}

}
