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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Intentionally identical implementation to ThrowableProjectile.
 * The idea is that mods that reflect throwable projectiles won't reflect this variant.
 */
// [VanillaCopy] ThrowableProjectile
public abstract class LegallyDistinctThrowableProjectile extends Projectile {
	protected LegallyDistinctThrowableProjectile(EntityType<? extends LegallyDistinctThrowableProjectile> entityType, Level level) {
		super(entityType, level);
	}

	protected LegallyDistinctThrowableProjectile(EntityType<? extends LegallyDistinctThrowableProjectile> entityType, double x, double y, double z, Level level) {
		this(entityType, level);
		this.setPos(x, y, z);
	}

	protected LegallyDistinctThrowableProjectile(EntityType<? extends LegallyDistinctThrowableProjectile> entityType, LivingEntity shooter, Level level) {
		this(entityType, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), level);
		this.setOwner(shooter);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = this.getBoundingBox().getSize() * 4.0;
		if (Double.isNaN(d0)) {
			d0 = 4.0;
		}

		d0 *= 64.0;
		return distance < d0 * d0;
	}

	@Override
	public boolean canUsePortal(boolean allowPassengers) {
		return true;
	}

	@Override
	public void tick() {
		super.tick();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS) {
			this.hitTargetOrDeflectSelf(hitresult);
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double x = this.getX() + vec3.x;
		double y = this.getY() + vec3.y;
		double z = this.getZ() + vec3.z;
		this.updateRotation();
		float f;
		if (this.isInWater()) {
			for (int i = 0; i < 4; i++) {
				this.level().addParticle(ParticleTypes.BUBBLE, x - vec3.x * 0.25, y - vec3.y * 0.25, z - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setDeltaMovement(vec3.scale(f));
		this.applyGravity();
		this.setPos(x, y, z);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.03;
	}
}
