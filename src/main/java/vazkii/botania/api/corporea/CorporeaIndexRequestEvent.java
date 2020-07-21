/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * CorporeaIndexRequestEvent is fired when a player attempts to request an item from a corporea index.
 * If the event is cancelled, the request will not be performed.
 */
@Cancelable
public class CorporeaIndexRequestEvent extends Event {
	private final ServerPlayerEntity requester;
	private final ICorporeaRequestMatcher request;
	private final int requestCount;
	private final ICorporeaSpark indexSpark;

	public CorporeaIndexRequestEvent(ServerPlayerEntity requester, ICorporeaRequestMatcher request, int requestCount, ICorporeaSpark indexSpark) {
		this.requester = requester;
		this.request = request;
		this.requestCount = requestCount;
		this.indexSpark = indexSpark;
	}

	public ServerPlayerEntity getRequester() {
		return requester;
	}

	public ICorporeaRequestMatcher getMatcher() {
		return request;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public ICorporeaSpark getIndexSpark() {
		return indexSpark;
	}
}
