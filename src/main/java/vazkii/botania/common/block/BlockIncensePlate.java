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
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;

public class BlockIncensePlate extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape X_SHAPE = createCuboidShape(6, 0, 2, 10, 1, 14);
	private static final VoxelShape Z_SHAPE = createCuboidShape(2, 0, 6, 14, 1, 10);

	protected BlockIncensePlate(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileIncensePlate plate = (TileIncensePlate) world.getBlockEntity(pos);
		ItemStack plateStack = plate.getItemHandler().getStack(0);
		ItemStack stack = player.getStackInHand(hand);
		boolean did = false;

		if (world.isClient) {
			if (state.get(Properties.WATERLOGGED)
					&& !plateStack.isEmpty()
					&& !plate.burning
					&& !stack.isEmpty()
					&& stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.spawnSmokeParticles();
			}
			return ActionResult.SUCCESS;
		}

		if (plateStack.isEmpty() && plate.acceptsItem(stack)) {
			plate.getItemHandler().setStack(0, stack.copy());
			stack.decrement(1);
			did = true;
		} else if (!plateStack.isEmpty() && !plate.burning) {
			if (!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
				plate.ignite();
				stack.damage(1, player, e -> e.sendToolBreakStatus(hand));
				did = true;
			} else {
				player.inventory.offerOrDrop(player.world, plateStack);
				plate.getItemHandler().setStack(0, ItemStack.EMPTY);

				did = true;
			}
		}

		if (did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
		}

		return did ? ActionResult.SUCCESS : ActionResult.PASS;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getPlayerFacing().getOpposite());
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ((TileIncensePlate) world.getBlockEntity(pos)).comparatorOutput;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		if (state.get(Properties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileIncensePlate();
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileIncensePlate plate = (TileIncensePlate) world.getBlockEntity(pos);
			if (plate != null && !plate.burning) {
				ItemScatterer.spawn(world, pos, plate.getItemHandler());
			}
		}
		super.onStateReplaced(state, world, pos, newState, isMoving);
	}

}
