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
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import vazkii.botania.mixin.MixinGoalSelector;
import vazkii.botania.mixin.MixinWitherEntity;

import javax.annotation.Nonnull;

public class EntityPinkWither extends WitherEntity {
	public EntityPinkWither(EntityType<EntityPinkWither> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Remove firing wither skulls
		((MixinGoalSelector) goalSelector).getGoals().removeIf(entry -> entry.getGoal() instanceof RangedAttackGoal);

		// Remove revenge and aggro
		((MixinGoalSelector) targetSelector).getGoals().removeIf(entry -> entry.getGoal() instanceof HurtByTargetGoal
				|| entry.getGoal() instanceof NearestAttackableTargetGoal);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (Math.random() < 0.1) {
			for (int j = 0; j < 3; ++j) {
				double x = ((MixinWitherEntity) this).callGetHeadX(j);
				double y = ((MixinWitherEntity) this).callGetHeadY(j);
				double z = ((MixinWitherEntity) this).callGetHeadZ(j);
				world.addParticle(ParticleTypes.HEART, x + rand.nextGaussian() * 0.3, y + rand.nextGaussian() * 0.3, z + rand.nextGaussian() * 0.3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void updateAITasks() {
		if (ticksExisted % 20 == 0) {
			heal(1.0F);
		}
	}

	@Override
	protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		if (!player.isSneaking()) {
			player.startRiding(this);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void addTrackingPlayer(@Nonnull ServerPlayerEntity player) {}
}
