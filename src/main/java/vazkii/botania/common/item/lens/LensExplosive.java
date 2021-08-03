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
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

public class LensExplosive extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (!entity.level.isClientSide && !burst.isFake() && pos.getType() == HitResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if (!isManaBlock && !coords.equals(((BlockHitResult) pos).getBlockPos())) {
				entity.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(),
						burst.getMana() / 50F, Explosion.BlockInteraction.BREAK);
			}
		} else {
			dead = false;
		}

		return dead;
	}

}
