/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.internal.ManaNetwork;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

public final class ManaNetworkHandler implements ManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final Map<Level, Set<ManaPool>> manaPools = new WeakHashMap<>();
	private final Map<Level, Set<ManaCollector>> manaCollectors = new WeakHashMap<>();

	public void onNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		switch (type) {
			case COLLECTOR -> {
				if (action == ManaNetworkAction.ADD) {
					add(manaCollectors, thing.getManaReceiverLevel(), (ManaCollector) thing);
				} else {
					remove(manaCollectors, thing.getManaReceiverLevel(), (ManaCollector) thing);
				}
			}
			case POOL -> {
				if (action == ManaNetworkAction.ADD) {
					add(manaPools, thing.getManaReceiverLevel(), (ManaPool) thing);
				} else {
					remove(manaPools, thing.getManaReceiverLevel(), (ManaPool) thing);
				}
			}
		}
	}

	@Override
	public void clear() {
		manaPools.clear();
		manaCollectors.clear();
	}

	@Override
	public ManaPool getClosestPool(BlockPos pos, Level world, int limit) {
		if (manaPools.containsKey(world)) {
			return getClosest(manaPools.get(world), pos, limit);
		}
		return null;
	}

	@Override
	public ManaCollector getClosestCollector(BlockPos pos, Level world, int limit) {
		if (manaCollectors.containsKey(world)) {
			return getClosest(manaCollectors.get(world), pos, limit);
		}
		return null;
	}

	public boolean isCollectorIn(Level level, ManaCollector collector) {
		return manaCollectors.getOrDefault(level, Collections.emptySet()).contains(collector);
	}

	public boolean isPoolIn(Level level, ManaPool pool) {
		return manaPools.getOrDefault(level, Collections.emptySet()).contains(pool);
	}

	@Nullable
	private <T extends ManaReceiver> T getClosest(Set<T> receivers, BlockPos pos, int limit) {
		long minDist = Long.MAX_VALUE;
		long limitSquared = (long) limit * limit;
		T closest = null;

		for (var receiver : receivers) {
			long distance = MathHelper.distSqr(receiver.getManaReceiverPos(), pos);
			if (distance <= limitSquared && distance < minDist) {
				minDist = distance;
				closest = receiver;
			}
		}

		return closest;
	}

	private <T> void remove(Map<Level, Set<T>> map, Level level, T thing) {
		if (!map.containsKey(level)) {
			return;
		}

		var set = map.get(level);
		set.remove(thing);
		if (set.isEmpty()) {
			map.remove(level);
		}
	}

	private <T> void add(Map<Level, Set<T>> map, Level level, T thing) {
		map.computeIfAbsent(level, k -> new HashSet<>()).add(thing);
	}

	@Override
	public Set<ManaCollector> getAllCollectorsInWorld(Level world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public Set<ManaPool> getAllPoolsInWorld(Level world) {
		return getAllInWorld(manaPools, world);
	}

	@Override
	public void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		XplatAbstractions.INSTANCE.fireManaNetworkEvent(thing, type, action);
	}

	private <T> Set<T> getAllInWorld(Map<Level, Set<T>> map, Level world) {
		Set<T> ret = map.get(world);
		if (ret == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(ret);
		}
	}

}
