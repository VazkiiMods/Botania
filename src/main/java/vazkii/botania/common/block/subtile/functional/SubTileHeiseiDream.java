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
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;

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

		if (getWorld().isRemote) {
			return;
		}

		@SuppressWarnings("unchecked")
		List<IMob> mobs = (List) getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)), Predicates.instanceOf(IMob.class));

		if (mobs.size() > 1 && getMana() >= COST) {
			for (IMob mob : mobs) {
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

	public static boolean brainwashEntity(MobEntity entity, List<IMob> mobs) {
		LivingEntity target = entity.getAttackTarget();
		boolean did = false;

		if (!(target instanceof IMob)) {
			IMob newTarget;
			do {
				newTarget = mobs.get(entity.world.rand.nextInt(mobs.size()));
			} while (newTarget == entity);

			if (newTarget instanceof MobEntity) {
				entity.setAttackTarget(null);

				// Move any EntityAIHurtByTarget to highest priority
				for (PrioritizedGoal entry : ((AccessorGoalSelector) entity.targetSelector).getGoals()) {
					if (entry.getGoal() instanceof HurtByTargetGoal) {
						// Concurrent modification OK since we break out of the loop
						entity.targetSelector.removeGoal(entry.getGoal());
						entity.targetSelector.addGoal(-1, entry.getGoal());
						break;
					}
				}

				// Now set revenge target, which EntityAIHurtByTarget will pick up
				entity.setRevengeTarget((MobEntity) newTarget);
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
