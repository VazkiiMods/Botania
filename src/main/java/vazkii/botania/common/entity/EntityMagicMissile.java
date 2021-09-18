/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Predicate;

public class EntityMagicMissile extends ThrowableProjectile {
	private static final String TAG_TIME = "time";
	private static final EntityDataAccessor<Boolean> EVIL = SynchedEntityData.defineId(EntityMagicMissile.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(EntityMagicMissile.class, EntityDataSerializers.INT);

	double lockX, lockY = Integer.MIN_VALUE, lockZ;
	int time = 0;

	public EntityMagicMissile(EntityType<EntityMagicMissile> type, Level world) {
		super(type, world);
	}

	public EntityMagicMissile(LivingEntity owner, boolean evil) {
		super(ModEntities.MAGIC_MISSILE, owner, owner.level);
		setEvil(evil);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(EVIL, false);
		entityData.define(TARGET, 0);
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	public void setEvil(boolean evil) {
		entityData.set(EVIL, evil);
	}

	public boolean isEvil() {
		return entityData.get(EVIL);
	}

	public void setTarget(LivingEntity e) {
		entityData.set(TARGET, e == null ? -1 : e.getId());
	}

	public LivingEntity getTargetEntity() {
		int id = entityData.get(TARGET);
		Entity e = level.getEntity(id);
		if (e instanceof LivingEntity) {
			return (LivingEntity) e;
		}

		return null;
	}

	@Override
	public void tick() {
		double lastTickPosX = this.xOld;
		double lastTickPosY = this.yOld;
		double lastTickPosZ = this.zOld;

		super.tick();

		if (!level.isClientSide && (!findTarget() || time > 40)) {
			discard();
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
			level.addParticle(data, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);

			if (level.random.nextInt(steps) <= 1) {
				level.addParticle(data, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 0, 0, 0);
			}

			particlePos = particlePos.add(step);
		}

		LivingEntity target = getTargetEntity();
		if (target != null) {
			if (lockY == Integer.MIN_VALUE) {
				lockX = target.getX();
				lockY = target.getY();
				lockZ = target.getZ();
			}

			Vector3 targetVec = evil ? new Vector3(lockX, lockY, lockZ) : Vector3.fromEntityCenter(target);
			Vector3 diffVec = targetVec.subtract(thisVec);
			Vector3 motionVec = diffVec.normalize().multiply(evil ? 0.5 : 0.6);
			setDeltaMovement(motionVec.toVector3d());
			if (time < 10) {
				setDeltaMovement(getDeltaMovement().x(), Math.abs(getDeltaMovement().y()), getDeltaMovement().z());
			}

			List<LivingEntity> targetList = level.getEntitiesOfClass(LivingEntity.class, new AABB(getX() - 0.5, getY() - 0.5, getZ() - 0.5, getX() + 0.5, getY() + 0.5, getZ() + 0.5));
			if (targetList.contains(target)) {
				Entity owner = getOwner();
				if (owner instanceof LivingEntity) {
					Player player = owner instanceof Player ? (Player) owner : null;
					target.hurt(player == null ? DamageSource.mobAttack((LivingEntity) owner) : DamageSource.playerAttack(player), evil ? 12 : 7);
				} else {
					target.hurt(DamageSource.GENERIC, evil ? 12 : 7);
				}

				discard();
			}

			if (evil && diffVec.mag() < 1) {
				discard();
			}
		}

		time++;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag cmp) {
		super.addAdditionalSaveData(cmp);
		cmp.putInt(TAG_TIME, time);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag cmp) {
		super.readAdditionalSaveData(cmp);
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
		AABB bounds = new AABB(getX() - range, getY() - range, getZ() - range, getX() + range, getY() + range, getZ() + range);
		List<? extends LivingEntity> entities;
		if (isEvil()) {
			entities = level.getEntitiesOfClass(Player.class, bounds);
		} else {
			Entity owner = getOwner();
			Predicate<Entity> pred = EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(targetPredicate(owner));
			entities = level.getEntitiesOfClass(LivingEntity.class, bounds, pred);
		}

		if (entities.size() > 0) {
			target = entities.get(level.random.nextInt(entities.size()));
			setTarget(target);
		}

		return target != null;
	}

	public static Predicate<Entity> targetPredicate(Entity owner) {
		return target -> target instanceof LivingEntity living && shouldTarget(owner, living);
	}

	public static boolean shouldTarget(Entity owner, LivingEntity e) {
		// always defend yourself
		if (e instanceof Mob && isHostile(owner, ((Mob) e).getTarget())) {
			return true;
		}
		// don't target tamed creatures...
		if (e instanceof TamableAnimal && ((TamableAnimal) e).isTame() || e instanceof AbstractHorse && ((AbstractHorse) e).isTamed()) {
			return false;
		}

		// ...but other mobs die
		return e instanceof Enemy;
	}

	public static boolean isHostile(Entity owner, Entity attackTarget) {
		// if the owner can attack the target thru PvP...
		if (owner instanceof Player && attackTarget instanceof Player && ((Player) owner).canHarmPlayer((Player) attackTarget)) {
			// ... then only defend self
			return owner == attackTarget;
		}
		// otherwise, kill any player-hostiles
		return attackTarget instanceof Player;
	}

	@Override
	protected void onHit(@Nonnull HitResult pos) {
		switch (pos.getType()) {
		case BLOCK: {
			Block block = level.getBlockState(((BlockHitResult) pos).getBlockPos()).getBlock();
			if (!(block instanceof BushBlock) && !(block instanceof LeavesBlock)) {
				discard();
			}
			break;
		}
		case ENTITY: {
			if (((EntityHitResult) pos).getEntity() == getTargetEntity()) {
				discard();
			}
			break;
		}
		default: {
			discard();
			break;
		}
		}
	}

}
