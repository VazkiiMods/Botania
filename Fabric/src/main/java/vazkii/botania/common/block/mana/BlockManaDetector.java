/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockMod;

import java.util.Random;

public class BlockManaDetector extends BlockMod implements IManaTrigger, IManaCollisionGhost {

	public BlockManaDetector(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public boolean isGhost(BlockState state, Level world, BlockPos pos) {
		// The burst will call onBurstCollision, but will still pass through this block.
		// TODO clarify the relationship between IManaTrigger and IManaCollisionGhost
		return false;
	}

	@Override
	public void onBurstCollision(IManaBurst burst, Level world, BlockPos pos) {
		if (!world.isClientSide && !burst.isFake()) {
			BlockState state = world.getBlockState(pos);
			if (!state.getValue(BlockStateProperties.POWERED) && !world.getBlockTicks().hasScheduledTick(pos, this)) {
				world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, true));
				world.scheduleTick(pos, this, 4);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide && !state.is(oldState.getBlock())
				&& state.getValue(BlockStateProperties.POWERED)
				&& !level.getBlockTicks().hasScheduledTick(pos, this)) {
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 18);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			for (int i = 0; i < 4; i++) {
				SparkleParticleData data = SparkleParticleData.sparkle(0.7F + 0.5F * (float) Math.random(), 1F, 0.2F, 0.2F, 5);
				level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
			}
		}
	}

}
