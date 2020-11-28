/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

import vazkii.botania.api.internal.IManaBurst;

public class LensExplosive extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (!entity.world.isClient && !burst.isFake() && pos.getType() == HitResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if (!isManaBlock && !coords.equals(((BlockHitResult) pos).getBlockPos())) {
				Entity cause = entity.getOwner() != null ? entity.getOwner() : entity;
				entity.world.createExplosion(cause, entity.getX(), entity.getY(), entity.getZ(),
						burst.getMana() / 50F, Explosion.DestructionType.BREAK);
			}
		} else {
			dead = false;
		}

		return dead;
	}

}
