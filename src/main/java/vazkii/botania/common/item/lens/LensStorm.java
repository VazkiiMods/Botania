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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

public class LensStorm extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level.isClientSide && !burst.isFake()) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if (pos.getType() == HitResult.Type.BLOCK
					&& !isManaBlock
					&& !coords.equals(((BlockHitResult) pos).getBlockPos())) {
				entity.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 5F, Explosion.BlockInteraction.DESTROY);
			}
		} else {
			dead = false;
		}

		return dead;
	}

}
