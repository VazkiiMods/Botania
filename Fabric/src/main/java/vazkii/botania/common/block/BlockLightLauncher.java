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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class BlockLightLauncher extends BlockModWaterloggable {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);

	public BlockLightLauncher(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			pickUpEntities(world, pos);
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 4);
		}
	}

	private void pickUpEntities(Level world, BlockPos pos) {
		List<TileLightRelay> relays = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			BlockEntity tile = world.getBlockEntity(pos.relative(dir));
			if (tile instanceof TileLightRelay relay) {
				if (relay.getNextDestination() != null) {
					relays.add(relay);
				}
			}
		}

		if (!relays.isEmpty()) {
			AABB aabb = new AABB(pos, pos.offset(1, 1, 1));
			var living = world.getEntitiesOfClass(LivingEntity.class, aabb);
			var items = world.getEntitiesOfClass(ItemEntity.class, aabb);

			for (Entity entity : Iterables.concat(living, items)) {
				TileLightRelay relay = relays.get(world.random.nextInt(relays.size()));
				relay.mountEntity(entity);
			}
		}
	}

}
