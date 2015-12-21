/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 20, 2014, 12:12:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.lib.LibItemNames;

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

				if((material == Material.lava || material == Material.water) && l == 0) {
					par2World.setBlockToAir(i, j, k);
					
					for(int x = 0; x < 5; x++)
						par2World.spawnParticle("explode", i + Math.random(), j + Math.random(), k + Math.random(), 0, 0, 0);
					
					return par1ItemStack;
				}
			}

			return par1ItemStack;
		}
	}

}
