/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.block.tile.TileFakeAir;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockFakeAir extends AirBlock {

	public BlockFakeAir(Properties builder) {
		super(builder);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (shouldRemove(world, pos)) {
			world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
		}
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isRemote && world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(pos)).canStay();
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (shouldRemove(world, pos)) {
			world.setBlockState(pos, rand.nextInt(10) == 0 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 4;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileFakeAir();
	}
}
