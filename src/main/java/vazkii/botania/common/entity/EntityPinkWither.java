/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import vazkii.botania.mixin.AccessorGoalSelector;
import vazkii.botania.mixin.AccessorWitherEntity;

import javax.annotation.Nonnull;

public class EntityPinkWither extends WitherEntity {
	public EntityPinkWither(EntityType<EntityPinkWither> type, World world) {
		super(type, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		// Remove firing wither skulls
		((AccessorGoalSelector) goalSelector).getGoals().removeIf(entry -> entry.getGoal() instanceof ProjectileAttackGoal);

		// Remove revenge and aggro
		((AccessorGoalSelector) targetSelector).getGoals().removeIf(entry -> entry.getGoal() instanceof RevengeGoal
				|| entry.getGoal() instanceof FollowTargetGoal);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		if (Math.random() < 0.1) {
			for (int j = 0; j < 3; ++j) {
				double x = ((AccessorWitherEntity) this).botania_getHeadX(j);
				double y = ((AccessorWitherEntity) this).botania_getHeadY(j);
				double z = ((AccessorWitherEntity) this).botania_getHeadZ(j);
				world.addParticle(ParticleTypes.HEART, x + random.nextGaussian() * 0.3, y + random.nextGaussian() * 0.3, z + random.nextGaussian() * 0.3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {}

	@Override
	public void mobTick() {
		if (age % 20 == 0) {
			heal(1.0F);
		}
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!player.isSneaking()) {
			player.startRiding(this);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	@Override
	public void onStartedTrackingBy(@Nonnull ServerPlayerEntity player) {}
}
