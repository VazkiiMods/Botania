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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.common.block.block_entity.LuminizerBlockEntity;

public class LuminizerToggleBlock extends LuminizerPoweredBlock {

	public LuminizerToggleBlock(Properties builder) {
		super(builder);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos,
			boolean movedByPiston) {
		if (!level.isClientSide) {
			if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
				level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS);
			} else if (!state.getValue(POWERED) && level.hasNeighborSignal(pos)) {
				level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Nullable
	public BlockPos getNextDestination(Level level, BlockState state, BlockPos blockPos, LuminizerBlockEntity blockEntity) {
		return state.getValue(POWERED)
				? null
				: super.getNextDestination(level, state, blockPos, blockEntity);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED)) {
			addRedstoneParticle(level, pos, random);
		}
	}
}
