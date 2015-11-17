/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:34:29 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.Vector3;

public class LensBounce extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!isManaBlock && pos.entityHit == null) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if(!coords.equals(pos.getBlockPos())) {
				Vector3 currentMovementVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);
				EnumFacing dir = pos.sideHit;
				Vector3 normalVector = new Vector3(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				burst.setMotion(movementVec.x, movementVec.y, movementVec.z);
				dead = false;
			}
		}

		return dead;
	}

}
