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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;

public class EntityPixie extends FlyingEntity {
	private static final TrackedData<Integer> PIXIE_TYPE = DataTracker.registerData(EntityPixie.class, TrackedDataHandlerRegistry.INTEGER);

	private LivingEntity summoner = null;
	private float damage = 0;
	private StatusEffectInstance effect = null;

	public EntityPixie(EntityType<EntityPixie> type, World world) {
		super(type, world);
	}

	public EntityPixie(World world) {
		this(ModEntities.PIXIE, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(PIXIE_TYPE, 0);
	}

	public void setPixieType(int type) {
		dataTracker.set(PIXIE_TYPE, type);
	}

	public int getPixieType() {
		return dataTracker.get(PIXIE_TYPE);
	}

	public void setProps(LivingEntity target, LivingEntity summoner, int type, float damage) {
		setTarget(target);
		this.summoner = summoner;
		this.damage = damage;
		setPixieType(type);
	}

	public void setApplyPotionEffect(StatusEffectInstance effect) {
		this.effect = effect;
	}

	@Override
	protected void mobTick() {
		LivingEntity target = getTarget();
		if (target != null) {
			double d0 = target.getX() + target.getWidth() / 2 - getX();
			double d1 = target.getY() + target.getHeight() / 2 - getY();
			double d2 = target.getZ() + target.getWidth() / 2 - getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			float mod = 0.45F;
			if (getPixieType() == 1) {
				mod = 0.1F;
			}

			setVelocity(d0 / d3 * mod, d1 / d3 * mod, d2 / d3 * mod);

			if (Math.sqrt(d3) < 1F) {
				if (summoner != null) {
					if (summoner instanceof PlayerEntity) {
						target.damage(DamageSource.player((PlayerEntity) summoner), damage);
					} else {
						target.damage(DamageSource.mob(summoner), damage);
					}
				} else {
					target.damage(DamageSource.mob(this), damage);
				}
				if (effect != null && !(target instanceof PlayerEntity)) {
					target.addStatusEffect(effect);
				}
				remove();
			}
		}

		bodyYaw = yaw = -((float) Math.atan2(getVelocity().getX(), getVelocity().getZ()))
				* 180.0F / (float) Math.PI;
	}

	@Override
	public boolean damage(@Nonnull DamageSource source, float amount) {
		if (getPixieType() == 0 && source.getAttacker() != summoner || getPixieType() == 1 && source.getAttacker() instanceof PlayerEntity) {
			return super.damage(source, amount);
		}
		return false;
	}

	@Override
	public void baseTick() {
		super.baseTick();

		if (!world.isClient
				&& (getTarget() == null || age > 200)) {
			remove();
		}

		boolean dark = getPixieType() == 1;
		if (world.isClient) {
			for (int i = 0; i < 4; i++) {
				float r = dark ? 0.1F : 1F;
				float g = dark ? 0.025F : 0.25F;
				float b = dark ? 0.09F : 0.9F;
				SparkleParticleData data = SparkleParticleData.sparkle(0.1F + (float) Math.random() * 0.25F, r, g, b, 12);
				world.addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5 + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
			}
		}
	}

	@Override
	public void remove() {
		if (world != null && world.isClient && getPixieType() == 0) {
			for (int i = 0; i < 12; i++) {
				SparkleParticleData data = SparkleParticleData.sparkle(1F + (float) Math.random() * 0.25F, 1F, 0.25F, 0.9F, 5);
				world.addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5 + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
			}
		}
		super.remove();
	}

	@Override
	public boolean collides() {
		return isAlive();
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
}
