/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 7, 2014, 7:06:20 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemManaTablet extends ItemMod implements IManaItem {

	Icon[] icons;
	
	private static final String TAG_MANA = "mana";
	
	public ItemManaTablet() {
		super(LibItemIDs.idManaTablet);
		setMaxStackSize(1);
		setMaxDamage(1000);
		setUnlocalizedName(LibItemNames.MANA_TABLET);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		float mana = getMana(par1ItemStack);
		return par2 == 1 ? Color.HSBtoRGB(0.528F,  mana / (float) getMaxMana(par1ItemStack), 1F) : 0xFFFFFF;
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		float mana = getMana(stack);
		return 1000 - (int) ((mana / (float) getMaxMana(stack)) * 1000);
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack) {
		return getDamage(stack);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return icons[Math.min(1, pass)];
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return 500000;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return true;
	}

	@Override
	public boolean isPriorityItem(ItemStack stack) {
		return false;
	}
	
}
