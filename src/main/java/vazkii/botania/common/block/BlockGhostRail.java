/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.GhostRailComponent;

import javax.annotation.Nonnull;

public class BlockGhostRail extends BaseRailBlock {

	public static final String TAG_FLOAT_TICKS = "botania:float_ticks";

	public BlockGhostRail(Properties builder) {
		super(true, builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
	}

	private void updateFloating(AbstractMinecart cart) {
		cart.level.getProfiler().push("cartFloating");
		GhostRailComponent persistentData = EntityComponents.GHOST_RAIL.get(cart);
		int floatTicks = persistentData.floatTicks;
		Preconditions.checkState(floatTicks > 0);

		BlockPos entPos = cart.blockPosition();
		BlockState state = cart.level.getBlockState(entPos);
		boolean air = state.isAir();

		if (state.getBlock() == ModBlocks.dreamwood
				|| (state.getBlock() != ModBlocks.ghostRail && state.is(BlockTags.RAILS))) {
			cart.level.levelEvent(2003, entPos, 0);
			persistentData.floatTicks = 0;
		} else {
			BlockPos down = entPos.below();
			BlockState stateBelow = cart.level.getBlockState(down);
			boolean airBelow = stateBelow.isAir();
			if (air && airBelow || !air && !airBelow) {
				cart.noPhysics = true;
			}
			cart.setDeltaMovement(cart.getDeltaMovement().x() * 1.4, 0.2, cart.getDeltaMovement().z() * 1.4);
			persistentData.floatTicks--;
			cart.level.levelEvent(2000, entPos, 0);
		}

		cart.level.getProfiler().pop();
	}

	public void onMinecartPass(Level world, AbstractMinecart cart) {
		if (!world.isClientSide) {
			EntityComponents.GHOST_RAIL.get(cart).floatTicks = 20;
			updateFloating(cart);
		}
	}

	public void tickCart(AbstractMinecart c) {
		if (c.level.isClientSide) {
			return;
		}

		GhostRailComponent persistentData = EntityComponents.GHOST_RAIL.get(c);
		if (!c.isAlive() || persistentData.floatTicks <= 0) {
			c.noPhysics = false;
			return;
		}

		updateFloating(c);

		if (persistentData.floatTicks <= 0) {
			c.noPhysics = false;
		}
	}

	@Nonnull
	@Override
	public Property<RailShape> getShapeProperty() {
		return BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	}
}
