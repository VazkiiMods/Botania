/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 21, 2015, 4:56:52 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockRedStringInterceptor extends BlockRedString {

	public BlockRedStringInterceptor(Block.Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.FACING, EnumFacing.DOWN).with(BotaniaStateProps.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BotaniaStateProps.POWERED);
	}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
		TileRedStringInterceptor.onInteract(event.getEntityPlayer(), event.getWorld(), event.getPos(), event.getHand());
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing side) {
		return state.get(BotaniaStateProps.POWERED) ? 15 : 0;
	}

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random update) {
		world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 1 | 2);
	}

	@Override
	public int tickRate(IWorldReaderBase world) {
		return 2;
	}

	@Nonnull
	@Override
	public TileRedString createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileRedStringInterceptor();
	}
}
