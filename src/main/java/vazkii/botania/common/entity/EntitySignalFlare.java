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
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;

import javax.annotation.Nonnull;

public class EntitySignalFlare extends Entity {
	private static final String COLOR_TAG = "color";
	private static final String FIRED_Y_TAG = "firedY";
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntitySignalFlare.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> FIRED_Y = EntityDataManager.createKey(EntitySignalFlare.class, DataSerializers.VARINT);

	public EntitySignalFlare(EntityType<EntitySignalFlare> type, World world) {
		super(type, world);
	}

	public EntitySignalFlare(World world) {
		this(ModEntities.SIGNAL_FLARE, world);
	}

	@Override
	protected void registerData() {
		dataManager.register(COLOR, 0);
		dataManager.register(FIRED_Y, 0);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (ticksExisted++ >= 100) {
			remove();
		}

		if (!removed) {
			if (ticksExisted % 10 == 0) {
				playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1F, 1F);
			}

			int color = getColor();
			if (color < 16 && color >= 0) {
				int hex = DyeColor.byId(color).getColorValue();
				int r = (hex & 0xFF0000) >> 16;
				int g = (hex & 0xFF00) >> 8;
				int b = hex & 0xFF;

				for (int i = 0; i < 3; i++) {
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 5 + 1F, r / 255F, g / 255F, b / 255F);
					Botania.proxy.addParticleForce(world, data, getPosX(), getPosY(), getPosZ() + 0.5, (float) (Math.random() - 0.5F), 10F * (float) Math.sqrt(256F / (256F - (float) getPosY())), (float) (Math.random() - 0.5F));
				}

				for (int i = 0; i < 4; i++) {
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 15 + 8F, r / 255F, g / 255F, b / 255F);
					Botania.proxy.addParticleForce(world, data, getPosX() + 0.5, Math.min(256, getFiredAt() + Botania.proxy.getClientRenderDistance() * 16), getPosZ() + 0.5, (float) (Math.random() - 0.5F) * 8F, 0F, (float) (Math.random() - 0.5F) * 8F);
				}
			}
		}
	}

	@Override
	protected void readAdditional(@Nonnull CompoundNBT nbttagcompound) {
		setColor(nbttagcompound.getInt(COLOR_TAG));
		setFiredAt(nbttagcompound.getInt(FIRED_Y_TAG));
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT nbttagcompound) {
		nbttagcompound.putInt(COLOR_TAG, getColor());
		nbttagcompound.putInt(FIRED_Y_TAG, getFiredAt());
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public void setColor(int color) {
		dataManager.set(COLOR, color);
	}

	public int getColor() {
		return dataManager.get(COLOR);
	}

	public void setFiredAt(int y) {
		dataManager.set(FIRED_Y, y);
	}

	public int getFiredAt() {
		return dataManager.get(FIRED_Y);
	}

}
