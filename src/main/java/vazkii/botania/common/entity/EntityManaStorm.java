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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class EntityManaStorm extends Entity {
	@ObjectHolder(LibMisc.MOD_ID + ":mana_storm") public static EntityType<EntityManaStorm> TYPE;

	private static final String TAG_TIME = "time";
	private static final String TAG_BURSTS_FIRED = "burstsFired";
	private static final String TAG_DEATH_TIME = "deathTime";

	public static final int TOTAL_BURSTS = 250;
	public static final int DEATH_TIME = 200;

	public int liveTime;
	public int burstsFired;
	public int deathTime;

	public EntityManaStorm(EntityType<EntityManaStorm> type, World world) {
		super(type, world);
	}

	public EntityManaStorm(World world) {
		this(TYPE, world);
	}

	@Override
	protected void registerData() {}

	@Override
	public void tick() {
		super.tick();
		liveTime++;

		int diffTime = Math.max(1, 30 - (int) (liveTime / 45f));
		if (burstsFired < TOTAL_BURSTS && liveTime % diffTime == 0) {
			if (!world.isRemote) {
				spawnBurst();
			}
			burstsFired++;
		}

		if (burstsFired >= TOTAL_BURSTS) {
			deathTime++;
			if (deathTime >= DEATH_TIME) {
				remove();
				world.createExplosion(this, getPosX(), getPosY(), getPosZ(), 8F, true, Explosion.Mode.DESTROY);
			}
		}
	}

	private void spawnBurst() {
		EntityManaBurst burst = new EntityManaBurst(world);
		burst.setPosition(getPosX(), getPosY(), getPosZ());

		float motionModifier = 0.5F;
		burst.setColor(0x20FF20);
		burst.setMana(120);
		burst.setStartingMana(340);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);

		burst.setSourceLens(new ItemStack(ModItems.lensStorm));

		Vector3 motion = new Vector3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().multiply(motionModifier);
		burst.setBurstMotion(motion.x, motion.y, motion.z);
		world.addEntity(burst);
	}

	@Override
	protected void readAdditional(@Nonnull CompoundNBT cmp) {
		liveTime = cmp.getInt(TAG_TIME);
		burstsFired = cmp.getInt(TAG_BURSTS_FIRED);
		deathTime = cmp.getInt(TAG_DEATH_TIME);
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT cmp) {
		cmp.putInt(TAG_TIME, liveTime);
		cmp.putInt(TAG_BURSTS_FIRED, burstsFired);
		cmp.putInt(TAG_DEATH_TIME, deathTime);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
