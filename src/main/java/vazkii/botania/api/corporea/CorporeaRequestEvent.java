/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired when a corporea request is initiated. Can be cancelled.
 */
@Cancelable
public class CorporeaRequestEvent extends Event {

	public final ICorporeaRequestMatcher request;
	public final int count;
	public final ICorporeaSpark spark;
	/**
	 * If false then items won't be pulled.
	 */
	public final boolean realRequest;

	public CorporeaRequestEvent(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, boolean real) {
		this.request = request;
		this.count = count;
		this.spark = spark;
		realRequest = real;
	}

}
