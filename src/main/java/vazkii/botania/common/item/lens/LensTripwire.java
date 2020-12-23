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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaSpreader;

import java.util.List;

public class LensTripwire extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		IManaBurst burst = spreader.runBurstSimulation();
		return burst.hasTripped();
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (burst.isFake()) {
			if (entity.world.isClient) {
				return;
			}

			Box axis = new Box(entity.getX(), entity.getY(), entity.getZ(), entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ).expand(0.25);
			List<LivingEntity> entities = entity.world.getNonSpectatingEntities(LivingEntity.class, axis);
			if (!entities.isEmpty()) {
				burst.setTripped(true);
			}
		}

	}

}
