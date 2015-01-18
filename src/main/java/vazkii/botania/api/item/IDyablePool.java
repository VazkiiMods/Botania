/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 18, 2015, 12:20:44 AM (GMT)]
 */
package vazkii.botania.api.item;

import vazkii.botania.api.mana.IManaPool;

/**
 * Used to define a Mana Pool that can be dyed through floral powder.
 */
public interface IDyablePool {

	public int getColor();
	
	public void setColor(int color);
	
	
}
