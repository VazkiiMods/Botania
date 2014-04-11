/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 10, 2014, 6:46:03 PM (GMT)]
 */
package vazkii.botania.common.item.magic;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

// TODO
public class ItemTimeRod extends ItemMod {

	public ItemTimeRod() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.TIME_ROD);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		for(int i = 0; i < 60; i++)
			par3World.scheduleBlockUpdate(par4, par5, par6, block, i);
		return true;
	}
	
}
