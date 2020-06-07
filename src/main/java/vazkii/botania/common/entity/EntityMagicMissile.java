/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityMagicMissile extends ThrowableEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":magic_missile") public static EntityType<EntityMagicMissile> TYPE;

	private static final String TAG_TIME = "time";
	private static final DataParameter<Boolean> EVIL = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.VARINT);

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(EntityType<EntityMagicMissile> type, World world) {
		super(type, world);
	}

	public EntityMagicMissile(World world) {
		this(TYPE, world);
	}

	public EntityMagicMissile(LivingEntity thrower, boolean evil) {
		super(TYPE, thrower, thrower.world);
		setEvil(evil);
	}

	@Override
	protected void registerData() {
		dataManager.register(EVIL, false);
		dataManager.register(TARGET, 0);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public void setEvil(boolean evil) {
		dataManager.set(EVIL, evil);
	}

	public boolean isEvil() {
		return dataManager.get(EVIL);
	}

	public void setTarget(LivingEntity e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	public LivingEntity getTargetEntity() {
		int id = dataManager.get(TARGET);
		Entity e = world.getEntityByID(id);
		if (e != null && e instanceof LivingEntity) {
			return (LivingEntity) e;
		}

		return null;
	}

	@Override
	public void tick() {
		double lastTickPosX = this.lastTickPosX;
		double lastTickPosY = this.lastTickPosY;
		double lastTickPosZ = this.lastTickPosZ;

		super.tick();

		if (!world.isRemote && (!findTarget() || time > 40)) {
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

		SparkleParticleData data = evil ? SparkleParticleData.corrupt(0.8F, 1F, 0.0F, 1F, 2)
				: SparkleParticleData.sparkle(0.8F, 1F, 0.4F, 1F, 2);
		for (int i = 0; i < steps; i++) {
			world.addParticle(data, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);

			if (world.rand.nextInt(steps) <= 1) {
				world.addParticle(data, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 0, 0, 0);
			}

			particlePos = particlePos.add(step);
		}

		LivingEntity target = getTargetEntity();
		if (target != null) {
			if (lockY == -1) {
				lockX = target.getPosX();
				lockY = target.getPosY();
				lockZ = target.getPosZ();
			}

			Vector3 targetVec = evil ? new Vector3(lockX, lockY, lockZ) : Vector3.fromEntityCenter(target);
			Vector3 diffVec = targetVec.subtract(thisVec);
			Vector3 motionVec = diffVec.normalize().multiply(evil ? 0.5 : 0.6);
			setMotion(motionVec.toVec3D());
			if (time < 10) {
				setMotion(getMotion().getX(), Math.abs(getMotion().getY()), getMotion().getZ());
			}

			List<LivingEntity> targetList = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(getPosX() - 0.5, getPosY() - 0.5, getPosZ() - 0.5, getPosX() + 0.5, getPosY() + 0.5, getPosZ() + 0.5));
			if (targetList.contains(target)) {
				LivingEntity thrower = getThrower();
				if (thrower != null) {
					PlayerEntity player = thrower instanceof PlayerEntity ? (PlayerEntity) thrower : null;
					target.attackEntityFrom(player == null ? DamageSource.causeMobDamage(thrower) : DamageSource.causePlayerDamage(player), evil ? 12 : 7);
				} else {
					target.attackEntityFrom(DamageSource.GENERIC, evil ? 12 : 7);
				}

				remove();
			}

			if (evil && diffVec.mag() < 1) {
				remove();
			}
		}

		time++;
	}

	@Override
	public void writeAdditional(CompoundNBT cmp) {
		super.writeAdditional(cmp);
		cmp.putInt(TAG_TIME, time);
	}

	@Override
	public void readAdditional(CompoundNBT cmp) {
		super.readAdditional(cmp);
		time = cmp.getInt(TAG_TIME);
	}

	public boolean findTarget() {
		LivingEntity target = getTargetEntity();
		if (target != null && target.isAlive()) {
			return true;
		}
		if (target != null) {
			setTarget(null);
		}

		double range = 12;
		AxisAlignedBB bounds = new AxisAlignedBB(getPosX() - range, getPosY() - range, getPosZ() - range, getPosX() + range, getPosY() + range, getPosZ() + range);
		List<Entity> entities;
		if (isEvil()) {
			entities = world.getEntitiesWithinAABB(PlayerEntity.class, bounds);
		} else {
			entities = world.getEntitiesWithinAABB(Entity.class, bounds, Predicates.instanceOf(IMob.class));
		}
		while (entities.size() > 0) {
			Entity e = entities.get(world.rand.nextInt(entities.size()));
			if (!(e instanceof LivingEntity) || !e.isAlive()) { // Just in case...
				entities.remove(e);
				continue;
			}

			target = (LivingEntity) e;
			setTarget(target);
			break;
		}

		return target != null;
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		switch (pos.getType()) {
		case BLOCK: {
			Block block = world.getBlockState(((BlockRayTraceResult) pos).getPos()).getBlock();
			if (!(block instanceof BushBlock) && !(block instanceof LeavesBlock)) {
				remove();
			}
			break;
		}
		case ENTITY: {
			if (((EntityRayTraceResult) pos).getEntity() == getTargetEntity()) {
				remove();
			}
			break;
		}
		default: {
			remove();
			break;
		}
		}
	}

}
