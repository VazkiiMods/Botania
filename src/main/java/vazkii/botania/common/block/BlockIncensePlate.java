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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;

public class BlockIncensePlate extends BlockModWaterloggable implements ITileEntityProvider {

	private static final VoxelShape X_SHAPE = makeCuboidShape(6, 0, 2, 10, 1, 14);
	private static final VoxelShape Z_SHAPE = makeCuboidShape(2, 0, 6, 14, 1, 10);

	protected BlockIncensePlate(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileIncensePlate plate = (TileIncensePlate) world.getTileEntity(pos);
		ItemStack plateStack = plate.getItemHandler().getStackInSlot(0);
		ItemStack stack = player.getHeldItem(hand);
		boolean did = false;

		if (world.isRemote) {
			if (state.get(BlockStateProperties.WATERLOGGED)
					&& !plateStack.isEmpty()
					&& !plate.burning
					&& !stack.isEmpty()
					&& stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.spawnSmokeParticles();
			}
			return ActionResultType.SUCCESS;
		}

		if (plateStack.isEmpty() && plate.acceptsItem(stack)) {
			plate.getItemHandler().setInventorySlotContents(0, stack.copy());
			stack.shrink(1);
			did = true;
		} else if (!plateStack.isEmpty() && !plate.burning) {
			if (!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.ignite();
				stack.damageItem(1, player, e -> e.sendBreakAnimation(hand));
				did = true;
			} else {
				player.inventory.placeItemBackInInventory(player.world, plateStack);
				plate.getItemHandler().setInventorySlotContents(0, ItemStack.EMPTY);

				did = true;
			}
		}

		if (did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
		}

		return did ? ActionResultType.SUCCESS : ActionResultType.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TileIncensePlate) world.getTileEntity(pos)).comparatorOutput;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		if (state.get(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileIncensePlate();
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity plate = world.getTileEntity(pos);
			if (plate instanceof TileIncensePlate && !((TileIncensePlate) plate).burning) {
				InventoryHelper.dropInventoryItems(world, pos, ((TileIncensePlate) plate).getItemHandler());
			}
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}

}
