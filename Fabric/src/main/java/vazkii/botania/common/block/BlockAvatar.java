/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.*;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.common.block.mana.BlockPrism;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockAvatar extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape X_AABB = box(5, 0, 3.5, 11, 17, 12.5);
	private static final VoxelShape Z_AABB = box(3.5, 0, 5, 12.5, 17, 11);

	protected BlockAvatar(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		if (state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_AABB;
		} else {
			return Z_AABB;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TileAvatar avatar = (TileAvatar) world.getBlockEntity(pos);
		ItemStack stackOnAvatar = avatar.getItemHandler().getItem(0);
		ItemStack stackOnPlayer = player.getItemInHand(hand);
		if (!stackOnAvatar.isEmpty()) {
			avatar.getItemHandler().setItem(0, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(stackOnAvatar);
			return InteractionResult.SUCCESS;
		} else if (!stackOnPlayer.isEmpty() && IAvatarWieldable.API.find(stackOnPlayer, Unit.INSTANCE) != null) {
			avatar.getItemHandler().setItem(0, stackOnPlayer.split(1));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newstate, boolean isMoving) {
		if (!state.is(newstate.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newstate, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileAvatar(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.AVATAR, TileAvatar::commonTick);
	}

	@Nonnull
	@Override
	public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Nonnull
	@Override
	public BlockState rotate(@Nonnull BlockState state, Rotation rot) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (world.hasNeighborSignal(pos)) {
			BlockPrism.redstoneParticlesInShape(state, world, pos, rand);
		}
	}
}
