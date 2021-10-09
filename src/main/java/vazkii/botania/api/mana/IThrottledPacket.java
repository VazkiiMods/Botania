/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that will only send a few packets rather than one per every entity collision.
 * markDispatchable marks that this block entity needs to send a packet. Further handling is to be done
 * in the block entity itself.
 */
public interface IThrottledPacket {

	void markDispatchable();

}
