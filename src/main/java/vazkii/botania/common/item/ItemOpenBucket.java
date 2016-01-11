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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
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
				BlockPos pos = movingobjectposition.getBlockPos();

				if(!par2World.isBlockModifiable(par3EntityPlayer, pos))
					return par1ItemStack;

				if(!par3EntityPlayer.canPlayerEdit(pos, movingobjectposition.sideHit, par1ItemStack))
					return par1ItemStack;

				Material material = par2World.getBlockState(pos).getBlock().getMaterial();
				int l = par2World.getBlockState(pos).getBlock().getMetaFromState(par2World.getBlockState(pos)); // hack to get meta so we don't have to know the level prop

				if((material == Material.lava || material == Material.water) && l == 0) {
					par2World.setBlockToAir(pos);
					
					for(int x = 0; x < 5; x++)
						par2World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);

					return par1ItemStack;
				}
			}

			return par1ItemStack;
		}
	}

}
