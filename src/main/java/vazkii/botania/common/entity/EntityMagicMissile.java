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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityMagicMissile extends EntityThrowable {

	private static final String TAG_TIME = "time";
	private static final DataParameter<Boolean> EVIL = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.VARINT);

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(World world) {
		super(world);
		setSize(0F, 0F);
	}

	public EntityMagicMissile(EntityLivingBase thrower, boolean evil) {
		super(thrower.world, thrower);
		setSize(0F, 0F);
		setEvil(evil);
	}

	@Override
	protected void entityInit() {
		dataManager.register(EVIL, false);
		dataManager.register(TARGET, 0);
	}

	public void setEvil(boolean evil) {
		dataManager.set(EVIL, evil);
	}

	public boolean isEvil() {
		return dataManager.get(EVIL);
	}

	public void setTarget(EntityLivingBase e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	public EntityLivingBase getTargetEntity() {
		int id = dataManager.get(TARGET);
		Entity e = world.getEntityByID(id);
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

		if(!world.isRemote && (!findTarget() || time > 40)) {
			setDead();
			return;
		}

		boolean evil = isEvil();
		Vector3 thisVec = Vector3.fromEntityCenter(this);
		Vector3 oldPos = new Vector3(lastTickPosX, lastTickPosY, lastTickPosZ);
		Vector3 diff = thisVec.subtract(oldPos);
		Vector3 step = diff.normalize().multiply(0.05);
		int steps = (int) (diff.mag() / step.mag());
		Vector3 particlePos = oldPos;

		Botania.proxy.setSparkleFXCorrupt(evil);
		for(int i = 0; i < steps; i++) {
			Botania.proxy.sparkleFX(particlePos.x, particlePos.y, particlePos.z, 1F, evil ? 0F : 0.4F, 1F, 0.8F, 2);
			if(world.rand.nextInt(steps) <= 1)
				Botania.proxy.sparkleFX(particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 1F, evil ? 0F : 0.4F, 1F, 0.8F, 2);

			particlePos = particlePos.add(step);
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
			Vector3 diffVec = targetVec.subtract(thisVec);
			Vector3 motionVec = diffVec.normalize().multiply(evil ? 0.5 : 0.6);
			motionX = motionVec.x;
			motionY = motionVec.y;
			if(time < 10)
				motionY = Math.abs(motionY);
			motionZ = motionVec.z;

			List<EntityLivingBase> targetList = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5));
			if(targetList.contains(target)) {
				EntityLivingBase thrower = getThrower();
				if(thrower != null) {
					EntityPlayer player = thrower instanceof EntityPlayer ? (EntityPlayer) thrower : null;
					target.attackEntityFrom(player == null ? DamageSource.causeMobDamage(thrower) : DamageSource.causePlayerDamage(player), evil ? 12 : 7);
				} else target.attackEntityFrom(DamageSource.GENERIC, evil ? 12 : 7);

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


	public boolean findTarget() {
		EntityLivingBase target = getTargetEntity();
		if(target != null && target.getHealth() > 0 && !target.isDead && world.loadedEntityList.contains(target))
			return true;
		if(target != null) {
			target = null;
			setTarget(null);
		}

		double range = 12;
		AxisAlignedBB bounds = new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range);
		List<EntityLivingBase> entities;
		if(isEvil()) {
			entities = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
		} else {
			entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds, targetPredicate(getThrower()));
		}
		if (entities.size() > 0) {
			target = entities.get(world.rand.nextInt(entities.size()));
			setTarget(target);
		}

		return target != null;
	}

	public static Predicate<EntityLivingBase> targetPredicate(Entity owner) {
		return target -> shouldTarget(owner, target);
	}

	public static boolean shouldTarget(Entity owner, EntityLivingBase e) {
		if (!e.isEntityAlive()) {
			return false;
		}
		// always defend yourself
		if (e instanceof EntityMob && isHostile(owner, ((EntityMob) e).getAttackTarget())) {
			return true;
		}
		// don't target tamed creatures...
		if (e instanceof EntityTameable && ((EntityTameable) e).isTamed() || e instanceof AbstractHorse && ((AbstractHorse) e).isTame()) {
			return false;
		}

		// ...but other mobs die
		return e instanceof IMob;
	}

	public static boolean isHostile(Entity owner, Entity attackTarget) {
		// if the owner can attack the target thru PvP...
		if (owner instanceof EntityPlayer && attackTarget instanceof EntityPlayer && ((EntityPlayer) owner).canAttackPlayer((EntityPlayer) attackTarget)) {
			// ... then only defend self
			return owner == attackTarget;
		}
		// otherwise, kill any player-hostiles
		return attackTarget instanceof EntityPlayer;
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		switch (pos.typeOfHit) {
		case BLOCK: {
			Block block = world.getBlockState(pos.getBlockPos()).getBlock();
			if(!(block instanceof BlockBush) && !(block instanceof BlockLeaves))
				setDead();
			break;
		}
		case ENTITY: {
			if (pos.entityHit == getTargetEntity())
				setDead();
			break;
		}
		default: {
			setDead();
			break;
		}
		}
	}

}
