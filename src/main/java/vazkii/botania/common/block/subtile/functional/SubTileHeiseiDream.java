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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.AccessorGoalSelector;
import vazkii.botania.mixin.AccessorMobEntity;

import java.util.List;

public class SubTileHeiseiDream extends TileEntityFunctionalFlower {
	private static final int RANGE = 5;
	private static final int COST = 100;

	public SubTileHeiseiDream(BlockPos pos, BlockState state) {
		super(ModSubtiles.HEISEI_DREAM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		@SuppressWarnings("unchecked")
		List<Enemy> mobs = (List) getLevel().getEntitiesOfClass(Entity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)), Predicates.instanceOf(Enemy.class));

		if (mobs.size() > 1 && getMana() >= COST) {
			for (Enemy mob : mobs) {
				if (mob instanceof Mob) {
					Mob entity = (Mob) mob;
					if (brainwashEntity(entity, mobs)) {
						addMana(-COST);
						sync();
						break;
					}
				}
			}
		}
	}

	public static boolean brainwashEntity(Mob entity, List<Enemy> mobs) {
		LivingEntity target = entity.getTarget();
		boolean did = false;

		if (!(target instanceof Enemy)) {
			Enemy newTarget;
			do {
				newTarget = mobs.get(entity.level.random.nextInt(mobs.size()));
			} while (newTarget == entity);

			if (newTarget instanceof Mob) {
				entity.setTarget(null);

				// Move any EntityAIHurtByTarget to highest priority
				GoalSelector targetSelector = ((AccessorMobEntity) entity).getTargetSelector();
				for (WrappedGoal entry : ((AccessorGoalSelector) targetSelector).getAvailableGoals()) {
					if (entry.getGoal() instanceof HurtByTargetGoal) {
						// Concurrent modification OK since we break out of the loop
						targetSelector.removeGoal(entry.getGoal());
						targetSelector.addGoal(-1, entry.getGoal());
						break;
					}
				}

				// Now set revenge target, which EntityAIHurtByTarget will pick up
				entity.setLastHurtByMob((Mob) newTarget);
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
