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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.mixin.ProjectileAccessor;

import java.util.UUID;

public class ManaStormEntity extends Entity implements TraceableEntity {
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
	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	public ManaStormEntity(EntityType<ManaStormEntity> type, Level level) {
		super(type, level);
	}

	public void setOwner(@Nullable Entity owner, @Nullable UUID ownerUUID) {
		if (owner != null) {
			this.ownerUUID = owner.getUUID();
			this.cachedOwner = owner;
		} else if (ownerUUID != null) {
			this.ownerUUID = ownerUUID;
		}
	}

	@Nullable
	@Override
	public Entity getOwner() {
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
			return this.cachedOwner;
		} else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
			this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
			return this.cachedOwner;
		} else {
			return null;
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {}

	@Override
	public void tick() {
		super.tick();
		liveTime++;

		int diffTime = Math.max(1, 30 - (int) (liveTime / 45f));
		if (burstsFired < TOTAL_BURSTS && liveTime % diffTime == 0) {
			if (!level().isClientSide()) {
				spawnBurst();
			}
			burstsFired++;
		}

		if (burstsFired >= TOTAL_BURSTS) {
			deathTime++;
			if (deathTime >= DEATH_TIME) {
				discard();
				level().explode(this, getX(), getY(), getZ(), 8F, true, Level.ExplosionInteraction.BLOCK);
			}
		}
	}

	private void spawnBurst() {
		ManaBurstEntity burst = BotaniaEntities.MANA_BURST.create(level());
		if (burst == null) {
			return;
		}
		Entity owner = getOwner();
		burst.setOwner(owner);
		if (owner == null && ownerUUID != null) {
			((ProjectileAccessor) burst).botania_setOwnerUUID(ownerUUID);
		}
		burst.setPos(getX(), getY(), getZ());

		float motionModifier = 0.5F;
		burst.setColor(burstColor);
		burst.setMana(300);
		burst.setStartingMana(850);
		burst.setMinManaLoss(50);
		burst.setManaLossPerTick(2.5F);
		burst.setGravity(0F);

		burst.setSourceLens(new ItemStack(BotaniaItems.STORM_LENS));

		Vec3 motion = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().scale(motionModifier);
		burst.setDeltaMovement(motion);
		level().addFreshEntity(burst);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag cmp) {
		liveTime = cmp.getInt(TAG_TIME);
		burstColor = cmp.getInt(TAG_BURST_COLOR);
		burstsFired = cmp.getInt(TAG_BURSTS_FIRED);
		deathTime = cmp.getInt(TAG_DEATH_TIME);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag cmp) {
		cmp.putInt(TAG_TIME, liveTime);
		cmp.putInt(TAG_BURST_COLOR, burstColor);
		cmp.putInt(TAG_BURSTS_FIRED, burstsFired);
		cmp.putInt(TAG_DEATH_TIME, deathTime);
	}
}
