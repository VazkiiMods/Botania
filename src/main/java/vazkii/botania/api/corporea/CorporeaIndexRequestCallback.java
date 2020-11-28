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
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * CorporeaIndexRequestEvent is fired when a player attempts to request an item from a corporea index.
 */
public interface CorporeaIndexRequestCallback {
	Event<CorporeaIndexRequestCallback> EVENT = EventFactory.createArrayBacked(CorporeaIndexRequestCallback.class,
			listeners -> (pl, req, count, spark) -> {
				for (CorporeaIndexRequestCallback listener : listeners) {
					if (listener.onIndexRequest(pl, req, count, spark)) {
						return true;
					}
				}
				return false;
			});

	/**
	 * @return {@code true} to cancel the request
	 */
	boolean onIndexRequest(ServerPlayerEntity requester, ICorporeaRequestMatcher request, int requestCount, ICorporeaSpark indexSpark);
}
