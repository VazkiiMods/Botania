/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 19, 2014, 10:46:14 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLaputaShard extends ItemMod {

	public ItemLaputaShard() {
		setUnlocalizedName(LibItemNames.LAPUTA_SHARD);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		int range = 14;
		
		int islandHeight = 64;
		int blocks = 0;

		if(!par3World.isRemote) {
			for(int i = 0; i < range * 2 + 1; i++)
				for(int j = 0; j < range * 2 + 1; j++)
					for(int k = 0; k < range * 2 + 1; k++) {
						int x = par4 - range + i;
						int y = par5 - range + j;
						int z = par6 - range + k;
						
						if(MathHelper.pointDistanceSpace(x, y, z, par4, par5, par6) < range) {
							Block block = par3World.getBlock(x, y, z);
							if(!block.isAir(par3World, x, y, z)) {
								int meta = par3World.getBlockMetadata(x, y, z);
								TileEntity tile = par3World.getTileEntity(x, y, z);
								
								par3World.setBlockToAir(x, y, z);
								par3World.setBlock(x, y + islandHeight, z, block, meta, 1 | 2);
								par3World.setTileEntity(x, y + islandHeight, z, tile);
								blocks++;
							}
						}
					}
			System.out.println("Blocks Moved: " + blocks);
		}

		return true;
	}

}
