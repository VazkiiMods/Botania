/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 2, 2014, 5:26:02 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that implements this will get it's recieveMana call
 * called on both client and server. If this is not implemented
 * the call will only occur on the server.
 */
public interface IClientManaHandler extends IManaReceiver {

}
