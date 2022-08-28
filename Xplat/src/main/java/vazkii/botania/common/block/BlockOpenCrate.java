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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;

public class BlockOpenCrate extends BlockMod implements EntityBlock {

	protected BlockOpenCrate(Properties builder) {
		super(builder);
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
		if (!newState.is(state.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory inventory) {
				Containers.dropContents(world, pos, inventory.getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (world.hasNeighborSignal(pos) && rand.nextDouble() < 0.2) {
			redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}

	public static void redstoneParticlesOnFullBlock(Level world, BlockPos pos, RandomSource random) {
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

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TileOpenCrate(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, ModTiles.OPEN_CRATE, TileOpenCrate::serverTick);
		}
		return null;
	}
}
