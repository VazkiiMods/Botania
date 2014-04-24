/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 24, 2014, 11:14:57 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.item.ItemStack;
import baubles.api.BaubleType;

public class ItemTravelBelt extends ItemBauble {

	public ItemTravelBelt() {
		super(LibItemNames.TRAVEL_BELT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

}
