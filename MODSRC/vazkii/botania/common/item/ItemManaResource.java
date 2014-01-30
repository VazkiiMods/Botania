/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 30, 2014, 4:49:16 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemManaResource extends ItemMod {

	final int types = 3;
	Icon[] icons;
	
	public ItemManaResource() {
		super(LibItemIDs.idManaResource);
		setUnlocalizedName(LibItemNames.MANA_RESOURCE);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < types; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[types];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forNameRaw(par1IconRegister, LibItemNames.MANA_RESOURCE_NAMES[i]);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + LibItemNames.MANA_RESOURCE_NAMES[Math.min(types - 1, par1ItemStack.getItemDamage())];
	}
	
	@Override
	public Icon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}
}
