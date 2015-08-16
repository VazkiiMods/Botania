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

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.ItemKingKey;

public class EntityBabylonWeapon extends EntityThrowableCopy {

	// TODO NBT

	public EntityBabylonWeapon(World world) {
		super(world);
	}

	public EntityBabylonWeapon(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}	

	@Override
	protected void entityInit() {
		setSize(0F, 0F);

		dataWatcher.addObject(20, (byte) 0);
		dataWatcher.addObject(21, 0);
		dataWatcher.addObject(22, 0);
		dataWatcher.addObject(23, 0);
		dataWatcher.addObject(24, 0);

		dataWatcher.setObjectWatched(20);
		dataWatcher.setObjectWatched(21);
		dataWatcher.setObjectWatched(22);
		dataWatcher.setObjectWatched(23);
		dataWatcher.setObjectWatched(24);
	}

	@Override
	public void onUpdate() {
		EntityLivingBase thrower = getThrower();
		if(!worldObj.isRemote && (thrower == null || !(thrower instanceof EntityPlayer))) {
			setDead();
			return;
		}
		EntityPlayer player = (EntityPlayer) thrower;
		boolean charging = isCharging();
		if(!worldObj.isRemote) {
			ItemStack stack = player == null ? null : player.getCurrentEquippedItem();
			boolean newCharging = stack != null && stack.getItem() == ModItems.kingKey && ItemKingKey.isCharging(stack);
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
		} else {
			if(liveTime < delay) {
				motionX = 0;
				motionY = 0;
				motionZ = 0;
			} else if(liveTime == delay && !worldObj.isRemote && player != null) {
				Vector3 playerLook = null;
				MovingObjectPosition lookat = ToolCommons.raytraceFromEntity(worldObj, player, true, 64);
				if(lookat == null)
					playerLook = new Vector3(player.getLookVec()).multiply(64).add(Vector3.fromEntity(player));
				else playerLook = new Vector3(lookat.blockX + 0.5, lookat.blockY + 0.5, lookat.blockZ + 0.5);

				Vector3 thisVec = Vector3.fromEntityCenter(this);
				Vector3 motionVec = playerLook.sub(thisVec).normalize().multiply(2);

				x = motionVec.x;
				y = motionVec.y;
				z = motionVec.z;
			}

			setLiveTicks(liveTime + 1);

			if(!worldObj.isRemote) {
				AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).expand(2, 2, 2);
				List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
				for(EntityLivingBase living : entities) {
					if(living != thrower)
						continue;

					if(living.hurtTime == 0) {
						if(player != null)
							living.attackEntityFrom(DamageSource.causePlayerDamage(player), 14);
						else living.attackEntityFrom(DamageSource.generic, 14);
						onImpact(new MovingObjectPosition(living));
						return;
					}
				}
			}
		}

		super.onUpdate();

		motionX = x;
		motionY = y;
		motionZ = z;

		Botania.proxy.wispFX(worldObj, posX, posY, posZ, 1F, 1F, 0F, 0.3F, 0F);

		if(liveTime > (200 + delay))
			setDead();
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		EntityLivingBase thrower = getThrower();
		if(pos.entityHit == null || pos.entityHit != thrower) {
			worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
			setDead();
		}
	}

	public boolean isCharging() {
		return dataWatcher.getWatchableObjectByte(20) == 1;
	}

	public void setCharging(boolean charging) {
		dataWatcher.updateObject(20, (byte) (charging ? 1 : 0));
	}

	public int getVariety() {
		return dataWatcher.getWatchableObjectInt(21);
	}

	public void setVariety(int var) {
		dataWatcher.updateObject(21, var);
	}

	public int getChargeTicks() {
		return dataWatcher.getWatchableObjectInt(22);
	}

	public void setChargeTicks(int ticks) {
		dataWatcher.updateObject(22, ticks);
	}

	public int getLiveTicks() {
		return dataWatcher.getWatchableObjectInt(23);
	}

	public void setLiveTicks(int ticks) {
		dataWatcher.updateObject(23, ticks);
	}

	public int getDelay() {
		return dataWatcher.getWatchableObjectInt(24);
	}

	public void setDelay(int delay) {
		dataWatcher.updateObject(24, delay);
	}

}
