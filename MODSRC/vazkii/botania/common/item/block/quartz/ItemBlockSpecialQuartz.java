/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 11, 2014, 1:13:36 AM (GMT)]
 */
package vazkii.botania.common.item.block.quartz;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.quartz.BlockSpecialQuartz;

public class ItemBlockSpecialQuartz extends ItemMultiTexture {

	public ItemBlockSpecialQuartz(Block par1) {
		super(par1, par1, new String[]{ "" });
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return par1ItemStack.getItemDamage() >= 3 ? "" : ((BlockSpecialQuartz) field_150939_a).getNames()[par1ItemStack.getItemDamage()];
	}
}