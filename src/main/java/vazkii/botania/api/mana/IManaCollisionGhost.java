/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 10, 2014, 7:49:19 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * Any TileEntity that implements this can be counted as a "ghost" block of
 * sorts, that won't call the collision code for the mana bursts.
 */
public interface IManaCollisionGhost {

	public boolean isGhost();

}
