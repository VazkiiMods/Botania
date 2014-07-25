/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 4:59:05 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that implements this is considered a Mana Block.
 * Just being a Mana Block doesn't mean much, look at the other IMana
 * interfaces.
 */
public interface IManaBlock {

	/**
	 * Gets the amount of mana currently in this block.
	 */
	public int getCurrentMana();

}
