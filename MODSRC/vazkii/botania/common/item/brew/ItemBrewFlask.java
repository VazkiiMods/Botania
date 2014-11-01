/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 7:34:30 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.item.ItemStack;

public class ItemBrewFlask extends ItemBrewBase {

	public ItemBrewFlask() {
		super(LibItemNames.BREW_FLASK, LibItemNames.FLASK, 6, 24, new ItemStack(ModItems.vial, 1, 1));
	}

}
