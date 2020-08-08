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
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import java.util.*;

public class BlockGhostRail extends AbstractRailBlock {

	private static final String TAG_FLOAT_TICKS = "botania:float_ticks";

	public BlockGhostRail(Settings builder) {
		super(true, builder);
		setDefaultState(getDefaultState().with(Properties.STRAIGHT_RAIL_SHAPE, RailShape.NORTH_SOUTH));
		MinecraftForge.EVENT_BUS.addListener(this::cartSpawn);
		MinecraftForge.EVENT_BUS.addListener(this::worldTick);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.STRAIGHT_RAIL_SHAPE);
	}

	private void updateFloating(AbstractMinecartEntity cart) {
		cart.world.getProfiler().push("cartFloating");
		int floatTicks = cart.getPersistentData().getInt(TAG_FLOAT_TICKS);
		Preconditions.checkState(floatTicks > 0);

		BlockPos entPos = cart.getBlockPos();
		BlockState state = cart.world.getBlockState(entPos);
		boolean air = state.isAir();

		if (state.getBlock() == ModBlocks.dreamwood
				|| (state.getBlock() != ModBlocks.ghostRail && state.isIn(BlockTags.RAILS))) {
			cart.world.syncWorldEvent(2003, entPos, 0);
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, 0);
		} else {
			BlockPos down = entPos.down();
			BlockState stateBelow = cart.world.getBlockState(down);
			boolean airBelow = stateBelow.isAir();
			if (air && airBelow || !air && !airBelow) {
				cart.noClip = true;
			}
			cart.setVelocity(cart.getVelocity().getX() * 1.4, 0.2, cart.getVelocity().getZ() * 1.4);
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, floatTicks - 1);
			cart.world.syncWorldEvent(2000, entPos, 0);
		}

		cart.world.getProfiler().pop();
	}

	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
		if (!world.isClient) {
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, 20);
			addFloatingCart(cart);
			updateFloating(cart);
		}
	}

	private final Map<RegistryKey<World>, Set<AbstractMinecartEntity>> floatingCarts = new HashMap<>();

	private void addFloatingCart(AbstractMinecartEntity cart) {
		if (cart.isAlive() && cart.getPersistentData().getInt(TAG_FLOAT_TICKS) > 0) {
			floatingCarts.computeIfAbsent(cart.world.getRegistryKey(), t -> Collections.newSetFromMap(new WeakHashMap<>()))
					.add(cart);
		}
	}

	private void cartSpawn(EntityJoinWorldEvent evt) {
		if (!evt.getWorld().isClient && evt.getEntity() instanceof AbstractMinecartEntity) {
			addFloatingCart((AbstractMinecartEntity) evt.getEntity());
		}
	}

	private void worldTick(TickEvent.WorldTickEvent evt) {
		if (!evt.world.isClient() && evt.phase == TickEvent.Phase.END) {
			evt.world.getProfiler().push("cartFloatingIter");
			Iterator<AbstractMinecartEntity> iter = floatingCarts.getOrDefault(evt.world.getRegistryKey(), Collections.emptySet()).iterator();
			while (iter.hasNext()) {
				AbstractMinecartEntity c = iter.next();
				BlockPos entPos = c.getBlockPos();

				if (!c.isAlive() || !c.isAddedToWorld() || !c.world.isChunkLoaded(entPos)
						|| c.getPersistentData().getInt(TAG_FLOAT_TICKS) <= 0) {
					c.noClip = false;
					iter.remove();
					continue;
				}

				updateFloating(c);

				if (c.getPersistentData().getInt(TAG_FLOAT_TICKS) <= 0) {
					c.noClip = false;
					iter.remove();
				}
			}
			evt.world.getProfiler().pop();
		}
	}

	@Nonnull
	@Override
	public Property<RailShape> getShapeProperty() {
		return Properties.STRAIGHT_RAIL_SHAPE;
	}
}
