/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.corporea.CorporeaRetainerBlockEntity;

public class CorporeaRetainerBlock extends BotaniaBlock implements EntityBlock {

	public CorporeaRetainerBlock(BlockBehaviour.Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
		boolean power = false;
		for (var direction : Direction.values()) {
			var neighborPos = pos.relative(direction);
			var neighborState = world.getBlockState(neighborPos);
			if (!neighborState.is(BotaniaBlocks.corporeaInterceptor)) {
				if (world.getSignal(neighborPos, direction) > 0) {
					power = true;
					break;
				}
			}
		}
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			((CorporeaRetainerBlockEntity) world.getBlockEntity(pos)).fulfilRequest();
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, true));
		} else if (!power && powered) {
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((CorporeaRetainerBlockEntity) world.getBlockEntity(pos)).getComparatorValue();
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new CorporeaRetainerBlockEntity(pos, state);
	}
}
