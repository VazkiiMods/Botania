/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 25, 2015, 12:35:51 AM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;

public class EntityManaStorm extends Entity {

	private static final String TAG_TIME = "time";
	private static final String TAG_BURSTS_FIRED = "burstsFired";
	private static final String TAG_DEATH_TIME = "deathTime";

	public static final int TOTAL_BURSTS = 250;
	public static final int DEATH_TIME = 200;

	public int liveTime;
	public int burstsFired;
	public int deathTime;

	public EntityManaStorm(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {}

	@Override
	public void onUpdate() {
		super.onUpdate();
		liveTime++;

		int diffTime = Math.max(1, 30 - (int) (liveTime / 45f));
		if(burstsFired < TOTAL_BURSTS && liveTime % diffTime == 0) {
			if(!world.isRemote)
				spawnBurst();
			burstsFired++;
		}

		if(burstsFired >= TOTAL_BURSTS) {
			deathTime++;
			if(deathTime >= DEATH_TIME) {
				setDead();
				world.newExplosion(this, posX, posY, posZ, 8F, true, true);
			}
		}
	}

	private void spawnBurst() {
		EntityManaBurst burst = new EntityManaBurst(world);
		burst.setPosition(posX, posY, posZ);

		float motionModifier = 0.5F;
		burst.setColor(0x20FF20);
		burst.setMana(120);
		burst.setStartingMana(340);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);

		ItemStack lens = new ItemStack(ModItems.lens, 1, ItemLens.STORM);
		burst.setSourceLens(lens);

		Vector3 motion = new Vector3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().multiply(motionModifier);
		burst.setMotion(motion.x, motion.y, motion.z);
		world.spawnEntity(burst);
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound cmp) {
		liveTime = cmp.getInteger(TAG_TIME);
		burstsFired = cmp.getInteger(TAG_BURSTS_FIRED);
		deathTime = cmp.getInteger(TAG_DEATH_TIME);
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
		cmp.setInteger(TAG_TIME, liveTime);
		cmp.setInteger(TAG_BURSTS_FIRED, burstsFired);
		cmp.setInteger(TAG_DEATH_TIME, deathTime);
	}

}
