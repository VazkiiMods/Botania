/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
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

	private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> VARIETY = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CHARGE_TICKS = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> LIVE_TICKS = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> DELAY = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Float> ROTATION = DataTracker.registerData(EntityBabylonWeapon.class, TrackedDataHandlerRegistry.FLOAT);

	public EntityBabylonWeapon(EntityType<EntityBabylonWeapon> type, World world) {
		super(type, world);
	}

	public EntityBabylonWeapon(LivingEntity thrower, World world) {
		super(ModEntities.BABYLON_WEAPON, thrower, world);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(CHARGING, false);
		dataTracker.startTracking(VARIETY, 0);
		dataTracker.startTracking(CHARGE_TICKS, 0);
		dataTracker.startTracking(LIVE_TICKS, 0);
		dataTracker.startTracking(DELAY, 0);
		dataTracker.startTracking(ROTATION, 0F);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean shouldRender(double dist) {
		return dist < 64 * 64;
	}

	@Override
	public boolean isImmuneToExplosion() {
		return true;
	}

	@Override
	public void tick() {
		Entity thrower = getOwner();
		if (!world.isClient && (thrower == null || !(thrower instanceof PlayerEntity) || thrower.removed)) {
			remove();
			return;
		}
		PlayerEntity player = (PlayerEntity) thrower;
		if (!world.isClient) {
			ItemStack stack = PlayerHelper.getFirstHeldItem(player, ModItems.kingKey);
			boolean newCharging = !stack.isEmpty() && ItemKingKey.isCharging(stack);
			if (isCharging() != newCharging) {
				setCharging(newCharging);
			}
		}

		Vec3d mot = getVelocity();

		int liveTime = getLiveTicks();
		int delay = getDelay();
		boolean charging = isCharging() && liveTime == 0;

		if (charging) {
			setVelocity(Vec3d.ZERO);

			int chargeTime = getChargeTicks();
			setChargeTicks(chargeTime + 1);

			if (world.random.nextInt(20) == 0) {
				world.playSound(null, getX(), getY(), getZ(), ModSounds.babylonSpawn, SoundCategory.PLAYERS, 0.1F, 1F + world.random.nextFloat() * 3F);
			}
		} else {
			if (liveTime < delay) {
				setVelocity(Vec3d.ZERO);
			} else if (liveTime == delay && player != null) {
				Vec3d playerLook;
				BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 64, true);
				if (rtr.getType() != HitResult.Type.BLOCK) {
					playerLook = player.getRotationVector().multiply(64).add(player.getPos());
				} else {
					playerLook = Vec3d.ofCenter(rtr.getBlockPos());
				}

				Vector3 thisVec = Vector3.fromEntityCenter(this);

				mot = playerLook.subtract(thisVec.x, thisVec.y, thisVec.z).normalize().multiply(2);
				world.playSound(null, getX(), getY(), getZ(), ModSounds.babylonAttack, SoundCategory.PLAYERS, 2F, 0.1F + world.random.nextFloat() * 3F);
			}

			if (!world.isClient) {
				setLiveTicks(liveTime + 1);
				Box axis = new Box(getX(), getY(), getZ(), lastRenderX, lastRenderY, lastRenderZ).expand(2);
				List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, axis);
				for (LivingEntity living : entities) {
					if (living == thrower) {
						continue;
					}

					if (living.hurtTime == 0) {
						if (player != null) {
							living.damage(DamageSource.player(player), 20);
						} else {
							living.damage(DamageSource.GENERIC, 20);
						}
						onCollision(new EntityHitResult(living));
						return;
					}
				}
			}
		}

		super.tick();

		// Apply after super tick so drag is not applied by super
		setVelocity(mot);

		if (world.isClient && liveTime > delay) {
			WispParticleData data = WispParticleData.wisp(0.3F, 1F, 1F, 0F, 1);
			world.addParticle(data, getX(), getY(), getZ(), 0, -0F, 0);
		}

		if (!world.isClient && liveTime > 200 + delay) {
			remove();
		}
	}

	@Override
	protected void onCollision(HitResult pos) {
		Entity thrower = getOwner();
		if (pos.getType() != HitResult.Type.ENTITY || ((EntityHitResult) pos).getEntity() != thrower) {
			world.createExplosion(this, getX(), getY(), getZ(), 3F, Explosion.DestructionType.NONE);
			remove();
		}
	}

	@Override
	public void writeCustomDataToTag(@Nonnull CompoundTag cmp) {
		super.writeCustomDataToTag(cmp);
		cmp.putBoolean(TAG_CHARGING, isCharging());
		cmp.putInt(TAG_VARIETY, getVariety());
		cmp.putInt(TAG_CHARGE_TICKS, getChargeTicks());
		cmp.putInt(TAG_LIVE_TICKS, getLiveTicks());
		cmp.putInt(TAG_DELAY, getDelay());
		cmp.putFloat(TAG_ROTATION, getRotation());
	}

	@Override
	public void readCustomDataFromTag(@Nonnull CompoundTag cmp) {
		super.readCustomDataFromTag(cmp);
		setCharging(cmp.getBoolean(TAG_CHARGING));
		setVariety(cmp.getInt(TAG_VARIETY));
		setChargeTicks(cmp.getInt(TAG_CHARGE_TICKS));
		setLiveTicks(cmp.getInt(TAG_LIVE_TICKS));
		setDelay(cmp.getInt(TAG_DELAY));
		setRotation(cmp.getFloat(TAG_ROTATION));
	}

	public boolean isCharging() {
		return dataTracker.get(CHARGING);
	}

	public void setCharging(boolean charging) {
		dataTracker.set(CHARGING, charging);
	}

	public int getVariety() {
		return dataTracker.get(VARIETY);
	}

	public void setVariety(int var) {
		dataTracker.set(VARIETY, var);
	}

	public int getChargeTicks() {
		return dataTracker.get(CHARGE_TICKS);
	}

	public void setChargeTicks(int ticks) {
		dataTracker.set(CHARGE_TICKS, ticks);
	}

	public int getLiveTicks() {
		return dataTracker.get(LIVE_TICKS);
	}

	public void setLiveTicks(int ticks) {
		dataTracker.set(LIVE_TICKS, ticks);
	}

	public int getDelay() {
		return dataTracker.get(DELAY);
	}

	public void setDelay(int delay) {
		dataTracker.set(DELAY, delay);
	}

	public float getRotation() {
		return dataTracker.get(ROTATION);
	}

	public void setRotation(float rot) {
		dataTracker.set(ROTATION, rot);
	}

}
