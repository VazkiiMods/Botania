/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.Vector3;

public class LensBounce extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (!isManaBlock && pos.getType() == HitResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			BlockHitResult rtr = (BlockHitResult) pos;
			if (!coords.equals(rtr.getBlockPos())) {
				Vector3 currentMovementVec = new Vector3(entity.getDeltaMovement());
				Direction dir = rtr.getDirection();
				Vector3 normalVector = new Vector3(dir.getStepX(), dir.getStepY(), dir.getStepZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				burst.setBurstMotion(movementVec.x, movementVec.y, movementVec.z);
				dead = false;
			}
		}

		return dead;
	}

}
