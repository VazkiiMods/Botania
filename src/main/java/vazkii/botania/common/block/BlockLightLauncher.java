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
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class BlockLightLauncher extends BlockModWaterloggable {

	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockLightLauncher(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (power && !powered) {
			pickUpEntities(world, pos);
			world.setBlockState(pos, state.with(Properties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(Properties.POWERED, false), 4);
		}
	}

	private void pickUpEntities(World world, BlockPos pos) {
		List<TileLightRelay> relays = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			BlockEntity tile = world.getBlockEntity(pos.offset(dir));
			if (tile instanceof TileLightRelay) {
				TileLightRelay relay = (TileLightRelay) tile;
				if (relay.getNextDestination() != null) {
					relays.add(relay);
				}
			}
		}

		if (!relays.isEmpty()) {
			Box aabb = new Box(pos, pos.add(1, 1, 1));
			List<Entity> entities = world.getNonSpectatingEntities(LivingEntity.class, aabb);
			entities.addAll(world.getNonSpectatingEntities(ItemEntity.class, aabb));

			if (!entities.isEmpty()) {
				for (Entity entity : entities) {
					TileLightRelay relay = relays.get(world.random.nextInt(relays.size()));
					relay.mountEntity(entity);
				}
			}
		}
	}

}
