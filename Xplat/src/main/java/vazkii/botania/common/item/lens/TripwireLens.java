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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaSpreader;

import java.util.List;

public class TripwireLens extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, ManaSpreader spreader, boolean redstone) {
		ManaBurst burst = spreader.runBurstSimulation();
		return burst.hasTripped();
	}

	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (burst.isFake()) {
			if (entity.getLevel().isClientSide) {
				return;
			}

			AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(0.25);
			List<LivingEntity> entities = entity.getLevel().getEntitiesOfClass(LivingEntity.class, axis);
			if (!entities.isEmpty()) {
				burst.setTripped(true);
			}
		}

	}

}
