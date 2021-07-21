/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockOpenCrate extends BlockMod implements ITileEntityProvider {

	protected BlockOpenCrate(Properties builder) {
		super(builder);
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (newState.getBlock() != state.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileSimpleInventory) {
				InventoryHelper.dropInventoryItems(world, pos, ((TileSimpleInventory) te).getItemHandler());
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (world.isBlockPowered(pos) && rand.nextDouble() < 0.2) {
			redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}

	public static void redstoneParticlesOnFullBlock(World world, BlockPos pos, Random random) {
		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.offset(direction);
			if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
				Direction.Axis axis = direction.getAxis();
				double dx = axis == Direction.Axis.X ? 0.5D + 0.5625D * direction.getXOffset() : random.nextFloat();
				double dy = axis == Direction.Axis.Y ? 0.5D + 0.5625D * direction.getYOffset() : random.nextFloat();
				double dz = axis == Direction.Axis.Z ? 0.5D + 0.5625D * direction.getZOffset() : random.nextFloat();
				world.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileOpenCrate();
	}

}
