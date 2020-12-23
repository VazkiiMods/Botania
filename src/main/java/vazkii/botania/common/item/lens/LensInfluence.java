/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class LensInfluence extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!burst.isFake()) {
			double range = 3.5;
			Box bounds = new Box(entity.getX() - range, entity.getY() - range, entity.getZ() - range, entity.getX() + range, entity.getY() + range, entity.getZ() + range);
			List<Entity> movables = entity.world.getNonSpectatingEntities(ItemEntity.class, bounds);
			movables.addAll(entity.world.getNonSpectatingEntities(ExperienceOrbEntity.class, bounds));
			movables.addAll(entity.world.getNonSpectatingEntities(PersistentProjectileEntity.class, bounds));
			movables.addAll(entity.world.getNonSpectatingEntities(FallingBlockEntity.class, bounds));
			movables.addAll(entity.world.getEntitiesByClass(ThrownEntity.class, bounds, Predicates.instanceOf(IManaBurst.class)));

			for (Entity movable : movables) {
				if (movable == burst) {
					continue;
				}

				if (movable instanceof IManaBurst) {
					IManaBurst otherBurst = (IManaBurst) movable;
					ItemStack lens = otherBurst.getSourceLens();
					if (!lens.isEmpty() && lens.getItem() == ModItems.lensInfluence) {
						continue;
					}

					((IManaBurst) movable).setBurstMotion(entity.getVelocity().getX(),
							entity.getVelocity().getY(), entity.getVelocity().getZ());
				} else {
					movable.setVelocity(entity.getVelocity());
				}
			}
		}
	}

}
