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
import com.google.common.collect.Iterables;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.item.ModItems;

public class LensInfluence extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!burst.isFake()) {
			double range = 3.5;
			AABB bounds = new AABB(entity.getX() - range, entity.getY() - range, entity.getZ() - range, entity.getX() + range, entity.getY() + range, entity.getZ() + range);
			var items = entity.level.getEntitiesOfClass(ItemEntity.class, bounds);
			var expOrbs = entity.level.getEntitiesOfClass(ExperienceOrb.class, bounds);
			var arrows = entity.level.getEntitiesOfClass(AbstractArrow.class, bounds);
			var fallingBlocks = entity.level.getEntitiesOfClass(FallingBlockEntity.class, bounds);
			var primedTnt = entity.level.getEntitiesOfClass(PrimedTnt.class, bounds);
			var bursts = entity.level.getEntitiesOfClass(ThrowableProjectile.class, bounds, Predicates.instanceOf(IManaBurst.class));

			@SuppressWarnings("unchecked")
			var concat = Iterables.concat(items, expOrbs, arrows, fallingBlocks, primedTnt, bursts);
			for (Entity movable : concat) {
				if (movable == burst) {
					continue;
				}

				if (movable instanceof IManaBurst otherBurst) {
					ItemStack lens = otherBurst.getSourceLens();
					if (!lens.isEmpty() && lens.is(ModItems.lensInfluence)) {
						continue;
					}
				}
				movable.setDeltaMovement(entity.getDeltaMovement());
			}
		}
	}

}
