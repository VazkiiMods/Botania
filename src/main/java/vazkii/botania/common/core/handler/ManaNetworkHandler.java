/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.ManaNetworkEvent.Action;
import vazkii.botania.api.mana.ManaNetworkEvent.ManaBlockType;

import javax.annotation.Nullable;

import java.util.*;
import java.util.function.BinaryOperator;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final WeakHashMap<World, Set<TileEntity>> manaPools = new WeakHashMap<>();
	private final WeakHashMap<World, Set<TileEntity>> manaCollectors = new WeakHashMap<>();

	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<World, Set<TileEntity>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
		if (event.action == Action.ADD) {
			add(map, event.tile);
		} else {
			remove(map, event.tile);
		}
	}

	@Override
	public void clear() {
		manaPools.clear();
		manaCollectors.clear();
	}

	@Override
	public TileEntity getClosestPool(BlockPos pos, World world, int limit) {
		if (manaPools.containsKey(world)) {
			return getClosest(manaPools.get(world), pos, limit);
		}
		return null;
	}

	@Override
	public TileEntity getClosestCollector(BlockPos pos, World world, int limit) {
		if (manaCollectors.containsKey(world)) {
			return getClosest(manaCollectors.get(world), pos, limit);
		}
		return null;
	}

	public boolean isCollectorIn(TileEntity tile) {
		return isIn(tile, manaCollectors);
	}

	public boolean isPoolIn(TileEntity tile) {
		return isIn(tile, manaPools);
	}

	private boolean isIn(TileEntity tile, Map<World, Set<TileEntity>> map) {
		Set<TileEntity> set = map.get(tile.getWorld());
		return set != null && set.contains(tile);
	}

	@Nullable
	private TileEntity getClosest(Set<TileEntity> tiles, BlockPos pos, int limit) {
		double minDist = Double.MAX_VALUE;
		TileEntity closest = null;

		for (TileEntity te : tiles) {
			if (!te.isRemoved()) {
				double distance = te.getPos().distanceSq(pos);
				if (distance <= limit * limit && distance < minDist) {
					minDist = distance;
					closest = te;
				}
			}
		}

		return closest;
	}

	private void remove(Map<World, Set<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorld();

		if (!map.containsKey(world)) {
			return;
		}

		map.get(world).remove(tile);
	}

	private void add(Map<World, Set<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorld();
		map.computeIfAbsent(world, k -> new HashSet<>()).add(tile);
	}

	@Override
	public Set<TileEntity> getAllCollectorsInWorld(World world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public Set<TileEntity> getAllPoolsInWorld(World world) {
		return getAllInWorld(manaPools, world);
	}

	private Set<TileEntity> getAllInWorld(Map<World, Set<TileEntity>> map, World world) {
		Set<TileEntity> ret = map.get(world);
		if (ret == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(ret);
		}
	}

}
