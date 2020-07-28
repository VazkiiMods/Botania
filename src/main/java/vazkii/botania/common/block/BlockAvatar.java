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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

public class BlockAvatar extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape X_AABB = createCuboidShape(5, 0, 3.5, 11, 17, 12.5);
	private static final VoxelShape Z_AABB = createCuboidShape(3.5, 0, 5, 12.5, 17, 11);

	protected BlockAvatar(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		if (state.get(Properties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_AABB;
		} else {
			return Z_AABB;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileAvatar avatar = (TileAvatar) world.getBlockEntity(pos);
		ItemStack stackOnAvatar = avatar.getItemHandler().getStack(0);
		ItemStack stackOnPlayer = player.getStackInHand(hand);
		if (!stackOnAvatar.isEmpty()) {
			avatar.getItemHandler().setStack(0, ItemStack.EMPTY);
			player.inventory.offerOrDrop(player.world, stackOnAvatar);
			return ActionResult.SUCCESS;
		} else if (!stackOnPlayer.isEmpty() && stackOnPlayer.getItem() instanceof IAvatarWieldable) {
			avatar.getItemHandler().setStack(0, stackOnPlayer.split(1));
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newstate, boolean isMoving) {
		if (state.getBlock() != newstate.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			ItemScatterer.spawn(world, pos, inv.getItemHandler());
			super.onStateReplaced(state, world, pos, newstate, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getPlayerFacing().getOpposite());
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileAvatar();
	}

	@Nonnull
	@Override
	public BlockState mirror(@Nonnull BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Nonnull
	@Override
	public BlockState rotate(@Nonnull BlockState state, BlockRotation rot) {
		return state.with(Properties.HORIZONTAL_FACING, rot.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}
}
