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

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockGhostRail extends AbstractRailBlock {

	private static final String TAG_FLOAT_TICKS = "botania:float_ticks";

	public BlockGhostRail(Properties builder) {
		super(true, builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
	}

	private void updateFloating(AbstractMinecartEntity cart) {
		cart.world.getProfiler().startSection("cartFloating");
		int floatTicks = cart.getPersistentData().getInt(TAG_FLOAT_TICKS);
		Preconditions.checkState(floatTicks > 0);

		BlockPos entPos = cart.getPosition();
		BlockState state = cart.world.getBlockState(entPos);
		boolean air = state.isAir(cart.world, entPos);

		if (state.getBlock() == ModBlocks.dreamwood
				|| (state.getBlock() != ModBlocks.ghostRail && state.isIn(BlockTags.RAILS))) {
			cart.world.playEvent(2003, entPos, 0);
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, 0);
		} else {
			BlockPos down = entPos.down();
			BlockState stateBelow = cart.world.getBlockState(down);
			boolean airBelow = stateBelow.isAir(cart.world, down);
			if (air && airBelow || !air && !airBelow) {
				cart.noClip = true;
			}
			cart.setMotion(cart.getMotion().getX() * 1.4, 0.2, cart.getMotion().getZ() * 1.4);
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, floatTicks - 1);
			cart.world.playEvent(2000, entPos, 0);
		}

		cart.world.getProfiler().endSection();
	}

	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
		if (!world.isRemote) {
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, 20);
			updateFloating(cart);
		}
	}

	public void tickCart(AbstractMinecartEntity c) {
		if (c.world.isRemote) {
			return;
		}

		if (!c.isAlive() || c.getPersistentData().getInt(TAG_FLOAT_TICKS) <= 0) {
			c.noClip = false;
			return;
		}

		updateFloating(c);

		if (c.getPersistentData().getInt(TAG_FLOAT_TICKS) <= 0) {
			c.noClip = false;
		}
	}

	@Nonnull
	@Override
	public Property<RailShape> getShapeProperty() {
		return BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	}
}
