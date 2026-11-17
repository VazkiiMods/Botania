/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.google.common.collect.Iterables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.block_entity.LuminizerBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class LuminizerLauncherBlock extends Block implements SimpleWaterloggedBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public LuminizerLauncherBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED).add(POWERED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		boolean power = level.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(POWERED);

		if (power && !powered) {
			pickUpEntities(level, pos);
			level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_INVISIBLE);
		} else if (!power && powered) {
			level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_INVISIBLE);
		}
	}

	private void pickUpEntities(Level world, BlockPos pos) {
		List<LuminizerBlockEntity> relays = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (world.getBlockEntity(pos.relative(dir)) instanceof LuminizerBlockEntity relay
					&& relay.getNextDestination() != null) {
				relays.add(relay);
			}
		}

		if (!relays.isEmpty()) {
			AABB aabb = new AABB(pos);
			var living = world.getEntitiesOfClass(LivingEntity.class, aabb);
			var items = world.getEntitiesOfClass(ItemEntity.class, aabb);

			for (Entity entity : Iterables.concat(living, items)) {
				LuminizerBlockEntity relay = relays.get(world.random.nextInt(relays.size()));
				relay.mountEntity(entity);
			}
		}
	}

}
