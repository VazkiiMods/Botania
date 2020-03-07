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
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockGhostRail extends AbstractRailBlock {

	private static final String TAG_FLOAT_TICKS = "botania:float_ticks";

	public BlockGhostRail(Properties builder) {
		super(true, builder);
		setDefaultState(stateContainer.getBaseState().with(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
		MinecraftForge.EVENT_BUS.addListener(this::cartSpawn);
		MinecraftForge.EVENT_BUS.addListener(this::worldTick);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
	}

	private void updateFloating(AbstractMinecartEntity cart) {
		cart.world.getProfiler().startSection("cartFloating");
		int floatTicks = cart.getPersistentData().getInt(TAG_FLOAT_TICKS);
		Preconditions.checkState(floatTicks > 0);

		BlockPos entPos = new BlockPos(cart);
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
			addFloatingCart(cart);
			updateFloating(cart);
		}
	}

	private final Map<DimensionType, Set<AbstractMinecartEntity>> floatingCarts = new HashMap<>();

	private void addFloatingCart(AbstractMinecartEntity cart) {
		if (cart.isAlive() && cart.getPersistentData().getInt(TAG_FLOAT_TICKS) > 0) {
			floatingCarts.computeIfAbsent(cart.world.getDimension().getType(), t -> Collections.newSetFromMap(new WeakHashMap<>()))
					.add(cart);
		}
	}

	private void cartSpawn(EntityJoinWorldEvent evt) {
		if (!evt.getWorld().isRemote && evt.getEntity() instanceof AbstractMinecartEntity) {
			addFloatingCart((AbstractMinecartEntity) evt.getEntity());
		}
	}

	private void worldTick(TickEvent.WorldTickEvent evt) {
		if (!evt.world.isRemote() && evt.phase == TickEvent.Phase.END) {
			evt.world.getProfiler().startSection("cartFloatingIter");
			Iterator<AbstractMinecartEntity> iter = floatingCarts.getOrDefault(evt.world.getDimension().getType(), Collections.emptySet()).iterator();
			while (iter.hasNext()) {
				AbstractMinecartEntity c = iter.next();
				BlockPos entPos = new BlockPos(c);

				if (!c.isAlive() || !c.isAddedToWorld() || !c.world.isBlockLoaded(entPos)) {
					iter.remove();
					continue;
				}

				updateFloating(c);

				if (c.getPersistentData().getInt(TAG_FLOAT_TICKS) <= 0) {
					c.noClip = false;
					iter.remove();
				}
			}
			evt.world.getProfiler().endSection();
		}
	}

	@Nonnull
	@Override
	public IProperty<RailShape> getShapeProperty() {
		return BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	}
}
