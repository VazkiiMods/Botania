/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Predicate;

public class EntityMagicMissile extends ThrowableEntity {
	private static final String TAG_TIME = "time";
	private static final DataParameter<Boolean> EVIL = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMagicMissile.class, DataSerializers.VARINT);

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(EntityType<EntityMagicMissile> type, World world) {
		super(type, world);
	}

	public EntityMagicMissile(LivingEntity thrower, boolean evil) {
		super(ModEntities.MAGIC_MISSILE, thrower, thrower.world);
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
		if (e instanceof LivingEntity) {
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
			setMotion(motionVec.toVector3d());
			if (time < 10) {
				setMotion(getMotion().getX(), Math.abs(getMotion().getY()), getMotion().getZ());
			}

			List<LivingEntity> targetList = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(getPosX() - 0.5, getPosY() - 0.5, getPosZ() - 0.5, getPosX() + 0.5, getPosY() + 0.5, getPosZ() + 0.5));
			if (targetList.contains(target)) {
				Entity thrower = func_234616_v_();
				if (thrower instanceof LivingEntity) {
					PlayerEntity player = thrower instanceof PlayerEntity ? (PlayerEntity) thrower : null;
					target.attackEntityFrom(player == null ? DamageSource.causeMobDamage((LivingEntity) thrower) : DamageSource.causePlayerDamage(player), evil ? 12 : 7);
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
		if (target != null) {
			if (target.isAlive()) {
				return true;
			} else {
				target = null;
				setTarget(null);
			}
		}

		double range = 12;
		AxisAlignedBB bounds = new AxisAlignedBB(getPosX() - range, getPosY() - range, getPosZ() - range, getPosX() + range, getPosY() + range, getPosZ() + range);
		List<LivingEntity> entities;
		if (isEvil()) {
			entities = world.getEntitiesWithinAABB(PlayerEntity.class, bounds);
		} else {
			Predicate<LivingEntity> pred = EntityPredicates.IS_LIVING_ALIVE.and(this::shouldTarget);
			entities = world.getEntitiesWithinAABB(LivingEntity.class, bounds, pred);
		}

		if (entities.size() > 0) {
			target = entities.get(world.rand.nextInt(entities.size()));
			setTarget(target);
		}

		return target != null;
	}

	public boolean shouldTarget(LivingEntity e) {
		// always defend yourself
		Entity thrower = func_234616_v_();
		if (e instanceof MobEntity && isHostile(thrower, ((MobEntity) e).getAttackTarget())) {
			return true;
		}
		// don't target tamed creatures...
		if (e instanceof TameableEntity && ((TameableEntity) e).isTamed() || e instanceof AbstractHorseEntity && ((AbstractHorseEntity) e).isTame()) {
			return false;
		}

		// ...but other mobs die
		return e instanceof IMob;
	}

	public static boolean isHostile(Entity thrower, Entity attackTarget) {
		// if the thrower can attack the target thru PvP...
		if (thrower instanceof PlayerEntity && attackTarget instanceof PlayerEntity && ((PlayerEntity) thrower).canAttackPlayer((PlayerEntity) attackTarget)) {
			// ... then only defend self
			return thrower == attackTarget;
		}
		// otherwise, kill any player-hostiles
		return attackTarget instanceof PlayerEntity;
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
