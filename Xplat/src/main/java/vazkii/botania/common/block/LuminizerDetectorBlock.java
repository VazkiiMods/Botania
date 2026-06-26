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
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.entity.LuminizerMoverEntity;

public class LuminizerDetectorBlock extends LuminizerPoweredBlock {

	public static final int POWERED_EVENT = 0;

	public LuminizerDetectorBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (!state.is(oldState.getBlock())) {
			if (!level.isClientSide() && state.getValue(POWERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
				level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.setBlockAndUpdate(pos, state.setValue(POWERED, false));
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction s) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	public void onMoverPassing(Level level, BlockState state, BlockPos pos, LuminizerMoverEntity entity) {
		level.setBlockAndUpdate(pos, state.setValue(POWERED, true));
		level.scheduleTick(pos, state.getBlock(), 2);
		level.blockEvent(pos, state.getBlock(), POWERED_EVENT, 0);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
		if (id == POWERED_EVENT) {
			if (world.isClientSide()) {
				RandomSource random = RandomSource.create();
				for (int i = 0; i < 5; i++) {
					addRedstoneParticle(world, pos, random);
				}
			}
			return true;
		}
		return false;
	}

}
