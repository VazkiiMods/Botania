/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 16, 2015, 4:01:43 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// A copy of the vanilla EntityThrowable class
// Doing this because if I didn't do this it'd be an EntityThrowable
// And we all know how much mods like deflecting EntityThrowables
public abstract class EntityThrowableCopy extends Entity implements IProjectile {
	private int field_145788_c = -1;
	private int field_145786_d = -1;
	private int field_145787_e = -1;
	private Block field_145785_f;
	protected boolean inGround;
	public int throwableShake;
	/** The entity that threw this throwable item. */
	private EntityLivingBase thrower;
	private String throwerName;
	private int ticksInGround;
	private int ticksInAir;
	private static final String __OBFID = "CL_00001723";

	public EntityThrowableCopy(World p_i1776_1_)
	{
		super(p_i1776_1_);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {}

	/**
	 * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
	 * length * 64 * renderDistanceWeight Args: distance
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_)
	{
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return p_70112_1_ < d1 * d1;
	}

	public EntityThrowableCopy(World p_i1777_1_, EntityLivingBase p_i1777_2_)
	{
		super(p_i1777_1_);
		this.thrower = p_i1777_2_;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(p_i1777_2_.posX, p_i1777_2_.posY + (double)p_i1777_2_.getEyeHeight(), p_i1777_2_.posZ, p_i1777_2_.rotationYaw, p_i1777_2_.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		float f = 0.4F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
		this.motionY = (double)(-MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
	}

	public EntityThrowableCopy(World p_i1778_1_, double p_i1778_2_, double p_i1778_4_, double p_i1778_6_)
	{
		super(p_i1778_1_);
		this.ticksInGround = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(p_i1778_2_, p_i1778_4_, p_i1778_6_);
		this.yOffset = 0.0F;
	}

	protected float func_70182_d()
	{
		return 1.5F;
	}

	protected float func_70183_g()
	{
		return 0.0F;
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
	 */
	 public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_)
	 {
		 float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
		 p_70186_1_ /= (double)f2;
		 p_70186_3_ /= (double)f2;
		 p_70186_5_ /= (double)f2;
		 p_70186_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
		 p_70186_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
		 p_70186_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
		 p_70186_1_ *= (double)p_70186_7_;
		 p_70186_3_ *= (double)p_70186_7_;
		 p_70186_5_ *= (double)p_70186_7_;
		 this.motionX = p_70186_1_;
		 this.motionY = p_70186_3_;
		 this.motionZ = p_70186_5_;
		 float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
		 this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
		 this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70186_3_, (double)f3) * 180.0D / Math.PI);
		 this.ticksInGround = 0;
	 }

	 /**
	  * Sets the velocity to the args. Args: x, y, z
	  */
	 @SideOnly(Side.CLIENT)
	 public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
	 {
		 this.motionX = p_70016_1_;
		 this.motionY = p_70016_3_;
		 this.motionZ = p_70016_5_;

		 if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		 {
			 float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			 this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			 this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70016_3_, (double)f) * 180.0D / Math.PI);
		 }
	 }

	 /**
	  * Called to update the entity's position/logic.
	  */
	 public void onUpdate()
	 {
		 this.lastTickPosX = this.posX;
		 this.lastTickPosY = this.posY;
		 this.lastTickPosZ = this.posZ;
		 super.onUpdate();

		 if (this.throwableShake > 0)
		 {
			 --this.throwableShake;
		 }

		 if (this.inGround)
		 {
			 if (this.worldObj.getBlock(this.field_145788_c, this.field_145786_d, this.field_145787_e) == this.field_145785_f)
			 {
				 ++this.ticksInGround;

				 if (this.ticksInGround == 1200)
				 {
					 this.setDead();
				 }

				 return;
			 }

			 this.inGround = false;
			 this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			 this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			 this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			 this.ticksInGround = 0;
			 this.ticksInAir = 0;
		 }
		 else
		 {
			 ++this.ticksInAir;
		 }

		 Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		 Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		 MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
		 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		 if (movingobjectposition != null)
		 {
			 vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		 }

		 if (!this.worldObj.isRemote)
		 {
			 Entity entity = null;
			 List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			 double d0 = 0.0D;
			 EntityLivingBase entitylivingbase = this.getThrower();

			 for (int j = 0; j < list.size(); ++j)
			 {
				 Entity entity1 = (Entity)list.get(j);

				 if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5))
				 {
					 float f = 0.3F;
					 AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
					 MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					 if (movingobjectposition1 != null)
					 {
						 double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						 if (d1 < d0 || d0 == 0.0D)
						 {
							 entity = entity1;
							 d0 = d1;
						 }
					 }
				 }
			 }

			 if (entity != null)
			 {
				 movingobjectposition = new MovingObjectPosition(entity);
			 }
		 }

		 if (movingobjectposition != null)
		 {
			 if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal)
			 {
				 this.setInPortal();
			 }
			 else
			 {
				 this.onImpact(movingobjectposition);
			 }
		 }

		 this.posX += this.motionX;
		 this.posY += this.motionY;
		 this.posZ += this.motionZ;
		 float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		 this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

		 for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
		 {
			 ;
		 }

		 while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
		 {
			 this.prevRotationPitch += 360.0F;
		 }

		 while (this.rotationYaw - this.prevRotationYaw < -180.0F)
		 {
			 this.prevRotationYaw -= 360.0F;
		 }

		 while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
		 {
			 this.prevRotationYaw += 360.0F;
		 }

		 this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		 this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		 float f2 = 0.99F;
		 float f3 = this.getGravityVelocity();

		 if (this.isInWater())
		 {
			 for (int i = 0; i < 4; ++i)
			 {
				 float f4 = 0.25F;
				 this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
			 }

			 f2 = 0.8F;
		 }

		 this.motionX *= (double)f2;
		 this.motionY *= (double)f2;
		 this.motionZ *= (double)f2;
		 this.motionY -= (double)f3;
		 this.setPosition(this.posX, this.posY, this.posZ);
	 }

	 /**
	  * Gets the amount of gravity to apply to the thrown entity with each tick.
	  */
	 protected float getGravityVelocity()
	 {
		 return 0.03F;
	 }

	 /**
	  * Called when this EntityThrowable hits a block or entity.
	  */
	 protected abstract void onImpact(MovingObjectPosition p_70184_1_);

	 /**
	  * (abstract) Protected helper method to write subclass entity data to NBT.
	  */
	 public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	 {
		 p_70014_1_.setShort("xTile", (short)this.field_145788_c);
		 p_70014_1_.setShort("yTile", (short)this.field_145786_d);
		 p_70014_1_.setShort("zTile", (short)this.field_145787_e);
		 p_70014_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.field_145785_f));
		 p_70014_1_.setByte("shake", (byte)this.throwableShake);
		 p_70014_1_.setByte("inGround", (byte)(this.inGround ? 1 : 0));

		 if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer)
		 {
			 this.throwerName = this.thrower.getCommandSenderName();
		 }

		 p_70014_1_.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
	 }

	 /**
	  * (abstract) Protected helper method to read subclass entity data from NBT.
	  */
	 public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	 {
		 this.field_145788_c = p_70037_1_.getShort("xTile");
		 this.field_145786_d = p_70037_1_.getShort("yTile");
		 this.field_145787_e = p_70037_1_.getShort("zTile");
		 this.field_145785_f = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		 this.throwableShake = p_70037_1_.getByte("shake") & 255;
		 this.inGround = p_70037_1_.getByte("inGround") == 1;
		 this.throwerName = p_70037_1_.getString("ownerName");

		 if (this.throwerName != null && this.throwerName.length() == 0)
		 {
			 this.throwerName = null;
		 }
	 }

	 @SideOnly(Side.CLIENT)
	 public float getShadowSize()
	 {
		 return 0.0F;
	 }

	 public EntityLivingBase getThrower()
	 {
		 if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0)
		 {
			 this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
		 }

		 return this.thrower;
	 }
}