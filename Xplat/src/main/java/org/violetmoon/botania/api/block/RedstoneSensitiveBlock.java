/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.UnknownNullability;

/**
 * Common code for redstone-sensitive blocks. Blocks implementing this interface should:
 * <ul>
 * <li>Override {@link Block#createBlockStateDefinition(StateDefinition.Builder)} to add
 * {@link BlockStateProperties#POWERED}.</li>
 * <li>Call {@link Block#registerDefaultState(net.minecraft.world.level.block.state.BlockState)} in constructor to have
 * {@link BlockStateProperties#POWERED} default to <code>false</code>.</li>
 * <li>Override {@link Block#getStateForPlacement(BlockPlaceContext)} to include a call to
 * {@link #getPoweredStateForPlacement(BlockState, BlockPlaceContext)}.</li>
 * <li>Override
 * {@link net.minecraft.world.level.block.state.BlockBehaviour#neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)}
 * and call {@link #updateRedstonePower(BlockState, Level, BlockPos)}.</li>
 * <li>Optionally override {@link Block#animateTick(BlockState, Level, BlockPos, RandomSource)} to include a call to
 * {@link #redstoneParticlesInShape(BlockState, Level, BlockPos, RandomSource)} if {@link #isPowered(BlockState)}
 * returns <code>true</code>.</li>
 * </ul>
 */
@SuppressWarnings("JavadocReference")
public interface RedstoneSensitiveBlock {
	static void redstoneParticlesInShape(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (rand.nextBoolean()) {
			VoxelShape shape = state.getShape(world, pos);
			if (!shape.isEmpty()) {
				AABB localBox = shape.bounds();
				double x = pos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
				double y = pos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
				double z = pos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
				world.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
			}
		}
	}

	static void updateRedstonePower(BlockState state, Level world, BlockPos pos) {
		boolean isPowered = world.hasNeighborSignal(pos);
		if (isPowered != state.getValue(BlockStateProperties.POWERED)) {
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, isPowered));
		}
	}

	static BlockState getPoweredStateForPlacement(@UnknownNullability BlockState defaultState, BlockPlaceContext context) {
		return defaultState.setValue(BlockStateProperties.POWERED,
				context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	default boolean isPowered(BlockState state) {
		return state.getValue(BlockStateProperties.POWERED);
	}
}
