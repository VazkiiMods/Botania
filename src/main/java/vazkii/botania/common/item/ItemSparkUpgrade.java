/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 5:32:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSparkUpgrade extends ItemMod {

	private static final int VARIANTS = 4;

	public static IIcon[] worldIcons;
	IIcon[] invIcons;

	public ItemSparkUpgrade() {
		setUnlocalizedName(LibItemNames.SPARK_UPGRADE);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		worldIcons = new IIcon[VARIANTS];
		invIcons = new IIcon[VARIANTS];
		for(int i = 0; i < VARIANTS; i++) {
			worldIcons[i] = IconHelper.forItem(par1IconRegister, this, "L" + i);
			invIcons[i] = IconHelper.forItem(par1IconRegister, this, i);
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return invIcons[Math.min(invIcons.length - 1, meta)];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < VARIANTS; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}
}
