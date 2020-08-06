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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.block.tile.TileFakeAir;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockFakeAir extends AirBlock implements ITileEntityProvider {

	public BlockFakeAir(Properties builder) {
		super(builder);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (shouldRemove(world, pos)) {
			world.getPendingBlockTicks().scheduleTick(pos, this, 4);
		}
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isRemote && world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(pos)).canStay();
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (shouldRemove(world, pos)) {
			world.setBlockState(pos, rand.nextInt(10) == 0 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileFakeAir();
	}
}
