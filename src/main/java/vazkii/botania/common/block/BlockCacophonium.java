/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 23, 2015, 7:23:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockCacophonium extends BlockMod {

	protected BlockCacophonium(Builder builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public boolean canSilkHarvest(@Nonnull IBlockState state, IWorldReader world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BotaniaStateProps.POWERED);

		if(power && !powered) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileCacophonium)
				((TileCacophonium) tile).annoyDirewolf();
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true), 4);
		} else if(!power && powered)
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 4);
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest, IFluidState fluid) {
		if (willHarvest) {
			// Copy of super.removedByPlayer but don't set to air yet
			// This is so getDrops below will have a TE to work with
			onBlockHarvested(world, pos, state, player);
			return true;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
	}

	@Override
	public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		// Now delete the block and TE
		world.removeBlock(pos);
	}

	@Override
	public void getDrops(@Nonnull IBlockState state, NonNullList<ItemStack> stacks, World world, BlockPos pos, int fortune) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileCacophonium) {
			stacks.add(new ItemStack(Blocks.NOTE_BLOCK));
			ItemStack thingy = ((TileCacophonium) tile).stack;
			if(!thingy.isEmpty())
				stacks.add(thingy.copy());
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileCacophonium();
	}

}
