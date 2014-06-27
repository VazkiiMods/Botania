/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 27, 2014, 2:41:19 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBottledMana extends ItemMod {

	IIcon[] icons;
	
	public ItemBottledMana() {
		setUnlocalizedName(LibItemNames.MANA_BOTTLE);
		setMaxStackSize(1);
		setMaxDamage(6);
	}
	
	public void randomEffect(EntityPlayer player) {
		
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[6];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}
	
	@Override
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}
	
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		randomEffect(par3EntityPlayer);
		par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
		
		if(par1ItemStack.getItemDamage() == 6)
			return new ItemStack(Items.glass_bottle);
		return par1ItemStack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 20;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.drink;
	}
	
}
