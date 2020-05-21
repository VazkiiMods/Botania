/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Random;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockRedStringInterceptor extends BlockRedString {

	public BlockRedStringInterceptor(Block.Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.FACING, Direction.DOWN).with(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
		TileRedStringInterceptor.onInteract(event.getPlayer(), event.getWorld(), event.getPos(), event.getHand());
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return state.get(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random update) {
		world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 2;
	}

	@Nonnull
	@Override
	public TileRedString createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileRedStringInterceptor();
	}
}
