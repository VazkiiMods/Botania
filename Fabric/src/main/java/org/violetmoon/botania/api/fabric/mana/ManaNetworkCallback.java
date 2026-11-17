/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.fabric.mana;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.api.mana.ManaReceiver;

public interface ManaNetworkCallback {
	Event<ManaNetworkCallback> EVENT = EventFactory.createArrayBacked(ManaNetworkCallback.class,
			listeners -> (thing, typ, act) -> {
				for (ManaNetworkCallback listener : listeners) {
					listener.onNetworkChange(thing, typ, act);
				}
			});

	/**
	 * @param thing If {@code type} is {@link ManaBlockType#COLLECTOR}, a {@link ManaCollector}.
	 */
	void onNetworkChange(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action);
}
