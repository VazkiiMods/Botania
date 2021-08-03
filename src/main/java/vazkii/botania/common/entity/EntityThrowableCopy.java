/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class EntityThrowableCopy extends Projectile {
	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, Level worldIn) {
		super(type, worldIn);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, double x, double y, double z, Level worldIn) {
		this(type, worldIn);
		this.setPos(x, y, z);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, LivingEntity livingEntityIn, Level worldIn) {
		this(type, livingEntityIn.getX(), livingEntityIn.getEyeY() - (double) 0.1F, livingEntityIn.getZ(), worldIn);
		this.setOwner(livingEntityIn);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		super.tick();
		HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
		boolean flag = false;
		if (raytraceresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				BlockEntity tileentity = this.level.getBlockEntity(blockpos);
				if (tileentity instanceof TheEndGatewayBlockEntity) {
					((TheEndGatewayBlockEntity) tileentity).teleportEntity(this);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != HitResult.Type.MISS && !flag) {
			this.onHit(raytraceresult);
		}

		this.checkInsideBlocks();
		Vec3 vector3d = this.getDeltaMovement();
		double d2 = this.getX() + vector3d.x;
		double d0 = this.getY() + vector3d.y;
		double d1 = this.getZ() + vector3d.z;
		this.updateRotation();
		float f;
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.level.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setDeltaMovement(vector3d.scale((double) f));
		if (!this.isNoGravity()) {
			Vec3 vector3d1 = this.getDeltaMovement();
			this.setDeltaMovement(vector3d1.x, vector3d1.y - (double) this.getGravityVelocity(), vector3d1.z);
		}

		this.setPos(d2, d0, d1);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
