/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityThrowableCopy extends ProjectileEntity {
	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, World worldIn) {
		super(type, worldIn);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, double x, double y, double z, World worldIn) {
		this(type, worldIn);
		this.setPosition(x, y, z);
	}

	protected EntityThrowableCopy(EntityType<? extends EntityThrowableCopy> type, LivingEntity livingEntityIn, World worldIn) {
		this(type, livingEntityIn.getPosX(), livingEntityIn.getPosYEye() - (double) 0.1F, livingEntityIn.getPosZ(), worldIn);
		this.setShooter(livingEntityIn);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;
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
		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_, RayTraceContext.BlockMode.OUTLINE);
		boolean flag = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos();
			BlockState blockstate = this.world.getBlockState(blockpos);
			if (blockstate.isIn(Blocks.NETHER_PORTAL)) {
				this.setPortal(blockpos);
				flag = true;
			} else if (blockstate.isIn(Blocks.END_GATEWAY)) {
				TileEntity tileentity = this.world.getTileEntity(blockpos);
				if (tileentity instanceof EndGatewayTileEntity) {
					((EndGatewayTileEntity) tileentity).teleportEntity(this);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onImpact(raytraceresult);
		}

		Vector3d vector3d = this.getMotion();
		double d2 = this.getPosX() + vector3d.x;
		double d0 = this.getPosY() + vector3d.y;
		double d1 = this.getPosZ() + vector3d.z;
		this.func_234617_x_();
		float f;
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setMotion(vector3d.scale((double) f));
		if (!this.hasNoGravity()) {
			Vector3d vector3d1 = this.getMotion();
			this.setMotion(vector3d1.x, vector3d1.y - (double) this.getGravityVelocity(), vector3d1.z);
		}

		this.setPosition(d2, d0, d1);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
	}
}
