/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileFakeAir;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockFakeAir extends AirBlock implements BlockEntityProvider {

	public BlockFakeAir(Settings builder) {
		super(builder);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (shouldRemove(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 4);
		}
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isClient && world.getBlockEntity(pos) == null || !(world.getBlockEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getBlockEntity(pos)).canStay();
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (shouldRemove(world, pos)) {
			world.setBlockState(pos, rand.nextInt(10) == 0 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileFakeAir();
	}
}
