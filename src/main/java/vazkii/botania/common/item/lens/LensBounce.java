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
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.IManaBurst;

public class LensBounce extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (!isManaBlock && pos.getType() == HitResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			BlockHitResult rtr = (BlockHitResult) pos;
			if (!coords.equals(rtr.getBlockPos())) {
				Vec3 currentMovementVec = entity.getDeltaMovement();
				Direction dir = rtr.getDirection();
				Vec3 normalVector = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ()).normalize();
				Vec3 movementVec = normalVector.scale(-2 * currentMovementVec.dot(normalVector)).add(currentMovementVec);

				entity.setDeltaMovement(movementVec);
				dead = false;
			}
		}

		return dead;
	}

}
