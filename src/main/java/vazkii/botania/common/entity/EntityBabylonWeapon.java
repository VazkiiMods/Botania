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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.ItemKingKey;

import javax.annotation.Nonnull;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;

import java.util.List;

public class EntityBabylonWeapon extends EntityThrowableCopy {

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

	public EntityBabylonWeapon(World world) {
		super(world);
	}

	public EntityBabylonWeapon(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		setSize(0F, 0F);

		dataManager.register(CHARGING, false);
		dataManager.register(VARIETY, 0);
		dataManager.register(CHARGE_TICKS, 0);
		dataManager.register(LIVE_TICKS, 0);
		dataManager.register(DELAY, 0);
		dataManager.register(ROTATION, 0F);
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	@Override
	public void onUpdate() {
		EntityLivingBase thrower = getThrower();
		if(!world.isRemote && (thrower == null || !(thrower instanceof EntityPlayer) || thrower.isDead)) {
			setDead();
			return;
		}
		EntityPlayer player = (EntityPlayer) thrower;
		boolean charging = isCharging();
		if(!world.isRemote) {
			ItemStack stack = player == null ? ItemStack.EMPTY : PlayerHelper.getFirstHeldItem(player, ModItems.kingKey);
			boolean newCharging = !stack.isEmpty() && ItemKingKey.isCharging(stack);
			if(charging != newCharging) {
				setCharging(newCharging);
				charging = newCharging;
			}
		}

		double x = motionX;
		double y = motionY;
		double z = motionZ;

		int liveTime = getLiveTicks();
		int delay = getDelay();
		charging &= liveTime == 0;

		if(charging) {
			motionX = 0;
			motionY = 0;
			motionZ = 0;

			int chargeTime = getChargeTicks();
			setChargeTicks(chargeTime + 1);

			if(world.rand.nextInt(20) == 0)
				world.playSound(null, posX, posY, posZ, ModSounds.babylonSpawn, SoundCategory.PLAYERS, 0.1F, 1F + world.rand.nextFloat() * 3F);
		} else {
			if(liveTime < delay) {
				motionX = 0;
				motionY = 0;
				motionZ = 0;
			} else if (liveTime == delay && player != null) {
				Vector3 playerLook;
				RayTraceResult lookat = ToolCommons.raytraceFromEntity(world, player, true, 64);
				if(lookat == null)
					playerLook = new Vector3(player.getLookVec()).multiply(64).add(Vector3.fromEntity(player));
				else playerLook = new Vector3(lookat.getBlockPos().getX() + 0.5, lookat.getBlockPos().getY() + 0.5, lookat.getBlockPos().getZ() + 0.5);

				Vector3 thisVec = Vector3.fromEntityCenter(this);
				Vector3 motionVec = playerLook.subtract(thisVec).normalize().multiply(2);

				x = motionVec.x;
				y = motionVec.y;
				z = motionVec.z;
				world.playSound(null, posX, posY, posZ, ModSounds.babylonAttack, SoundCategory.PLAYERS, 2F, 0.1F + world.rand.nextFloat() * 3F);
			}
			setLiveTicks(liveTime + 1);

			if(!world.isRemote) {
				AxisAlignedBB axis = new AxisAlignedBB(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, axis);
				for(EntityLivingBase living : entities) {
					if(living == thrower)
						continue;

					if(living.hurtTime == 0) {
						if(player != null)
							living.attackEntityFrom(DamageSource.causePlayerDamage(player), 20);
						else living.attackEntityFrom(DamageSource.GENERIC, 20);
						onImpact(new RayTraceResult(living));
						return;
					}
				}
			}
		}

		super.onUpdate();

		motionX = x;
		motionY = y;
		motionZ = z;

		if(liveTime > delay)
			Botania.proxy.wispFX(posX, posY, posZ, 1F, 1F, 0F, 0.3F, 0F);

		if(liveTime > 200 + delay)
			setDead();
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		EntityLivingBase thrower = getThrower();
		if(pos.entityHit == null || pos.entityHit != thrower) {
			world.createExplosion(this, posX, posY, posZ, 3F, false);
			setDead();
		}
	}

	@Override
	public void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
		super.writeEntityToNBT(cmp);
		cmp.setBoolean(TAG_CHARGING, isCharging());
		cmp.setInteger(TAG_VARIETY, getVariety());
		cmp.setInteger(TAG_CHARGE_TICKS, getChargeTicks());
		cmp.setInteger(TAG_LIVE_TICKS, getLiveTicks());
		cmp.setInteger(TAG_DELAY, getDelay());
		cmp.setFloat(TAG_ROTATION, getRotation());
	}

	@Override
	public void readEntityFromNBT(@Nonnull NBTTagCompound cmp) {
		super.readEntityFromNBT(cmp);
		setCharging(cmp.getBoolean(TAG_CHARGING));
		setVariety(cmp.getInteger(TAG_VARIETY));
		setChargeTicks(cmp.getInteger(TAG_CHARGE_TICKS));
		setLiveTicks(cmp.getInteger(TAG_LIVE_TICKS));
		setDelay(cmp.getInteger(TAG_DELAY));
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
