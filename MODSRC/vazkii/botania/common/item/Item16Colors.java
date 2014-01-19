/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 4:09:57 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;
import java.util.List;

import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;

public class Item16Colors extends ItemMod {

	public Item16Colors(int par1, String name) {
		super(par1);
		setHasSubtypes(true);
		setUnlocalizedName(name);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		float[] color = EntitySheep.fleeceColorTable[par1ItemStack.getItemDamage()];
		return new Color(color[0], color[1], color[2]).getRGB();
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}
	
}
