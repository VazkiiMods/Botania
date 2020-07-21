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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityThrowableCopy extends ProjectileEntity {
	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, World worldIn) {
		super(type, worldIn);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, double x, double y, double z, World worldIn) {
		this(type, worldIn);
		this.updatePosition(x, y, z);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, LivingEntity livingEntityIn, World worldIn) {
		this(type, livingEntityIn.getX(), livingEntityIn.getEyeY() - (double) 0.1F, livingEntityIn.getZ(), worldIn);
		this.setOwner(livingEntityIn);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		double d0 = this.getBoundingBox().getAverageSideLength() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		HitResult raytraceresult = ProjectileUtil.getCollision(this, this::method_26958, RayTraceContext.ShapeType.OUTLINE);
		boolean flag = false;
		if (raytraceresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
			BlockState blockstate = this.world.getBlockState(blockpos);
			if (blockstate.isOf(Blocks.NETHER_PORTAL)) {
				this.setInNetherPortal(blockpos);
				flag = true;
			} else if (blockstate.isOf(Blocks.END_GATEWAY)) {
				BlockEntity tileentity = this.world.getBlockEntity(blockpos);
				if (tileentity instanceof EndGatewayBlockEntity) {
					((EndGatewayBlockEntity) tileentity).tryTeleportingEntity(this);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onCollision(raytraceresult);
		}

		Vec3d vector3d = this.getVelocity();
		double d2 = this.getX() + vector3d.x;
		double d0 = this.getY() + vector3d.y;
		double d1 = this.getZ() + vector3d.z;
		this.method_26962();
		float f;
		if (this.isTouchingWater()) {
			for (int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setVelocity(vector3d.multiply((double) f));
		if (!this.hasNoGravity()) {
			Vec3d vector3d1 = this.getVelocity();
			this.setVelocity(vector3d1.x, vector3d1.y - (double) this.getGravityVelocity(), vector3d1.z);
		}

		this.updatePosition(d2, d0, d1);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}
