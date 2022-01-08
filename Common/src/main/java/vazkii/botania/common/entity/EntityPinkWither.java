/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.mixin.AccessorGoalSelector;
import vazkii.botania.mixin.AccessorWitherEntity;

import javax.annotation.Nonnull;

public class EntityPinkWither extends WitherBoss {
	public EntityPinkWither(EntityType<EntityPinkWither> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Remove firing wither skulls
		((AccessorGoalSelector) goalSelector).getAvailableGoals().removeIf(entry -> entry.getGoal() instanceof RangedAttackGoal);

		// Remove revenge and aggro
		((AccessorGoalSelector) targetSelector).getAvailableGoals().removeIf(entry -> entry.getGoal() instanceof HurtByTargetGoal
				|| entry.getGoal() instanceof NearestAttackableTargetGoal);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (Math.random() < 0.1) {
			for (int j = 0; j < 3; ++j) {
				double x = ((AccessorWitherEntity) this).botania_getHeadX(j);
				double y = ((AccessorWitherEntity) this).botania_getHeadY(j);
				double z = ((AccessorWitherEntity) this).botania_getHeadZ(j);
				level.addParticle(ParticleTypes.HEART, x + random.nextGaussian() * 0.3, y + random.nextGaussian() * 0.3, z + random.nextGaussian() * 0.3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int lootingMultiplier, boolean allowDrops) {}

	@Override
	public void customServerAiStep() {
		if (tickCount % 20 == 0) {
			heal(1.0F);
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!player.isShiftKeyDown()) {
			player.startRiding(this);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void startSeenByPlayer(@Nonnull ServerPlayer player) {}
}
