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

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when a corporea request is initiated. Can be cancelled.
 */
@Cancelable
public class CorporeaRequestEvent extends Event {

	public final Object request;
	public final int count;
	public final ICorporeaSpark spark;
	public final boolean checkNBT;
	/**
	 * If false then items won't be pulled.
	 */
	public final boolean realRequest;

	public CorporeaRequestEvent(Object request, int count, ICorporeaSpark spark, boolean nbt, boolean real) {
		this.request = request;
		this.count = count;
		this.spark = spark;
		checkNBT = nbt;
		realRequest = real;
	}


}
