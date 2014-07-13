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

import java.awt.Color;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibItemNames;

public class ItemManaResource extends ItemMod implements IFlowerComponent, IElvenItem {

	final int types = 11;
	IIcon[] icons;

	public ItemManaResource() {
		super();
		setUnlocalizedName(LibItemNames.MANA_RESOURCE);
		setHasSubtypes(true);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(par1ItemStack.getItemDamage() == 4)
			return EntityDoppleganger.spawn(par1ItemStack, par3World, par4, par5, par6);

		return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < types; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[types];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forName(par1IconRegister, LibItemNames.MANA_RESOURCE_NAMES[i]);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par1ItemStack.getItemDamage() == 5)
			return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 0.25F, 1F);
		
		return 0xFFFFFF;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + LibItemNames.MANA_RESOURCE_NAMES[Math.min(types - 1, par1ItemStack.getItemDamage())];
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public boolean canFit(ItemStack stack, IInventory apothecary) {
		int meta = stack.getItemDamage();
		return meta == 6 || meta == 8;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0x9b0000;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		int meta = stack.getItemDamage();
		return meta == 7 || meta == 8 || meta == 9;
	}
}
