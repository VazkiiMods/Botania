/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 16, 2015, 3:56:14 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityBabylonWeapon extends EntityThrowableCopy {
	@ObjectHolder(LibMisc.MOD_ID + ":babylon_weapon")
	public static EntityType<EntityBabylonWeapon> TYPE;

	private static final String TAG_CHARGING = "charging";
	private static final String TAG_VARIETY = "variety";
	private static final String TAG_CHARGE_TICKS = "chargeTicks";
	private static final String TAG_LIVE_TICKS = "liveTicks";
	private static final String TAG_DELAY = "delay";
	private static final String TAG_ROTATION = "rotation";

	private static final DataParameter<Boolean> CHARGING = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VARIETY = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> CHARGE_TICKS = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> LIVE_TICKS = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DELAY = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);
	private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.FLOAT);

	public EntityBabylonWeapon(EntityType<EntityBabylonWeapon> type, World world) {
		super(type, world);
	}

	public EntityBabylonWeapon(LivingEntity thrower, World world) {
		super(TYPE, thrower, world);
	}

	@Override
	protected void registerData() {
		dataManager.register(CHARGING, false);
		dataManager.register(VARIETY, 0);
		dataManager.register(CHARGE_TICKS, 0);
		dataManager.register(LIVE_TICKS, 0);
		dataManager.register(DELAY, 0);
		dataManager.register(ROTATION, 0F);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity thrower = getThrower();
		if(!world.isRemote && (thrower == null || !(thrower instanceof PlayerEntity) || thrower.removed)) {
			remove();
			return;
		}
		PlayerEntity player = (PlayerEntity) thrower;
		boolean charging = isCharging();
		if(!world.isRemote) {
			ItemStack stack = player == null ? ItemStack.EMPTY : PlayerHelper.getFirstHeldItem(player, ModItems.kingKey);
			boolean newCharging = !stack.isEmpty() && ItemKingKey.isCharging(stack);
			if(charging != newCharging) {
				setCharging(newCharging);
				charging = newCharging;
			}
		}

		Vec3d mot = getMotion();

		int liveTime = getLiveTicks();
		int delay = getDelay();
		charging &= liveTime == 0;

		if(charging) {
			setMotion(Vec3d.ZERO);

			int chargeTime = getChargeTicks();
			setChargeTicks(chargeTime + 1);

			if(world.rand.nextInt(20) == 0)
				world.playSound(null, posX, posY, posZ, ModSounds.babylonSpawn, SoundCategory.PLAYERS, 0.1F, 1F + world.rand.nextFloat() * 3F);
		} else {
			if(liveTime < delay) {
				setMotion(Vec3d.ZERO);
			} else if (liveTime == delay && player != null) {
				Vector3 playerLook;
				RayTraceResult lookat = ToolCommons.raytraceFromEntity(world, player, RayTraceContext.FluidMode.SOURCE_ONLY, 64);
				if(lookat.getType() != RayTraceResult.Type.BLOCK)
					playerLook = new Vector3(player.getLookVec()).multiply(64).add(Vector3.fromEntity(player));
				else {
					BlockRayTraceResult rtr = (BlockRayTraceResult) lookat;
					playerLook = new Vector3(rtr.getPos().getX() + 0.5, rtr.getPos().getY() + 0.5, rtr.getPos().getZ() + 0.5);
				}

				Vector3 thisVec = Vector3.fromEntityCenter(this);
				Vector3 motionVec = playerLook.subtract(thisVec).normalize().multiply(2);

				mot = motionVec.toVec3D();
				world.playSound(null, posX, posY, posZ, ModSounds.babylonAttack, SoundCategory.PLAYERS, 2F, 0.1F + world.rand.nextFloat() * 3F);
			}
			setLiveTicks(liveTime + 1);

			if(!world.isRemote) {
				AxisAlignedBB axis = new AxisAlignedBB(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
				List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, axis);
				for(LivingEntity living : entities) {
					if(living == thrower)
						continue;

					if(living.hurtTime == 0) {
						if(player != null)
							living.attackEntityFrom(DamageSource.causePlayerDamage(player), 20);
						else living.attackEntityFrom(DamageSource.GENERIC, 20);
						onImpact(new EntityRayTraceResult(living));
						return;
					}
				}
			}
		}

		super.tick();

		setMotion(mot);

		if(liveTime > delay) {
            WispParticleData data = WispParticleData.wisp(0.3F, 1F, 1F, 0F, 1);
            world.addParticle(data, posX, posY, posZ, 0, -0F, 0);
        }

		if(liveTime > 200 + delay)
			remove();
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		LivingEntity thrower = getThrower();
		if(pos.getType() != RayTraceResult.Type.ENTITY || ((EntityRayTraceResult) pos).getEntity() != thrower) {
			world.createExplosion(this, posX, posY, posZ, 3F, Explosion.Mode.NONE);
			remove();
		}
	}

	@Override
	public void writeAdditional(@Nonnull CompoundNBT cmp) {
		super.writeAdditional(cmp);
		cmp.putBoolean(TAG_CHARGING, isCharging());
		cmp.putInt(TAG_VARIETY, getVariety());
		cmp.putInt(TAG_CHARGE_TICKS, getChargeTicks());
		cmp.putInt(TAG_LIVE_TICKS, getLiveTicks());
		cmp.putInt(TAG_DELAY, getDelay());
		cmp.putFloat(TAG_ROTATION, getRotation());
	}

	@Override
	public void readAdditional(@Nonnull CompoundNBT cmp) {
		super.readAdditional(cmp);
		setCharging(cmp.getBoolean(TAG_CHARGING));
		setVariety(cmp.getInt(TAG_VARIETY));
		setChargeTicks(cmp.getInt(TAG_CHARGE_TICKS));
		setLiveTicks(cmp.getInt(TAG_LIVE_TICKS));
		setDelay(cmp.getInt(TAG_DELAY));
		setRotation(cmp.getFloat(TAG_ROTATION));
	}
	
	public boolean isCharging() {
		return dataManager.get(CHARGING);
	}

	public void setCharging(boolean charging) {
		dataManager.set(CHARGING, charging);
	}

	public int getVariety() {
		return dataManager.get(VARIETY);
	}

	public void setVariety(int var) {
		dataManager.set(VARIETY, var);
	}

	public int getChargeTicks() {
		return dataManager.get(CHARGE_TICKS);
	}

	public void setChargeTicks(int ticks) {
		dataManager.set(CHARGE_TICKS, ticks);
	}

	public int getLiveTicks() {
		return dataManager.get(LIVE_TICKS);
	}

	public void setLiveTicks(int ticks) {
		dataManager.set(LIVE_TICKS, ticks);
	}

	public int getDelay() {
		return dataManager.get(DELAY);
	}

	public void setDelay(int delay) {
		dataManager.set(DELAY, delay);
	}

	public float getRotation() {
		return dataManager.get(ROTATION);
	}

	public void setRotation(float rot) {
		dataManager.set(ROTATION, rot);
	}

}
