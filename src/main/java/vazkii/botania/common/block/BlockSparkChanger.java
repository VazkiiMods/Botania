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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ItemSparkUpgrade;

import javax.annotation.Nonnull;

public class BlockSparkChanger extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 3, 16);

	public BlockSparkChanger(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, true));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (power && !powered) {
			((TileSparkChanger) world.getBlockEntity(pos)).doSwap();
			world.setBlockState(pos, state.with(Properties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(Properties.POWERED, false), 4);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileSparkChanger changer = (TileSparkChanger) world.getBlockEntity(pos);
		ItemStack pstack = player.getStackInHand(hand);
		ItemStack cstack = changer.getItemHandler().getStack(0);
		if (!cstack.isEmpty()) {
			changer.getItemHandler().setStack(0, ItemStack.EMPTY);
			player.inventory.offerOrDrop(player.world, cstack);
			return ActionResult.SUCCESS;
		} else if (!pstack.isEmpty() && pstack.getItem() instanceof ItemSparkUpgrade) {
			changer.getItemHandler().setStack(0, pstack.split(1));
			changer.markDirty();

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TileSparkChanger changer = (TileSparkChanger) world.getBlockEntity(pos);
		ItemStack stack = changer.getItemHandler().getStack(0);
		if (!stack.isEmpty() && stack.getItem() instanceof ItemSparkUpgrade) {
			return ((ItemSparkUpgrade) stack.getItem()).type.ordinal() + 1;
		}
		return 0;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileSparkChanger();
	}

}
