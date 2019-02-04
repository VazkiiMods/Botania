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

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityMagicMissile extends EntityThrowable {
	@ObjectHolder(LibMisc.MOD_ID + ":magic_missile")
	public static EntityType<?> TYPE;

	private static final String TAG_TIME = "time";
	private static final DataParameter<Boolean> EVIL = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.VARINT);

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(World world) {
		super(TYPE, world);
		setSize(0F, 0F);
	}

	public EntityMagicMissile(EntityLivingBase thrower, boolean evil) {
		super(TYPE, thrower, thrower.world);
		setSize(0F, 0F);
		setEvil(evil);
	}

	@Override
	protected void registerData() {
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
	public void tick() {
		double lastTickPosX = this.lastTickPosX;
		double lastTickPosY = this.lastTickPosY;
		double lastTickPosZ = this.lastTickPosZ;

		super.tick();

		if(!world.isRemote && (!findTarget() || time > 40)) {
			remove();
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

				remove();
			}

			if(evil && diffVec.mag() < 1)
				remove();
		}

		time++;
	}

	@Override
	public void writeAdditional(NBTTagCompound cmp) {
		super.writeAdditional(cmp);
		cmp.setInt(TAG_TIME, time);
	}

	@Override
	public void readAdditional(NBTTagCompound cmp) {
		super.readAdditional(cmp);
		time = cmp.getInt(TAG_TIME);
	}


	public boolean findTarget() {
		EntityLivingBase target = getTargetEntity();
		if(target != null && target.getHealth() > 0 && !target.removed && world.loadedEntityList.contains(target))
			return true;
		if(target != null)
			setTarget(null);

		double range = 12;
		AxisAlignedBB bounds = new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range);
		List entities;
		if(isEvil()) {
			entities = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
		} else {
			entities = world.getEntitiesWithinAABB(Entity.class, bounds, Predicates.instanceOf(IMob.class));
		}
		while(entities.size() > 0) {
			Entity e = (Entity) entities.get(world.rand.nextInt(entities.size()));
			if(!(e instanceof EntityLivingBase) || e.removed) { // Just in case...
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
	protected void onImpact(@Nonnull RayTraceResult pos) {
		switch (pos.type) {
		case BLOCK: {
			Block block = world.getBlockState(pos.getBlockPos()).getBlock();
			if(!(block instanceof BlockBush) && !(block instanceof BlockLeaves))
				remove();
			break;
		}
		case ENTITY: {
			if (pos.entity == getTargetEntity())
				remove();
			break;
		}
		default: {
			remove();
			break;
		}
		}
	}

}
