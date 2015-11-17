/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:43:16 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;

public class LensWeight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!burst.isFake()) {
			Block block = entity.worldObj.getBlockState(pos.getBlockPos()).getBlock();
			Block blockBelow = entity.worldObj.getBlockState(pos.getBlockPos().down()).getBlock();
			if(blockBelow.isAir(entity.worldObj, pos.getBlockPos().down()) && block.getBlockHardness(entity.worldObj, pos.getBlockPos()) != -1 && entity.worldObj.getTileEntity(pos.getBlockPos()) == null && block.canSilkHarvest(entity.worldObj, pos.getBlockPos(), entity.worldObj.getBlockState(pos.getBlockPos()), null)) {
				EntityFallingBlock falling = new EntityFallingBlock(entity.worldObj, pos.getBlockPos().getX() + 0.5, pos.getBlockPos().getY() + 0.5, pos.getBlockPos().getZ() + 0.5, entity.worldObj.getBlockState(pos.getBlockPos()));
				if(!entity.worldObj.isRemote)
					entity.worldObj.spawnEntityInWorld(falling);
			}
		}

		return dead;
	}

}
