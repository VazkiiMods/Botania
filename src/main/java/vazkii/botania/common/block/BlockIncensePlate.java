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
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;

public class BlockIncensePlate extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape X_SHAPE = box(6, 0, 2, 10, 1, 14);
	private static final VoxelShape Z_SHAPE = box(2, 0, 6, 14, 1, 10);

	protected BlockIncensePlate(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TileIncensePlate plate = (TileIncensePlate) world.getBlockEntity(pos);
		ItemStack plateStack = plate.getItemHandler().getItem(0);
		ItemStack stack = player.getItemInHand(hand);
		boolean did = false;

		if (world.isClientSide) {
			if (state.getValue(BlockStateProperties.WATERLOGGED)
					&& !plateStack.isEmpty()
					&& !plate.burning
					&& !stack.isEmpty()
					&& stack.is(Items.FLINT_AND_STEEL)) {
				plate.spawnSmokeParticles();
			}
			return InteractionResult.SUCCESS;
		}

		if (plateStack.isEmpty() && plate.acceptsItem(stack)) {
			plate.getItemHandler().setItem(0, stack.copy());
			stack.shrink(1);
			did = true;
		} else if (!plateStack.isEmpty() && !plate.burning) {
			if (!stack.isEmpty() && stack.is(Items.FLINT_AND_STEEL)) {
				plate.ignite();
				stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
				did = true;
			} else {
				player.getInventory().placeItemBackInInventory(plateStack);
				plate.getItemHandler().setItem(0, ItemStack.EMPTY);

				did = true;
			}
		}

		if (did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
		}

		return did ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((TileIncensePlate) world.getBlockEntity(pos)).comparatorOutput;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		if (state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileIncensePlate(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.INCENSE_PLATE, TileIncensePlate::commonTick);
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity plate = world.getBlockEntity(pos);
			if (plate instanceof TileIncensePlate && !((TileIncensePlate) plate).burning) {
				Containers.dropContents(world, pos, ((TileIncensePlate) plate).getItemHandler());
			}
		}
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public void onProjectileHit(@Nonnull Level level, @Nonnull BlockState blockState,
			@Nonnull BlockHitResult hit, @Nonnull Projectile projectile) {
		if (!level.isClientSide && projectile.mayInteract(level, hit.getBlockPos())
				&& projectile.isOnFire()) {
			if (level.getBlockEntity(hit.getBlockPos()) instanceof TileIncensePlate plate) {
				plate.ignite();
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
			}
		}
	}
}
