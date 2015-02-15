/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 13, 2015, 10:25:32 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCorporeaSpark extends ItemMod {

	public static IIcon invIcon, worldIcon, invIconMaster, worldIconMaster, iconColorStar;

	public ItemCorporeaSpark() {
		setUnlocalizedName(LibItemNames.CORPOREA_SPARK);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xv, float yv, float zv) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof IInventory && !CorporeaHelper.doesBlockHaveSpark(world, x, y, z)) {
			stack.stackSize--;
			if(!world.isRemote) {
				EntityCorporeaSpark spark = new EntityCorporeaSpark(world);
				if(stack.getItemDamage() == 1)
					spark.setMaster(true);
				spark.setPosition(x + 0.5, y + 1.5, z + 0.5);
				world.spawnEntityInWorld(spark);
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		return false;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		invIcon = IconHelper.forItem(par1IconRegister, this, 0);
		worldIcon = IconHelper.forItem(par1IconRegister, this, 1);
		invIconMaster = IconHelper.forItem(par1IconRegister, this, 2);
		worldIconMaster = IconHelper.forItem(par1IconRegister, this, 3);
		iconColorStar = IconHelper.forItem(par1IconRegister, this, "Star");
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return meta == 0 ? invIcon : invIconMaster;
	}

}
