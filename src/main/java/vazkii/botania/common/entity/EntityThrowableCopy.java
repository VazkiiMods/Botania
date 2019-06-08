package vazkii.botania.common.entity;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.block.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityThrowableCopy extends Entity implements IProjectile {
	private int xTile;
	private int yTile;
	private int zTile;
	protected boolean inGround;
	public int throwableShake;
	/** The entity that threw this throwable item. */
	protected LivingEntity thrower;
	private UUID field_200218_h;
	public Entity ignoreEntity;
	private int ignoreTime;

	protected EntityThrowableCopy(EntityType<?> type, World p_i48540_2_) {
		super(type, p_i48540_2_);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.setSize(0.25F, 0.25F);
	}

	protected EntityThrowableCopy(EntityType<?> type, double p_i48541_2_, double p_i48541_4_, double p_i48541_6_, World p_i48541_8_) {
		this(type, p_i48541_8_);
		this.setPosition(p_i48541_2_, p_i48541_4_, p_i48541_6_);
	}

	protected EntityThrowableCopy(EntityType<?> type, LivingEntity p_i48542_2_, World p_i48542_3_) {
		this(type, p_i48542_2_.posX, p_i48542_2_.posY + (double)p_i48542_2_.getEyeHeight() - (double)0.1F, p_i48542_2_.posZ, p_i48542_3_);
		this.thrower = p_i48542_2_;
		this.field_200218_h = p_i48542_2_.getUniqueID();
	}

	protected void registerData() {
	}

	/**
	 * Checks if the entity is in range to render.
	 */
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
	 * Sets throwable heading based on an entity that's throwing it
	 */
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
		float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
		this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
		this.motionX += entityThrower.motionX;
		this.motionZ += entityThrower.motionZ;
		if (!entityThrower.onGround) {
			this.motionY += entityThrower.motionY;
		}

	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
	 */
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double)f;
		y = y / (double)f;
		z = z / (double)f;
		x = x + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		y = y + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		z = z + this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
		x = x * (double)velocity;
		y = y * (double)velocity;
		z = z * (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (double)(180F / (float)Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
			this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.tick();
		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
			this.inGround = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
		}

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = null;
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
		double d0 = 0.0D;
		boolean flag = false;

		for(int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);
			if (entity1.canBeCollidedWith()) {
				if (entity1 == this.ignoreEntity) {
					flag = true;
				} else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
					this.ignoreEntity = entity1;
					flag = true;
				} else {
					flag = false;
					AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)0.3F);
					RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
					if (raytraceresult1 != null) {
						double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
		}

		if (this.ignoreEntity != null) {
			if (flag) {
				this.ignoreTime = 2;
			} else if (this.ignoreTime-- <= 0) {
				this.ignoreEntity = null;
			}
		}

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null) {
			if (raytraceresult.type == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.NETHER_PORTAL) {
				this.setPortal(raytraceresult.getBlockPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
				this.onImpact(raytraceresult);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (double)(180F / (float)Math.PI));

		for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = this.getGravityVelocity();
		if (this.isInWater()) {
			for(int j = 0; j < 4; ++j) {
				float f3 = 0.25F;
				this.world.addParticle(Particles.BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f1 = 0.8F;
		}

		this.motionX *= (double)f1;
		this.motionY *= (double)f1;
		this.motionZ *= (double)f1;
		if (!this.hasNoGravity()) {
			this.motionY -= (double)f2;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.03F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected abstract void onImpact(RayTraceResult result);

	/**
	 * Writes the extra NBT data specific to this type of entity. Should <em>not</em> be called from outside this class;
	 * use {@link #writeUnlessPassenger} or {@link #writeWithoutTypeId} instead.
	 */
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("xTile", this.xTile);
		compound.putInt("yTile", this.yTile);
		compound.putInt("zTile", this.zTile);
		compound.putByte("shake", (byte)this.throwableShake);
		compound.putByte("inGround", (byte)(this.inGround ? 1 : 0));
		if (this.field_200218_h != null) {
			compound.put("owner", NBTUtil.writeUniqueId(this.field_200218_h));
		}

	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		this.xTile = compound.getInt("xTile");
		this.yTile = compound.getInt("yTile");
		this.zTile = compound.getInt("zTile");
		this.throwableShake = compound.getByte("shake") & 255;
		this.inGround = compound.getByte("inGround") == 1;
		this.thrower = null;
		if (compound.contains("owner", 10)) {
			this.field_200218_h = NBTUtil.readUniqueId(compound.getCompound("owner"));
		}

	}

	@Nullable
	public LivingEntity getThrower() {
		if (this.thrower == null && this.field_200218_h != null && this.world instanceof ServerWorld) {
			Entity entity = ((ServerWorld)this.world).getEntityFromUuid(this.field_200218_h);
			if (entity instanceof LivingEntity) {
				this.thrower = (LivingEntity)entity;
			} else {
				this.field_200218_h = null;
			}
		}

		return this.thrower;
	}
}