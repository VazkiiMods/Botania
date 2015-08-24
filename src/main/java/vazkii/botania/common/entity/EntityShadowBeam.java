/**
 * This class was created by <Phanta>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 23, 2015, 7:31:50 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

public class EntityShadowBeam extends EntityThrowableCopy {
	
	final int effectThreshhold = 2, length = 300;
	
	private int ticksInAir;
	private EntityPlayer shooter;
	
	public EntityShadowBeam(World world) {
		super(world);
		setSize(0F, 0F);
	}

	public EntityShadowBeam(EntityPlayer player) {
		this(player.worldObj);
		shooter = player;
		
		setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw + 180, -player.rotationPitch);

		posX -= MathHelper.cos((rotationYaw + 180) / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw + 180) / 180.0F * (float) Math.PI) * 0.16F;

		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		float f = 0.8F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f);
		double my = MathHelper.sin((rotationPitch + func_70183_g()) / 180.0F * (float) Math.PI) * f;
		setMotion(mx, my, mz);
	}
	
	public void superUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		onEntityUpdate();

		if(throwableShake > 0)
			--throwableShake;

		Vec3 vec3 = new Vector3(posX, posY, posZ).toVec3D();
		Vec3 vec31 = new Vector3(posX + motionX, posY + motionY, posZ + motionZ).toVec3D();
		MovingObjectPosition movingobjectposition = clip(vec3, vec31);

		if(movingobjectposition != null)
			vec31 = new Vector3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord).toVec3D();

		if(!worldObj.isRemote) {
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = getThrower();

			for(int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if(entity1.canBeCollidedWith() && (entity1 != entitylivingbase || ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if(movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if(entity != null)
				movingobjectposition = new MovingObjectPosition(entity);
		}

		if(movingobjectposition != null)
			onImpact(movingobjectposition);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for(rotationPitch = (float)(Math.atan2(motionY, f1) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F);

		while(rotationPitch - prevRotationPitch >= 180.0F)
			prevRotationPitch += 360.0F;

		while(rotationYaw - prevRotationYaw < -180.0F)
			prevRotationYaw -= 360.0F;

		while(rotationYaw - prevRotationYaw >= 180.0F)
			prevRotationYaw += 360.0F;

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		
		setPosition(posX, posY, posZ);
	}
	
	public MovingObjectPosition clip(Vec3 par1Vec3, Vec3 par2Vec3) {
		boolean par3 = false;
		boolean par4 = false;
		if(!Double.isNaN(par1Vec3.xCoord) && !Double.isNaN(par1Vec3.yCoord) && !Double.isNaN(par1Vec3.zCoord)) {
			if(!Double.isNaN(par2Vec3.xCoord) && !Double.isNaN(par2Vec3.yCoord) && !Double.isNaN(par2Vec3.zCoord)) {
				int i = MathHelper.floor_double(par2Vec3.xCoord);
				int j = MathHelper.floor_double(par2Vec3.yCoord);
				int k = MathHelper.floor_double(par2Vec3.zCoord);
				int l = MathHelper.floor_double(par1Vec3.xCoord);
				int i1 = MathHelper.floor_double(par1Vec3.yCoord);
				int j1 = MathHelper.floor_double(par1Vec3.zCoord);
				Block block = worldObj.getBlock(l, i1, j1);
				int l1 = worldObj.getBlockMetadata(l, i1, j1);

				if(block != null && (!par4 || block == null || block.getCollisionBoundingBoxFromPool(worldObj, l, i1, j1) != null) && block != Blocks.air && block.canCollideCheck(l1, par3)) {
					MovingObjectPosition movingobjectposition = block.collisionRayTrace(worldObj, l, i1, j1, par1Vec3, par2Vec3);

					if(movingobjectposition != null)
						return movingobjectposition;
				}

				int k1 = 200;

				while(k1-- >= 0) {
					if(Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord))
						return null;

					if(l == i && i1 == j && j1 == k)
						return null;

					boolean flag2 = true;
					boolean flag3 = true;
					boolean flag4 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;

					if(i > l)
						d0 = l + 1.0D;
					else if(i < l)
						d0 = l + 0.0D;
					else flag2 = false;

					if(j > i1)
						d1 = i1 + 1.0D;
					else if(j < i1)
						d1 = i1 + 0.0D;
					else flag3 = false;

					if(k > j1)
						d2 = j1 + 1.0D;
					else if(k < j1)
						d2 = j1 + 0.0D;
					else flag4 = false;

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = par2Vec3.xCoord - par1Vec3.xCoord;
					double d7 = par2Vec3.yCoord - par1Vec3.yCoord;
					double d8 = par2Vec3.zCoord - par1Vec3.zCoord;

					if(flag2)
						d3 = (d0 - par1Vec3.xCoord) / d6;

					if(flag3)
						d4 = (d1 - par1Vec3.yCoord) / d7;

					if(flag4)
						d5 = (d2 - par1Vec3.zCoord) / d8;

					byte b0;

					if(d3 < d4 && d3 < d5) {
						if(i > l)
							b0 = 4;
						else b0 = 5;

						par1Vec3.xCoord = d0;
						par1Vec3.yCoord += d7 * d3;
						par1Vec3.zCoord += d8 * d3;
					} else if(d4 < d5) {
						if(j > i1)
							b0 = 0;
						else b0 = 1;

						par1Vec3.xCoord += d6 * d4;
						par1Vec3.yCoord = d1;
						par1Vec3.zCoord += d8 * d4;
					} else {
						if(k > j1)
							b0 = 2;
						else b0 = 3;

						par1Vec3.xCoord += d6 * d5;
						par1Vec3.yCoord += d7 * d5;
						par1Vec3.zCoord = d2;
					}

					Vec3 vec32 = new Vector3(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord).toVec3D();
					l = (int)(vec32.xCoord = MathHelper.floor_double(par1Vec3.xCoord));

					if(b0 == 5) {
						--l;
						++vec32.xCoord;
					}

					i1 = (int)(vec32.yCoord = MathHelper.floor_double(par1Vec3.yCoord));

					if(b0 == 1) {
						--i1;
						++vec32.yCoord;
					}

					j1 = (int)(vec32.zCoord = MathHelper.floor_double(par1Vec3.zCoord));

					if(b0 == 3) {
						--j1;
						++vec32.zCoord;
					}

					Block block1 = worldObj.getBlock(l, i1, j1);
					int j2 = worldObj.getBlockMetadata(l, i1, j1);

					if((!par4 || block1 == null || block1.getCollisionBoundingBoxFromPool(worldObj, l, i1, j1) != null) && block1 != Blocks.air && block1.canCollideCheck(j2, par3)) {
						MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(worldObj, l, i1, j1, par1Vec3, par2Vec3);

						if(movingobjectposition1 != null)
							return movingobjectposition1;
					}
				}

				return null;
			}
			else return null;
		} else return null;
	}
	
	public void setMotion(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}
	
	@Override
	public void onUpdate() {
		superUpdate();
		
		if(ticksExisted >= effectThreshhold && worldObj.isRemote)
			Botania.proxy.wispFX(worldObj, posX, posY, posZ, 0.3F, 0.09F, 1F, 1F, -0.1F, 0.18F);
			
		if(ticksExisted >= length)
			this.setDead();
		
		ticksExisted++;
		
		if (!isDead)
			onUpdate();
	}

	// I can't do vector physics -Phanta
	@Override
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		if(par1MovingObjectPosition != null) {
			if(par1MovingObjectPosition.entityHit == null) {
				Vector3 current = new Vector3(this.motionX, this.motionY, this.motionZ);
				ForgeDirection faceHit = ForgeDirection.getOrientation(par1MovingObjectPosition.sideHit);
				Vector3 faceVec = new Vector3(faceHit.offsetX, faceHit.offsetY, faceHit.offsetZ).normalize();
				Vector3 newMomentum = faceVec.multiply(-2 * current.dotProduct(faceVec)).add(current);
				setMotion(newMomentum.x, newMomentum.y, newMomentum.z);
			} else {
				if(par1MovingObjectPosition.entityHit instanceof EntityLivingBase && !par1MovingObjectPosition.entityHit.equals(shooter))
					((EntityLivingBase)par1MovingObjectPosition.entityHit).attackEntityFrom(DamageSource.causeIndirectMagicDamage(par1MovingObjectPosition.entityHit, shooter), 8F);
			}
		}
	}
	
}
