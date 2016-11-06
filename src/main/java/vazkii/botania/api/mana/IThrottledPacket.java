/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 12, 2015, 3:08:09 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that will only send a few packets rather than one per every entity collision.
 * markDispatchable marks that this TE needs to send a packet. Further handling is to be done
 * in the TE itself.
 */
public interface IThrottledPacket {

	public void markDispatchable();

}
