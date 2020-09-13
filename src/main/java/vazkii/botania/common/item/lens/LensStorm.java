/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;

import vazkii.botania.api.internal.IManaBurst;

public class LensStorm extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableEntity entity = burst.entity();
		if (!entity.world.isRemote && !burst.isFake()) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if (pos.getType() == RayTraceResult.Type.BLOCK
					&& !isManaBlock
					&& !coords.equals(((BlockRayTraceResult) pos).getPos())) {
				entity.world.createExplosion(entity, entity.getPosX(), entity.getPosY(), entity.getPosZ(), 5F, Explosion.Mode.DESTROY);
			}
		} else {
			dead = false;
		}

		return dead;
	}

}
