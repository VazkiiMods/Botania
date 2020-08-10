/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaNetworkCallback.Action;
import vazkii.botania.api.mana.ManaNetworkCallback.ManaBlockType;

import javax.annotation.Nullable;

import java.util.*;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final WeakHashMap<World, Set<BlockEntity>> manaPools = new WeakHashMap<>();
	private final WeakHashMap<World, Set<BlockEntity>> manaCollectors = new WeakHashMap<>();

	public void onNetworkEvent(BlockEntity be, ManaBlockType type, Action action) {
		Map<World, Set<BlockEntity>> map = type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
		if (action == Action.ADD) {
			add(map, be);
		} else {
			remove(map, be);
		}
	}

	@Override
	public void clear() {
		manaPools.clear();
		manaCollectors.clear();
	}

	@Override
	public BlockEntity getClosestPool(BlockPos pos, World world, int limit) {
		if (manaPools.containsKey(world)) {
			return getClosest(manaPools.get(world), pos, limit);
		}
		return null;
	}

	@Override
	public BlockEntity getClosestCollector(BlockPos pos, World world, int limit) {
		if (manaCollectors.containsKey(world)) {
			return getClosest(manaCollectors.get(world), pos, limit);
		}
		return null;
	}

	public boolean isCollectorIn(BlockEntity tile) {
		return isIn(tile, manaCollectors);
	}

	public boolean isPoolIn(BlockEntity tile) {
		return isIn(tile, manaPools);
	}

	private boolean isIn(BlockEntity tile, Map<World, Set<BlockEntity>> map) {
		Set<BlockEntity> set = map.get(tile.getWorld());
		return set != null && set.contains(tile);
	}

	@Nullable
	private BlockEntity getClosest(Set<BlockEntity> tiles, BlockPos pos, int limit) {
		double minDist = Double.MAX_VALUE;
		BlockEntity closest = null;

		for (BlockEntity te : tiles) {
			if (!te.isRemoved()) {
				double distance = te.getPos().getSquaredDistance(pos);
				if (distance <= limit * limit && distance < minDist) {
					minDist = distance;
					closest = te;
				}
			}
		}

		return closest;
	}

	private void remove(Map<World, Set<BlockEntity>> map, BlockEntity tile) {
		World world = tile.getWorld();

		if (!map.containsKey(world)) {
			return;
		}

		map.get(world).remove(tile);
	}

	private void add(Map<World, Set<BlockEntity>> map, BlockEntity tile) {
		World world = tile.getWorld();
		map.computeIfAbsent(world, k -> new HashSet<>()).add(tile);
	}

	@Override
	public Set<BlockEntity> getAllCollectorsInWorld(World world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public Set<BlockEntity> getAllPoolsInWorld(World world) {
		return getAllInWorld(manaPools, world);
	}

	private Set<BlockEntity> getAllInWorld(Map<World, Set<BlockEntity>> map, World world) {
		Set<BlockEntity> ret = map.get(world);
		if (ret == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(ret);
		}
	}

}
