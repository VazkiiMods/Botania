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
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class EntityPinkWither extends WitherEntity {
	public EntityPinkWither(EntityType<EntityPinkWither> type, World world) {
		super(type, world);

		// Remove firing wither skulls
		goalSelector.goals.removeIf(entry -> entry.getGoal() instanceof RangedAttackGoal);

		// Remove revenge and aggro
		targetSelector.goals.removeIf(entry -> entry.getGoal() instanceof HurtByTargetGoal
				|| entry.getGoal() instanceof NearestAttackableTargetGoal);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (Math.random() < 0.1) {
			for (int j = 0; j < 3; ++j) {
				double d10 = getHeadX(j);
				double d2 = getHeadY(j);
				double d4 = getHeadZ(j);
				world.addParticle(ParticleTypes.HEART, d10 + rand.nextGaussian() * 0.30000001192092896D, d2 + rand.nextGaussian() * 0.30000001192092896D, d4 + rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
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
	protected boolean processInteract(PlayerEntity player, Hand hand) {
		if (!player.isSneaking()) {
			player.startRiding(this);
			return true;
		}
		return false;
	}

	@Override
	public void addTrackingPlayer(@Nonnull ServerPlayerEntity player) {}

	// [VanillaCopy] super

	private double getHeadX(int p_82214_1_) {
		if (p_82214_1_ <= 0) {
			return this.getPosX();
		} else {
			float f = (this.renderYawOffset + (float) (180 * (p_82214_1_ - 1))) * ((float) Math.PI / 180F);
			float f1 = MathHelper.cos(f);
			return this.getPosX() + (double) f1 * 1.3D;
		}
	}

	private double getHeadY(int p_82208_1_) {
		return p_82208_1_ <= 0 ? this.getPosY() + 3.0D : this.getPosY() + 2.2D;
	}

	private double getHeadZ(int p_82213_1_) {
		if (p_82213_1_ <= 0) {
			return this.getPosZ();
		} else {
			float f = (this.renderYawOffset + (float) (180 * (p_82213_1_ - 1))) * ((float) Math.PI / 180F);
			float f1 = MathHelper.sin(f);
			return this.getPosZ() + (double) f1 * 1.3D;
		}
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
