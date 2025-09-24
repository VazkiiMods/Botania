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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.common.block.block_entity.AvatarBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.xplat.XplatAbstractions;

public class AvatarBlock extends BotaniaWaterloggedBlock implements EntityBlock, RedstoneSensitiveBlock {

	private static final VoxelShape X_AABB = box(5, 0, 3.5, 11, 17, 12.5);
	private static final VoxelShape Z_AABB = box(3.5, 0, 5, 12.5, 17, 11);

	protected AvatarBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
				.setValue(BlockStateProperties.POWERED, false));
	}

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
		builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.POWERED);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stackOnPlayer, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (world.getBlockEntity(pos) instanceof AvatarBlockEntity avatar) {
			ItemStack stackOnAvatar = avatar.getItemHandler().getItem(0);
			if (!stackOnAvatar.isEmpty()) {
				avatar.getItemHandler().setItem(0, ItemStack.EMPTY);
				player.getInventory().placeItemBackInInventory(stackOnAvatar);
				return ItemInteractionResult.sidedSuccess(world.isClientSide());
			} else if (!stackOnPlayer.isEmpty() &&
					XplatAbstractions.INSTANCE.findAvatarWieldable(stackOnPlayer) != null) {
				avatar.getItemHandler().setItem(0, stackOnPlayer.split(1));
				return ItemInteractionResult.sidedSuccess(world.isClientSide());
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newstate, boolean isMoving) {
		if (!state.is(newstate.getBlock())) {
			if (world.getBlockEntity(pos) instanceof SimpleInventoryBlockEntity inventory) {
				Containers.dropContents(world, pos, inventory.getItemHandler());
			}
			super.onRemove(state, world, pos, newstate, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		RedstoneSensitiveBlock.updateRedstonePower(state, world, pos);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AvatarBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.AVATAR, AvatarBlockEntity::commonTick);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			RedstoneSensitiveBlock.redstoneParticlesInShape(state, world, pos, rand);
		}
	}
}
