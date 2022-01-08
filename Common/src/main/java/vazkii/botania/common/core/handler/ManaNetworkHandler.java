/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nullable;

import java.util.*;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final WeakHashMap<Level, Set<BlockEntity>> manaPools = new WeakHashMap<>();
	private final WeakHashMap<Level, Set<BlockEntity>> manaCollectors = new WeakHashMap<>();

	public void onNetworkEvent(BlockEntity be, ManaBlockType type, ManaNetworkAction action) {
		Map<Level, Set<BlockEntity>> map = type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
		if (action == ManaNetworkAction.ADD) {
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
	public BlockEntity getClosestPool(BlockPos pos, Level world, int limit) {
		if (manaPools.containsKey(world)) {
			return getClosest(manaPools.get(world), pos, limit);
		}
		return null;
	}

	@Override
	public BlockEntity getClosestCollector(BlockPos pos, Level world, int limit) {
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

	private boolean isIn(BlockEntity tile, Map<Level, Set<BlockEntity>> map) {
		Set<BlockEntity> set = map.get(tile.getLevel());
		return set != null && set.contains(tile);
	}

	@Nullable
	private BlockEntity getClosest(Set<BlockEntity> tiles, BlockPos pos, int limit) {
		long minDist = Long.MAX_VALUE;
		long limitSquared = (long) limit * limit;
		BlockEntity closest = null;

		for (BlockEntity te : tiles) {
			if (!te.isRemoved()) {
				long distance = MathHelper.distSqr(te.getBlockPos(), pos);
				if (distance <= limitSquared && distance < minDist) {
					minDist = distance;
					closest = te;
				}
			}
		}

		return closest;
	}

	private void remove(Map<Level, Set<BlockEntity>> map, BlockEntity tile) {
		Level world = tile.getLevel();

		if (!map.containsKey(world)) {
			return;
		}

		map.get(world).remove(tile);
	}

	private void add(Map<Level, Set<BlockEntity>> map, BlockEntity tile) {
		Level world = tile.getLevel();
		map.computeIfAbsent(world, k -> new HashSet<>()).add(tile);
	}

	@Override
	public Set<BlockEntity> getAllCollectorsInWorld(Level world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public Set<BlockEntity> getAllPoolsInWorld(Level world) {
		return getAllInWorld(manaPools, world);
	}

	private Set<BlockEntity> getAllInWorld(Map<Level, Set<BlockEntity>> map, Level world) {
		Set<BlockEntity> ret = map.get(world);
		if (ret == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(ret);
		}
	}

}
