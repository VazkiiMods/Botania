/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class EntityEnderAir extends Entity {
	private static final String TAG_AGE = "Age";
	private static final int MAX_AGE = 3 * 20;

	public EntityEnderAir(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide && tickCount > MAX_AGE) {
			discard();
		}
		if (level.isClientSide && random.nextBoolean()) {
			float r = (EntityEnderAirBottle.PARTICLE_COLOR >> 16 & 0xFF) / 255.0F;
			float g = (EntityEnderAirBottle.PARTICLE_COLOR >> 8 & 0xFF) / 255.0F;
			float b = (EntityEnderAirBottle.PARTICLE_COLOR & 0xFF) / 255.0F;
			for (int i = 0; i < 5; i++) {
				double x = this.getX() + random.nextDouble();
				double y = this.getY() + random.nextDouble();
				double z = this.getZ() + random.nextDouble();
				level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, r, g, b);
			}
		}
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(@Nonnull CompoundTag tag) {
		tickCount = tag.getInt(TAG_AGE);
	}

	@Override
	protected void addAdditionalSaveData(@Nonnull CompoundTag tag) {
		tag.putInt(TAG_AGE, tickCount);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
