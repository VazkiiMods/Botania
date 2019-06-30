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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileCacophonium;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockCacophonium extends BlockMod {

	protected BlockCacophonium(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
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

	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
		List<ItemStack> stacks = super.getDrops(state, builder);
		TileEntity te = builder.get(LootParameters.BLOCK_ENTITY);
		if(te instanceof TileCacophonium) {
			ItemStack thingy = ((TileCacophonium) te).stack;
			if(!thingy.isEmpty())
				stacks.add(thingy.copy());
		}
		return stacks;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileCacophonium();
	}

}
