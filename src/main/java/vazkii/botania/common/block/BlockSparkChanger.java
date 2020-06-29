/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ItemSparkUpgrade;

import javax.annotation.Nonnull;

public class BlockSparkChanger extends BlockModWaterloggable implements ITileEntityProvider {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 3, 16);

	public BlockSparkChanger(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, true));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (power && !powered) {
			((TileSparkChanger) world.getTileEntity(pos)).doSwap();
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileSparkChanger changer = (TileSparkChanger) world.getTileEntity(pos);
		ItemStack pstack = player.getHeldItem(hand);
		ItemStack cstack = changer.getItemHandler().getStackInSlot(0);
		if (!cstack.isEmpty()) {
			changer.getItemHandler().setInventorySlotContents(0, ItemStack.EMPTY);
			player.inventory.placeItemBackInInventory(player.world, cstack);
			return ActionResultType.SUCCESS;
		} else if (!pstack.isEmpty() && pstack.getItem() instanceof ItemSparkUpgrade) {
			changer.getItemHandler().setInventorySlotContents(0, pstack.split(1));
			changer.markDirty();

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TileSparkChanger changer = (TileSparkChanger) world.getTileEntity(pos);
		ItemStack stack = changer.getItemHandler().getStackInSlot(0);
		if (!stack.isEmpty() && stack.getItem() instanceof ItemSparkUpgrade) {
			return ((ItemSparkUpgrade) stack.getItem()).type.ordinal() + 1;
		}
		return 0;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileSparkChanger();
	}

}
