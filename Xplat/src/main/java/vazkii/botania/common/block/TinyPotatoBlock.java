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
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.TinyPotatoBlockEntity;

public class TinyPotatoBlock extends BotaniaWaterloggedBlock implements EntityBlock {

	private static final VoxelShape SHAPE = box(6, 0, 6, 10, 6, 10);

	public TinyPotatoBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof TinyPotatoBlockEntity tater
				? AbstractContainerMenu.getRedstoneSignalFromContainer(tater)
				: 0;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	/*
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TinyPotatoBlockEntity tater) {
			tater.interact(player, hand, player.getItemInHand(hand), hit.getDirection());
			if (!world.isClientSide) {
				spawnHearts((ServerLevel) world, pos);
			}
		}
		return InteractionResult.sidedSuccess(world.isClientSide());
	}
	 */

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TinyPotatoBlockEntity tater) {
			tater.interact(player, hand, player.getItemInHand(hand), hit.getDirection());
			if (!world.isClientSide) {
				spawnHearts((ServerLevel) world, pos);
			}
		}
		return ItemInteractionResult.sidedSuccess(world.isClientSide());
	}

	public static void spawnHearts(ServerLevel level, BlockPos pos) {
		AABB box = SHAPE.bounds();
		level.sendParticles(ParticleTypes.HEART, pos.getX() + box.minX + Math.random() * (box.maxX - box.minX), pos.getY() + box.maxY, pos.getZ() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0, 0, 0, 0);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
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
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
		if (stack.has(DataComponents.CUSTOM_NAME) && world.getBlockEntity(pos) instanceof TinyPotatoBlockEntity tater) {
			tater.name = stack.getHoverName();
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TinyPotatoBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.TINY_POTATO, TinyPotatoBlockEntity::commonTick);
	}
}
