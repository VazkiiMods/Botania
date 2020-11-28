/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockRedStringInterceptor extends BlockRedString {

	public BlockRedStringInterceptor(AbstractBlock.Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.FACING, Direction.DOWN).with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	public static ActionResult onInteract(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
		return TileRedStringInterceptor.onInteract(player, world, hit.getBlockPos(), hand);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction side) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random update) {
		world.setBlockState(pos, state.with(Properties.POWERED, false));
	}

	@Nonnull
	@Override
	public TileRedString createBlockEntity(@Nonnull BlockView world) {
		return new TileRedStringInterceptor();
	}
}
