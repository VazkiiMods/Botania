/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;

public class StormLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {
			if (!entity.level().isClientSide && !burst.isFake() && !isManaBlock) {
				entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 5F, Level.ExplosionInteraction.BLOCK);
			}
			return true;
		}

		return shouldKill;
	}

}
