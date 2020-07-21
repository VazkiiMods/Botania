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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public abstract class EntitySparkBase extends Entity {
	private static final String TAG_INVIS = "invis";
	private static final String TAG_NETWORK = "network";
	private static final TrackedData<Integer> NETWORK = DataTracker.registerData(EntitySparkBase.class, TrackedDataHandlerRegistry.INTEGER);

	public EntitySparkBase(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(NETWORK, 0);
	}

	public DyeColor getNetwork() {
		return DyeColor.byId(dataTracker.get(NETWORK));
	}

	public void setNetwork(DyeColor color) {
		dataTracker.set(NETWORK, color.getId());
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag compound) {
		setInvisible(compound.getBoolean(TAG_INVIS));
		setNetwork(DyeColor.byId(compound.getInt(TAG_NETWORK)));
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag compound) {
		compound.putBoolean(TAG_INVIS, isInvisible());
		compound.putInt(TAG_NETWORK, getNetwork().getId());
	}
}
