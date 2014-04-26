/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 26, 2014, 2:06:17 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import baubles.api.BaubleType;
import vazkii.botania.common.lib.LibItemNames;

public class ItemIcePendant extends ItemBauble {

	public ItemIcePendant() {
		super(LibItemNames.ICE_PENDANT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
	}

}
