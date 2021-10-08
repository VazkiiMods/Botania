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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.ItemKingKey;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityBabylonWeapon extends EntityThrowableCopy {
	private static final String TAG_CHARGING = "charging";
	private static final String TAG_VARIETY = "variety";
	private static final String TAG_CHARGE_TICKS = "chargeTicks";
	private static final String TAG_LIVE_TICKS = "liveTicks";
	private static final String TAG_DELAY = "delay";
	private static final String TAG_ROTATION = "rotation";

	private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> VARIETY = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CHARGE_TICKS = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> LIVE_TICKS = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(EntityBabylonWeapon.class, EntityDataSerializers.FLOAT);

	public EntityBabylonWeapon(EntityType<EntityBabylonWeapon> type, Level world) {
		super(type, world);
	}

	public EntityBabylonWeapon(LivingEntity thrower, Level world) {
		super(ModEntities.BABYLON_WEAPON, thrower, world);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(CHARGING, false);
		entityData.define(VARIETY, 0);
		entityData.define(CHARGE_TICKS, 0);
		entityData.define(LIVE_TICKS, 0);
		entityData.define(DELAY, 0);
		entityData.define(ROTATION, 0F);
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 64 * 64;
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	public void tick() {
		Entity thrower = getOwner();
		if (!level.isClientSide && (thrower == null || !(thrower instanceof Player) || !thrower.isAlive())) {
			discard();
			return;
		}
		Player player = (Player) thrower;
		if (!level.isClientSide) {
			ItemStack stack = PlayerHelper.getFirstHeldItem(player, ModItems.kingKey);
			boolean newCharging = !stack.isEmpty() && ItemKingKey.isCharging(stack);
			if (isCharging() != newCharging) {
				setCharging(newCharging);
			}
		}

		Vec3 mot = getDeltaMovement();

		int liveTime = getLiveTicks();
		int delay = getDelay();
		boolean charging = isCharging() && liveTime == 0;

		if (charging) {
			setDeltaMovement(Vec3.ZERO);

			int chargeTime = getChargeTicks();
			setChargeTicks(chargeTime + 1);

			if (level.random.nextInt(20) == 0) {
				level.playSound(null, getX(), getY(), getZ(), ModSounds.babylonSpawn, SoundSource.PLAYERS, 0.1F, 1F + level.random.nextFloat() * 3F);
			}
		} else {
			if (liveTime < delay) {
				setDeltaMovement(Vec3.ZERO);
			} else if (liveTime == delay && player != null) {
				Vec3 playerLook;
				BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 64, true);
				if (rtr.getType() != HitResult.Type.BLOCK) {
					playerLook = player.getLookAngle().scale(64).add(player.position());
				} else {
					playerLook = Vec3.atCenterOf(rtr.getBlockPos());
				}

				Vec3 thisVec = Vector3.fromEntityCenter(this);

				mot = playerLook.subtract(thisVec.x, thisVec.y, thisVec.z).normalize().scale(2);
				level.playSound(null, getX(), getY(), getZ(), ModSounds.babylonAttack, SoundSource.PLAYERS, 2F, 0.1F + level.random.nextFloat() * 3F);
			}

			if (!level.isClientSide) {
				setLiveTicks(liveTime + 1);
				AABB axis = new AABB(getX(), getY(), getZ(), xOld, yOld, zOld).inflate(2);
				List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, axis);
				for (LivingEntity living : entities) {
					if (living == thrower) {
						continue;
					}

					if (living.hurtTime == 0) {
						if (player != null) {
							living.hurt(DamageSource.playerAttack(player), 20);
						} else {
							living.hurt(DamageSource.GENERIC, 20);
						}
						onHit(new EntityHitResult(living));
						return;
					}
				}
			}
		}

		super.tick();

		// Apply after super tick so drag is not applied by super
		setDeltaMovement(mot);

		if (level.isClientSide && liveTime > delay) {
			WispParticleData data = WispParticleData.wisp(0.3F, 1F, 1F, 0F, 1);
			level.addParticle(data, getX(), getY(), getZ(), 0, -0F, 0);
		}

		if (!level.isClientSide && liveTime > 200 + delay) {
			discard();
		}
	}

	@Override
	protected void onHit(HitResult pos) {
		Entity thrower = getOwner();
		if (pos.getType() != HitResult.Type.ENTITY || ((EntityHitResult) pos).getEntity() != thrower) {
			level.explode(this, getX(), getY(), getZ(), 3F, Explosion.BlockInteraction.NONE);
			discard();
		}
	}

	@Override
	public void addAdditionalSaveData(@Nonnull CompoundTag cmp) {
		super.addAdditionalSaveData(cmp);
		cmp.putBoolean(TAG_CHARGING, isCharging());
		cmp.putInt(TAG_VARIETY, getVariety());
		cmp.putInt(TAG_CHARGE_TICKS, getChargeTicks());
		cmp.putInt(TAG_LIVE_TICKS, getLiveTicks());
		cmp.putInt(TAG_DELAY, getDelay());
		cmp.putFloat(TAG_ROTATION, getRotation());
	}

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundTag cmp) {
		super.readAdditionalSaveData(cmp);
		setCharging(cmp.getBoolean(TAG_CHARGING));
		setVariety(cmp.getInt(TAG_VARIETY));
		setChargeTicks(cmp.getInt(TAG_CHARGE_TICKS));
		setLiveTicks(cmp.getInt(TAG_LIVE_TICKS));
		setDelay(cmp.getInt(TAG_DELAY));
		setRotation(cmp.getFloat(TAG_ROTATION));
	}

	public boolean isCharging() {
		return entityData.get(CHARGING);
	}

	public void setCharging(boolean charging) {
		entityData.set(CHARGING, charging);
	}

	public int getVariety() {
		return entityData.get(VARIETY);
	}

	public void setVariety(int var) {
		entityData.set(VARIETY, var);
	}

	public int getChargeTicks() {
		return entityData.get(CHARGE_TICKS);
	}

	public void setChargeTicks(int ticks) {
		entityData.set(CHARGE_TICKS, ticks);
	}

	public int getLiveTicks() {
		return entityData.get(LIVE_TICKS);
	}

	public void setLiveTicks(int ticks) {
		entityData.set(LIVE_TICKS, ticks);
	}

	public int getDelay() {
		return entityData.get(DELAY);
	}

	public void setDelay(int delay) {
		entityData.set(DELAY, delay);
	}

	public float getRotation() {
		return entityData.get(ROTATION);
	}

	public void setRotation(float rot) {
		entityData.set(ROTATION, rot);
	}

}
