/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 20, 2014, 12:12:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event;

public class ItemOpenBucket extends ItemMod {

	public ItemOpenBucket() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.OPEN_BUCKET);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

		if(movingobjectposition == null)
			return par1ItemStack;
		else {
			if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if(!par2World.canMineBlock(par3EntityPlayer, i, j, k))
					return par1ItemStack;

				if(!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					return par1ItemStack;

				Material material = par2World.getBlock(i, j, k).getMaterial();
				int l = par2World.getBlockMetadata(i, j, k);

				if(material == Material.water && l == 0) {
					par2World.setBlockToAir(i, j, k);
					return par1ItemStack;
				}

				if(material == Material.lava && l == 0) {
					par2World.setBlockToAir(i, j, k);
					return par1ItemStack;
				}
			}

			return par1ItemStack;
		}
	}

}
