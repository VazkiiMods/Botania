/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 4:10:47 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDye extends Item16Colors {

	public ItemDye() {
		super(LibItemNames.DYE);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		int meta = par1ItemStack.getItemDamage();
		if(meta != par3World.getBlockMetadata(par4, par5, par6) && (block == Blocks.wool || block == Blocks.carpet)) {
			par3World.setBlockMetadataWithNotify(par4, par5, par6, meta, 1 | 2);
			par1ItemStack.stackSize--;
			return true;
		}
		return false;
	}

}
