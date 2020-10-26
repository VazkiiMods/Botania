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
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Predicate;

public class EntityMagicMissile extends ThrownEntity {
	private static final String TAG_TIME = "time";
	private static final TrackedData<Boolean> EVIL = DataTracker.registerData(EntityMagicMissile.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> TARGET = DataTracker.registerData(EntityMagicMissile.class, TrackedDataHandlerRegistry.INTEGER);

	double lockX, lockY = -1, lockZ;
	int time = 0;

	public EntityMagicMissile(EntityType<EntityMagicMissile> type, World world) {
		super(type, world);
	}

	public EntityMagicMissile(LivingEntity owner, boolean evil) {
		super(ModEntities.MAGIC_MISSILE, owner, owner.world);
		setEvil(evil);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(EVIL, false);
		dataTracker.startTracking(TARGET, 0);
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return PacketSpawnEntity.make(this);
	}

	public void setEvil(boolean evil) {
		dataTracker.set(EVIL, evil);
	}

	public boolean isEvil() {
		return dataTracker.get(EVIL);
	}

	public void setTarget(LivingEntity e) {
		dataTracker.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	public LivingEntity getTargetEntity() {
		int id = dataTracker.get(TARGET);
		Entity e = world.getEntityById(id);
		if (e instanceof LivingEntity) {
			return (LivingEntity) e;
		}

		return null;
	}

	@Override
	public void tick() {
		double lastTickPosX = this.lastRenderX;
		double lastTickPosY = this.lastRenderY;
		double lastTickPosZ = this.lastRenderZ;

		super.tick();

		if (!world.isClient && (!findTarget() || time > 40)) {
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

			if (world.random.nextInt(steps) <= 1) {
				world.addParticle(data, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 0, 0, 0);
			}

			particlePos = particlePos.add(step);
		}

		LivingEntity target = getTargetEntity();
		if (target != null) {
			if (lockY == -1) {
				lockX = target.getX();
				lockY = target.getY();
				lockZ = target.getZ();
			}

			Vector3 targetVec = evil ? new Vector3(lockX, lockY, lockZ) : Vector3.fromEntityCenter(target);
			Vector3 diffVec = targetVec.subtract(thisVec);
			Vector3 motionVec = diffVec.normalize().multiply(evil ? 0.5 : 0.6);
			setVelocity(motionVec.toVector3d());
			if (time < 10) {
				setVelocity(getVelocity().getX(), Math.abs(getVelocity().getY()), getVelocity().getZ());
			}

			List<LivingEntity> targetList = world.getNonSpectatingEntities(LivingEntity.class, new Box(getX() - 0.5, getY() - 0.5, getZ() - 0.5, getX() + 0.5, getY() + 0.5, getZ() + 0.5));
			if (targetList.contains(target)) {
				Entity owner = getOwner();
				if (owner instanceof LivingEntity) {
					PlayerEntity player = owner instanceof PlayerEntity ? (PlayerEntity) owner : null;
					target.damage(player == null ? DamageSource.mob((LivingEntity) owner) : DamageSource.player(player), evil ? 12 : 7);
				} else {
					target.damage(DamageSource.GENERIC, evil ? 12 : 7);
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
	public void writeCustomDataToTag(CompoundTag cmp) {
		super.writeCustomDataToTag(cmp);
		cmp.putInt(TAG_TIME, time);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag cmp) {
		super.readCustomDataFromTag(cmp);
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
		Box bounds = new Box(getX() - range, getY() - range, getZ() - range, getX() + range, getY() + range, getZ() + range);
		List<LivingEntity> entities;
		if (isEvil()) {
			entities = world.getNonSpectatingEntities(PlayerEntity.class, bounds);
		} else {
			Entity owner = getOwner();
			Predicate<LivingEntity> pred = EntityPredicates.VALID_LIVING_ENTITY.and(targetPredicate(owner));
			entities = world.getEntitiesByClass(LivingEntity.class, bounds, pred);
		}

		if (entities.size() > 0) {
			target = entities.get(world.random.nextInt(entities.size()));
			setTarget(target);
		}

		return target != null;
	}

	public static Predicate<LivingEntity> targetPredicate(Entity owner) {
		return target -> shouldTarget(owner, target);
	}

	public static boolean shouldTarget(Entity owner, LivingEntity e) {
		// always defend yourself
		if (e instanceof MobEntity && isHostile(owner, ((MobEntity) e).getTarget())) {
			return true;
		}
		// don't target tamed creatures...
		if (e instanceof TameableEntity && ((TameableEntity) e).isTamed() || e instanceof HorseBaseEntity && ((HorseBaseEntity) e).isTame()) {
			return false;
		}

		// ...but other mobs die
		return e instanceof Monster;
	}

	public static boolean isHostile(Entity owner, Entity attackTarget) {
		// if the owner can attack the target thru PvP...
		if (owner instanceof PlayerEntity && attackTarget instanceof PlayerEntity && ((PlayerEntity) owner).shouldDamagePlayer((PlayerEntity) attackTarget)) {
			// ... then only defend self
			return owner == attackTarget;
		}
		// otherwise, kill any player-hostiles
		return attackTarget instanceof PlayerEntity;
	}

	@Override
	protected void onCollision(@Nonnull HitResult pos) {
		switch (pos.getType()) {
		case BLOCK: {
			Block block = world.getBlockState(((BlockHitResult) pos).getBlockPos()).getBlock();
			if (!(block instanceof PlantBlock) && !(block instanceof LeavesBlock)) {
				remove();
			}
			break;
		}
		case ENTITY: {
			if (((EntityHitResult) pos).getEntity() == getTargetEntity()) {
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
