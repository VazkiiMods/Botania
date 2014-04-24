/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 24, 2014, 5:20:54 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGreaterManaRing extends ItemManaRing {

	private static final int MAX_MANA = ItemManaRing.MAX_MANA * 4;
	
	public ItemGreaterManaRing() {
		super(LibItemNames.MANA_RING_GREATER);
	}
	
	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA;
	}
	
}
