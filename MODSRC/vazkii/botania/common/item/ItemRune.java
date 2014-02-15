/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 7, 2014, 9:46:24 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemRune extends ItemMod implements IFlowerComponent {

	Icon[] icons;

	public ItemRune() {
		super(LibItemIDs.idRune);
		setHasSubtypes(true);
		setUnlocalizedName(LibItemNames.RUNE);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[16];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public Icon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}

	@Override
	public boolean canFit(ItemStack stack, IInventory apothecary) {
		return true;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

}
