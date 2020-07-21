/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.Vector3;

public class LensBounce extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (!isManaBlock && pos.getType() == HitResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			BlockHitResult rtr = (BlockHitResult) pos;
			if (!coords.equals(rtr.getBlockPos())) {
				Vector3 currentMovementVec = new Vector3(entity.getVelocity());
				Direction dir = rtr.getSide();
				Vector3 normalVector = new Vector3(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				burst.setBurstMotion(movementVec.x, movementVec.y, movementVec.z);
				dead = false;
			}
		}

		return dead;
	}

}
