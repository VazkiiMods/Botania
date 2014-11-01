/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 5:45:50 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemVial extends ItemMod implements IBrewContainer {

	public static IIcon flaskIcon, vialIcon;
	
	public ItemVial() {
		setHasSubtypes(true);
		setUnlocalizedName(LibItemNames.VIAL);
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		vialIcon = IconHelper.forName(par1IconRegister, "vial0");
		flaskIcon = IconHelper.forName(par1IconRegister, "flask0");
	}

	@Override
	public IIcon getIconFromDamage(int i) {
		return i == 0 ? vialIcon : flaskIcon;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		return null;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * (stack.getItemDamage() + 1);
	}
	
}
