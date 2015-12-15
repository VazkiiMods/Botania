/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 24, 2014, 5:58:22 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class EntityMagicMissile extends EntityThrowable {

	private static final String TAG_TIME = "time";

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(World world) {
		super(world);
		setSize(0F, 0F);
	}

	public EntityMagicMissile(EntityLivingBase thrower, boolean evil) {
		this(thrower.worldObj);
		ReflectionHelper.setPrivateValue(EntityThrowable.class, this, thrower, LibObfuscation.THROWER);
		setEvil(evil);
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(25, (byte) 0);
		dataWatcher.addObject(26, 0);
	}

	public void setEvil(boolean evil) {
		dataWatcher.updateObject(25, (byte) (evil ? 1 : 0));
	}

	public boolean isEvil() {
		return dataWatcher.getWatchableObjectByte(25) == 1;
	}

	public void setTarget(EntityLivingBase e) {
		dataWatcher.updateObject(26, e == null ? -1 : e.getEntityId());
	}

	public EntityLivingBase getTargetEntity() {
		int id = dataWatcher.getWatchableObjectInt(26);
		Entity e = worldObj.getEntityByID(id);
		if(e != null && e instanceof EntityLivingBase)
			return (EntityLivingBase) e;

		return null;
	}

	@Override
	public void onUpdate() {
		double lastTickPosX = this.lastTickPosX;
		double lastTickPosY = this.lastTickPosY;
		double lastTickPosZ = this.lastTickPosZ;

		super.onUpdate();

		if(!worldObj.isRemote && (!getTarget() || time > 40)) {
			setDead();
			return;
		}

		boolean evil = isEvil();
		Vector3 thisVec = Vector3.fromEntityCenter(this);
		Vector3 oldPos = new Vector3(lastTickPosX, lastTickPosY, lastTickPosZ);
		Vector3 diff = thisVec.copy().sub(oldPos);
		Vector3 step = diff.copy().normalize().multiply(0.05);
		int steps = (int) (diff.mag() / step.mag());
		Vector3 particlePos = oldPos.copy();

		Botania.proxy.setSparkleFXCorrupt(evil);
		for(int i = 0; i < steps; i++) {
			Botania.proxy.sparkleFX(worldObj, particlePos.x, particlePos.y, particlePos.z, 1F, evil ? 0F : 0.4F, 1F, 0.8F, 2);
			if(worldObj.rand.nextInt(steps) <= 1)
				Botania.proxy.sparkleFX(worldObj, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 1F, evil ? 0F : 0.4F, 1F, 0.8F, 2);

			particlePos.add(step);
		}
		Botania.proxy.setSparkleFXCorrupt(false);

		EntityLivingBase target = getTargetEntity();
		if(target != null) {
			if(lockY == -1) {
				lockX = target.posX;
				lockY = target.posY;
				lockZ = target.posZ;
			}

			Vector3 targetVec = evil ? new Vector3(lockX, lockY, lockZ) : Vector3.fromEntityCenter(target);
			Vector3 diffVec = targetVec.copy().sub(thisVec);
			Vector3 motionVec = diffVec.copy().normalize().multiply(evil ? 0.5 : 0.6);
			motionX = motionVec.x;
			motionY = motionVec.y;
			if(time < 10)
				motionY = Math.abs(motionY);
			motionZ = motionVec.z;

			List<EntityLivingBase> targetList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5));
			if(targetList.contains(target) && target != null) {
				EntityLivingBase thrower = getThrower();
				if(thrower != null) {
					EntityPlayer player = thrower instanceof EntityPlayer ? (EntityPlayer) thrower : null;
					target.attackEntityFrom(player == null ? DamageSource.causeMobDamage(thrower) : DamageSource.causePlayerDamage(player), evil ? 12 : 7);
				} else target.attackEntityFrom(DamageSource.generic, evil ? 12 : 7);
				
				setDead();
			}

			if(evil && diffVec.mag() < 1)
				setDead();
		}

		time++;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound cmp) {
		super.writeEntityToNBT(cmp);
		cmp.setInteger(TAG_TIME, time);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound cmp) {
		super.readEntityFromNBT(cmp);
		time = cmp.getInteger(TAG_TIME);
	}


	public boolean getTarget() {
		EntityLivingBase target = getTargetEntity();
		if(target != null && target.getHealth() > 0 && !target.isDead && worldObj.loadedEntityList.contains(target))
			return true;
		if(target != null)
			setTarget(null);

		double range = 12;
		List entities = worldObj.getEntitiesWithinAABB(isEvil() ? EntityPlayer.class : IMob.class, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
		while(entities.size() > 0) {
			Entity e = (Entity) entities.get(worldObj.rand.nextInt(entities.size()));
			if(!(e instanceof EntityLivingBase) || e.isDead) { // Just in case...
				entities.remove(e);
				continue;
			}

			target = (EntityLivingBase) e;
			setTarget(target);
			break;
		}

		return target != null;
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		Block block = worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);

		if(!(block instanceof BlockBush) && !(block instanceof BlockLeaves) && (pos.entityHit == null || getTargetEntity() == pos.entityHit))
			setDead();
	}

}
