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

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.Vector3;

public class LensBounce extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!isManaBlock && pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			BlockRayTraceResult rtr = (BlockRayTraceResult) pos;
			if (!coords.equals(rtr.getPos())) {
				Vector3 currentMovementVec = new Vector3(entity.getMotion());
				Direction dir = rtr.getFace();
				Vector3 normalVector = new Vector3(dir.getXOffset(), dir.getYOffset(), dir.getZOffset()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				burst.setBurstMotion(movementVec.x, movementVec.y, movementVec.z);
				dead = false;
			}
		}

		return dead;
	}

}
