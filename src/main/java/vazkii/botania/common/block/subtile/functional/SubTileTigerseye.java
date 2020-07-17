/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.MixinAvoidEntityGoal;
import vazkii.botania.mixin.MixinCreeperEntity;
import vazkii.botania.mixin.MixinGoalSelector;
import vazkii.botania.mixin.MixinNearestAttackableTarget;

import java.util.ArrayList;
import java.util.List;

public class SubTileTigerseye extends TileEntityFunctionalFlower {
	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;

	public SubTileTigerseye() {
		super(ModSubtiles.TIGERSEYE);
	}

	@Override
	@SuppressWarnings("deprecated")
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			return;
		}

		boolean shouldAfffect = getMana() >= COST;

		List<MobEntity> entities = getWorld().getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));

		for (MobEntity entity : entities) {
			List<PrioritizedGoal> entries = new ArrayList<>(((MixinGoalSelector) entity.goalSelector).getGoals());
			entries.addAll(((MixinGoalSelector) entity.targetSelector).getGoals());

			boolean avoidsOcelots = false;
			if (shouldAfffect) {
				for (PrioritizedGoal entry : entries) {
					if (entry.getGoal() instanceof AvoidEntityGoal) {
						avoidsOcelots = messWithRunAwayAI((AvoidEntityGoal) entry.getGoal()) || avoidsOcelots;
					}

					if (entry.getGoal() instanceof NearestAttackableTargetGoal) {
						messWithGetTargetAI((NearestAttackableTargetGoal) entry.getGoal());
					}
				}
			}

			if (entity instanceof CreeperEntity) {
				((MixinCreeperEntity) entity).setTimeSinceIgnited(2);
				entity.setAttackTarget(null);
			}

			if (avoidsOcelots) {
				addMana(-COST);
				sync();
				shouldAfffect = false;
			}
		}
	}

	// TODO replace this kludge
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean messWithRunAwayAI(AvoidEntityGoal aiEntry) {
		MixinAvoidEntityGoal mEntry = (MixinAvoidEntityGoal) aiEntry;
		if (mEntry.getClassToAvoid() == OcelotEntity.class) {
			mEntry.setClassToAvoid(PlayerEntity.class);
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void messWithGetTargetAI(NearestAttackableTargetGoal aiEntry) {
		MixinNearestAttackableTarget mEntry = (MixinNearestAttackableTarget) aiEntry;
		if (mEntry.getTargetClass() == PlayerEntity.class) {
			mEntry.setTargetClass(EnderCrystalEntity.class); // Something random that won't be around
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xB1A618;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

}
