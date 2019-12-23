/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 30, 2015, 2:55:57 PM (GMT)]
 */
package vazkii.botania.api.corporea;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired when a corporea request is initiated. Can be cancelled.
 */
@Cancelable
public class CorporeaRequestEvent extends Event {

	public final CorporeaRequestMatcher request;
	public final int count;
	public final ICorporeaSpark spark;
	/**
	 * If false then items won't be pulled.
	 */
	public final boolean realRequest;

	public CorporeaRequestEvent(CorporeaRequestMatcher request, int count, ICorporeaSpark spark, boolean real) {
		this.request = request;
		this.count = count;
		this.spark = spark;
		realRequest = real;
	}


}
