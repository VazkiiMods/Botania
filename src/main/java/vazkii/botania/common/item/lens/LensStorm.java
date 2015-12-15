/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 2:10:26 AM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;

public class LensStorm extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!burst.isFake()) {
			ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
			if(!entity.worldObj.isRemote && pos.entityHit == null && !isManaBlock && (pos.blockX != coords.posX || pos.blockY != coords.posY || pos.blockZ != coords.posZ))
				entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 5F, true);
		} else dead = false;

		return dead;
	}

}
