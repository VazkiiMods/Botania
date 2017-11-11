/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 19, 2015, 9:52:05 PM (GMT)]
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.internal.IManaBurst;

import java.util.UUID;

/**
 * This describes an interface of a Mana Sender block that should be able to pingbackable
 * by a burst to tell it that the burst is still alive.
 */
public interface IPingable {

	/**
	 * Pings this object back, telling it that the burst passed in is still alive
	 * in the world. The UUID parameter should be the UUID with which the burst
	 * was created, this is used to let the object handle the check for if it's the
	 * correct ID internally. IManaBurst implementations should do this every tick.
	 */
	public void pingback(IManaBurst burst, UUID expectedIdentity);

	/**
	 * @return A unique and persistent identifier for this IPingable
	 */
	public UUID getIdentifier();

}
