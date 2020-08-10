/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.BlockEntity;

public interface ManaNetworkCallback {
	Event<ManaNetworkCallback> EVENT = EventFactory.createArrayBacked(ManaNetworkCallback.class,
		listeners -> (be, typ, act) -> {
			for (ManaNetworkCallback listener : listeners) {
				listener.onNetworkChange(be, typ, act);
			}
		});

	void onNetworkChange(BlockEntity be, ManaBlockType type, Action action);

	public static void addCollector(BlockEntity tile) {
		EVENT.invoker().onNetworkChange(tile, ManaBlockType.COLLECTOR, Action.ADD);
	}

	public static void removeCollector(BlockEntity tile) {
		EVENT.invoker().onNetworkChange(tile, ManaBlockType.COLLECTOR, Action.REMOVE);
	}

	public static void addPool(BlockEntity tile) {
		EVENT.invoker().onNetworkChange(tile, ManaBlockType.POOL, Action.ADD);
	}

	public static void removePool(BlockEntity tile) {
		EVENT.invoker().onNetworkChange(tile, ManaBlockType.POOL, Action.REMOVE);
	}

	public enum ManaBlockType {
		POOL, COLLECTOR
	}

	public enum Action {
		REMOVE, ADD
	}
}
