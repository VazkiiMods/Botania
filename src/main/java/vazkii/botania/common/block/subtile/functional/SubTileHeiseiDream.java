/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.Box;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.AccessorGoalSelector;

import java.util.List;

public class SubTileHeiseiDream extends TileEntityFunctionalFlower {
	private static final int RANGE = 5;
	private static final int COST = 100;

	public SubTileHeiseiDream() {
		super(ModSubtiles.HEISEI_DREAM);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			return;
		}

		@SuppressWarnings("unchecked")
		List<Monster> mobs = (List) getWorld().getEntitiesByClass(Entity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)), Predicates.instanceOf(Monster.class));

		if (mobs.size() > 1 && getMana() >= COST) {
			for (Monster mob : mobs) {
				if (mob instanceof MobEntity) {
					MobEntity entity = (MobEntity) mob;
					if (brainwashEntity(entity, mobs)) {
						addMana(-COST);
						sync();
						break;
					}
				}
			}
		}
	}

	public static boolean brainwashEntity(MobEntity entity, List<Monster> mobs) {
		LivingEntity target = entity.getTarget();
		boolean did = false;

		if (!(target instanceof Monster)) {
			Monster newTarget;
			do {
				newTarget = mobs.get(entity.world.random.nextInt(mobs.size()));
			} while (newTarget == entity);

			if (newTarget instanceof MobEntity) {
				entity.setTarget(null);

				// Move any EntityAIHurtByTarget to highest priority
				for (PrioritizedGoal entry : ((AccessorGoalSelector) entity.targetSelector).getGoals()) {
					if (entry.getGoal() instanceof RevengeGoal) {
						// Concurrent modification OK since we break out of the loop
						entity.targetSelector.remove(entry.getGoal());
						entity.targetSelector.add(-1, entry.getGoal());
						break;
					}
				}

				// Now set revenge target, which EntityAIHurtByTarget will pick up
				entity.setAttacker((MobEntity) newTarget);
				did = true;
			}
		}

		return did;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF219D;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

}
