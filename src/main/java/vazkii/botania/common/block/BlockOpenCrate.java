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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockOpenCrate extends BlockMod implements EntityBlock {

	protected BlockOpenCrate(Properties builder) {
		super(builder);
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (newState.getBlock() != state.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (world.hasNeighborSignal(pos) && rand.nextDouble() < 0.2) {
			redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}

	public static void redstoneParticlesOnFullBlock(Level world, BlockPos pos, Random random) {
		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
				Direction.Axis axis = direction.getAxis();
				double dx = axis == Direction.Axis.X ? 0.5D + 0.5625D * direction.getStepX() : random.nextFloat();
				double dy = axis == Direction.Axis.Y ? 0.5D + 0.5625D * direction.getStepY() : random.nextFloat();
				double dz = axis == Direction.Axis.Z ? 0.5D + 0.5625D * direction.getStepZ() : random.nextFloat();
				world.addParticle(DustParticleOptions.REDSTONE, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileOpenCrate();
	}

}
