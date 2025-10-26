/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import net.minecraft.world.level.Level;

import vazkii.botania.api.internal.ManaNetwork;
import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class ManaNetworkHandler implements ManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final Map<Level, Set<ManaCollector>> manaCollectors = new WeakHashMap<>();

	public void onNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		if (!thing.getManaReceiverLevel().isClientSide()) {
			throw new IllegalArgumentException("Mana network events are only supported on the client side");
		}
		if (type == ManaBlockType.COLLECTOR) {
			if (action == ManaNetworkAction.ADD) {
				add(manaCollectors, thing.getManaReceiverLevel(), (ManaCollector) thing);
			} else {
				remove(manaCollectors, thing.getManaReceiverLevel(), (ManaCollector) thing);
			}
		}
	}

	@Override
	public void clear() {
		manaCollectors.clear();
	}

	public boolean isCollectorIn(Level level, ManaCollector collector) {
		return manaCollectors.getOrDefault(level, Collections.emptySet()).contains(collector);
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
		map.computeIfAbsent(level, k -> Collections.newSetFromMap(new WeakHashMap<>())).add(thing);
	}

	@Override
	public Set<ManaCollector> getAllCollectorsInWorld(Level world) {
		return getAllInWorld(manaCollectors, world);
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
