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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.TorchMode;
import vazkii.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

import java.util.function.BiConsumer;

public class AnimatedTorchBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<TorchMode> MODE = BotaniaStateProperties.TORCH_MODE;
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);

	public AnimatedTorchBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(TRIGGERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, MODE, TRIGGERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		this.updateNeighborsInFront(level, pos, state);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);
		if (!movedByPiston && !state.is(newState.getBlock()) || state.getValue(FACING) != newState.getValue(FACING)) {
			this.updateNeighborsInFront(level, pos, state);
		}
	}

	protected void updateNeighborsInFront(final Level level, final BlockPos pos, final BlockState state) {
		Direction direction = state.getValue(FACING);
		BlockPos poweredPos = pos.relative(direction);
		level.neighborChanged(poweredPos, this, pos);
		level.updateNeighborsAtExceptFromFacing(poweredPos, this, direction.getOpposite());
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hit) {
		if (playerIn.isSecondaryUseActive() && worldIn.getBlockEntity(pos) instanceof AnimatedTorchBlockEntity torch) {
			torch.handRotate();
			return InteractionResult.sidedSuccess(worldIn.isClientSide());
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		return state.getSignal(level, pos, side);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		return !state.getValue(TRIGGERED) && state.getValue(FACING) == side.getOpposite() ? 15 : 0;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AnimatedTorchBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return !level.isClientSide() && state.getValue(TRIGGERED)
				? createTickerHelper(type, BotaniaBlockEntities.ANIMATED_TORCH,
						AnimatedTorchBlockEntity::serverRotatingTick)
				: null;
	}

	@Override
	protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
		if (explosion.canTriggerBlocks() && level.getBlockEntity(pos) instanceof AnimatedTorchBlockEntity torch) {
			torch.toggle();
		}

		super.onExplosionHit(state, level, pos, explosion, dropConsumer);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!state.getValue(TRIGGERED)) {
			Direction facing = state.getValue(FACING);
			double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + facing.getStepX() * 0.35;
			double y = pos.getY() + 0.2 + (random.nextDouble() - 0.5) * 0.2 + facing.getStepY() * 0.35;
			double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + facing.getStepZ() * 0.35;
			level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
		}
	}
}
