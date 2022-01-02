/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
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

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileTinyPotato;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTinyPotato extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(6, 0, 6, 10, 6, 10);

	public BlockTinyPotato(Properties builder) {
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
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact(player, hand, player.getItemInHand(hand), hit.getDirection());
			if (!world.isClientSide) {
				AABB box = SHAPE.bounds();
				((ServerLevel) world).sendParticles(ParticleTypes.HEART, pos.getX() + box.minX + Math.random() * (box.maxX - box.minX), pos.getY() + box.maxY, pos.getZ() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0, 0, 0, 0);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			((TileTinyPotato) world.getBlockEntity(pos)).name = stack.getHoverName();
		}
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileTinyPotato(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.TINY_POTATO, TileTinyPotato::commonTick);
	}
}
