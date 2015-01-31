/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 28, 2015, 2:59:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCraftPattern extends ItemMod {

	IIcon[] icons;

	public ItemCraftPattern() {
		setHasSubtypes(true);
		setUnlocalizedName(LibItemNames.CRAFT_PATTERN);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int s, float xs, float ys, float zs) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileCraftCrate && !world.isRemote) {
			TileCraftCrate crate = (TileCraftCrate) tile;
			crate.pattern = stack.getItemDamage();
			world.markBlockForUpdate(x, y, z);
		}
		return false;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < TileCraftCrate.PATTERNS.length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[TileCraftCrate.PATTERNS.length];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIconFromDamage(int dmg) {
		return icons[Math.min(icons.length - 1, dmg)];
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

}
