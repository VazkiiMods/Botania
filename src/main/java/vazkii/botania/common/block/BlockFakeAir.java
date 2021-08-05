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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.tile.TileFakeAir;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockFakeAir extends AirBlock implements EntityBlock {

	public BlockFakeAir(Properties builder) {
		super(builder);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (shouldRemove(world, pos)) {
			world.getBlockTicks().scheduleTick(pos, this, 4);
		}
	}

	private boolean shouldRemove(Level world, BlockPos pos) {
		return !world.isClientSide && world.getBlockEntity(pos) == null || !(world.getBlockEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getBlockEntity(pos)).canStay();
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (shouldRemove(world, pos)) {
			world.setBlockAndUpdate(pos, rand.nextInt(10) == 0 ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileFakeAir(pos, state);
	}
}
