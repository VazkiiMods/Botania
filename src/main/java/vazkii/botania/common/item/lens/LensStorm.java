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
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

import vazkii.botania.api.internal.IManaBurst;

public class LensStorm extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.world.isClient && !burst.isFake()) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if (pos.getType() == HitResult.Type.BLOCK
					&& !isManaBlock
					&& !coords.equals(((BlockHitResult) pos).getBlockPos())) {
				entity.world.createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 5F, Explosion.DestructionType.DESTROY);
			}
		} else {
			dead = false;
		}

		return dead;
	}

}
