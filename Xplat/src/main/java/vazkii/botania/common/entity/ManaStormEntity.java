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
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.item.BotaniaItems;

public class ManaStormEntity extends Entity {
	private static final String TAG_TIME = "time";
	private static final String TAG_BURST_COLOR = "burstColor";
	private static final String TAG_BURSTS_FIRED = "burstsFired";
	private static final String TAG_DEATH_TIME = "deathTime";

	public static final int TOTAL_BURSTS = 250;
	public static final int DEATH_TIME = 200;

	public int liveTime;
	public int burstColor;
	public int burstsFired;
	public int deathTime;

	public ManaStormEntity(EntityType<ManaStormEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public void tick() {
		super.tick();
		liveTime++;

		int diffTime = Math.max(1, 30 - (int) (liveTime / 45f));
		if (burstsFired < TOTAL_BURSTS && liveTime % diffTime == 0) {
			if (!getLevel().isClientSide) {
				spawnBurst();
			}
			burstsFired++;
		}

		if (burstsFired >= TOTAL_BURSTS) {
			deathTime++;
			if (deathTime >= DEATH_TIME) {
				discard();
				// TODO 1.19.3 check all explosion interactions
				getLevel().explode(this, getX(), getY(), getZ(), 8F, true, Level.ExplosionInteraction.BLOCK);
			}
		}
	}

	private void spawnBurst() {
		ManaBurstEntity burst = BotaniaEntities.MANA_BURST.create(getLevel());
		burst.setPos(getX(), getY(), getZ());

		float motionModifier = 0.5F;
		burst.setColor(burstColor);
		burst.setMana(120);
		burst.setStartingMana(340);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);

		burst.setSourceLens(new ItemStack(BotaniaItems.lensStorm));

		Vec3 motion = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().scale(motionModifier);
		burst.setDeltaMovement(motion);
		getLevel().addFreshEntity(burst);
	}

	@Override
	protected void readAdditionalSaveData(@NotNull CompoundTag cmp) {
		liveTime = cmp.getInt(TAG_TIME);
		burstColor = cmp.getInt(TAG_BURST_COLOR);
		burstsFired = cmp.getInt(TAG_BURSTS_FIRED);
		deathTime = cmp.getInt(TAG_DEATH_TIME);
	}

	@Override
	protected void addAdditionalSaveData(@NotNull CompoundTag cmp) {
		cmp.putInt(TAG_TIME, liveTime);
		cmp.putInt(TAG_BURST_COLOR, burstColor);
		cmp.putInt(TAG_BURSTS_FIRED, burstsFired);
		cmp.putInt(TAG_DEATH_TIME, deathTime);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

}
