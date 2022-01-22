/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired when a corporea request is initiated. Can be cancelled.
 */
public interface CorporeaRequestCallback {
	Event<CorporeaRequestCallback> EVENT = EventFactory.createArrayBacked(CorporeaRequestCallback.class,
			listeners -> (m, count, spark, dry) -> {
				for (CorporeaRequestCallback listener : listeners) {
					if (listener.onRequest(m, count, spark, dry)) {
						return true;
					}
				}
				return false;

			});

	/**
	 * @return {@code true} to cancel the request
	 */
	boolean onRequest(ICorporeaRequestMatcher matcher, int count, ICorporeaSpark spark, boolean dryRun);
}
