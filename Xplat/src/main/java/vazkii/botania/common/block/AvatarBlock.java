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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.block_entity.AvatarBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;

public class AvatarBlock extends BotaniaWaterloggedBlock implements EntityBlock, RedstoneSensitiveBlock {

	private static final VoxelShape X_AABB = box(5, 0, 3.5, 11, 17, 12.5);
	private static final VoxelShape Z_AABB = box(3.5, 0, 5, 12.5, 17, 11);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ACTIVE = BotaniaStateProperties.ACTIVE;

	protected AvatarBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(FACING, Direction.NORTH)
				.setValue(POWERED, false)
				.setValue(ACTIVE, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_AABB : Z_AABB;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, POWERED, ACTIVE);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stackOnPlayer, BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof AvatarBlockEntity avatar) {
			ItemStack stackOnAvatar = avatar.getItemHandler().getItem(0);
			if (!stackOnAvatar.isEmpty()) {
				if (!level.isClientSide()) {
					avatar.getItemHandler().setItem(0, ItemStack.EMPTY);
					player.getInventory().placeItemBackInInventory(stackOnAvatar);
					level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());

			} else if (!stackOnPlayer.isEmpty() && AvatarWieldable.LOOKUP.find(stackOnPlayer, avatar) != null) {
				if (!level.isClientSide()) {
					avatar.getItemHandler().setItem(0, stackOnPlayer.split(1));
					level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof SimpleInventoryBlockEntity inventory) {
				Containers.dropContents(level, pos, inventory.getItemHandler());
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context)
				.setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		RedstoneSensitiveBlock.updateRedstonePower(state, level, pos);
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
		return level.isClientSide() || !state.getValue(ACTIVE)
				? null
				: createTickerHelper(type, BotaniaBlockEntities.AVATAR, AvatarBlockEntity::serverTick);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (state.getValue(POWERED)) {
			RedstoneSensitiveBlock.redstoneParticlesInShape(state, level, pos, rand);
		}
	}
}
