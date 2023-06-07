/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.xplat.XplatAbstractions;

public abstract class SparkBaseEntity extends Entity implements SparkEntity {
	private static final String TAG_INVIS = "invis";
	private static final String TAG_NETWORK = "network";
	private static final EntityDataAccessor<Integer> NETWORK = SynchedEntityData.defineId(SparkBaseEntity.class, EntityDataSerializers.INT);

	public SparkBaseEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(NETWORK, 0);
	}

	@Override
	public BlockPos getAttachPos() {
		int x = Mth.floor(getX());
		int y = Mth.floor(getY() - 1);
		int z = Mth.floor(getZ());
		return new BlockPos(x, y, z);
	}

	@Nullable
	@Override
	public ManaReceiver getAttachedManaReceiver() {
		return XplatAbstractions.INSTANCE.findManaReceiver(getLevel(), getAttachPos(), Direction.UP);
	}

	@Override
	public DyeColor getNetwork() {
		return DyeColor.byId(entityData.get(NETWORK));
	}

	@Override
	public void setNetwork(DyeColor color) {
		entityData.set(NETWORK, color.getId());
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setInvisible(compound.getBoolean(TAG_INVIS));
		setNetwork(DyeColor.byId(compound.getInt(TAG_NETWORK)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean(TAG_INVIS, isInvisible());
		compound.putInt(TAG_NETWORK, getNetwork().getId());
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
