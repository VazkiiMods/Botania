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
 * A TileEntity that implements this will get it's recieveMana call
 * called on both client and server. If this is not implemented
 * the call will only occur on the server.
 */
public interface IClientManaHandler extends IManaReceiver {

}
