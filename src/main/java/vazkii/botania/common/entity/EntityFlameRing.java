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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityFlameRing extends Entity {
	public EntityFlameRing(EntityType<EntityFlameRing> type, World world) {
		super(type, world);
	}

	@Override
	protected void initDataTracker() {}

	@Override
	public void baseTick() {
		super.baseTick();

		float radius = 5F;
		float renderRadius = (float) (radius - Math.random());

		for (int i = 0; i < Math.min(90, age); i++) {
			float a = i;
			if (a % 2 == 0) {
				a = 45 + a;
			}

			if (world.random.nextInt(age < 90 ? 8 : 20) == 0) {
				float rad = (float) (a * 4 * Math.PI / 180F);
				double x = Math.cos(rad) * renderRadius;
				double z = Math.sin(rad) * renderRadius;

				WispParticleData data1 = WispParticleData.wisp(0.65F + (float) Math.random() * 0.45F, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
				world.addParticle(data1, getX() + x, getY() - 0.2, getZ() + z, (float) (Math.random() - 0.5F) * 0.15F, 0.055F + (float) Math.random() * 0.025F, (float) (Math.random() - 0.5F) * 0.15F);

				float gs = (float) Math.random() * 0.15F;
				float smokeRadius = (float) (renderRadius - Math.random() * renderRadius * 0.9);
				x = Math.cos(rad) * smokeRadius;
				z = Math.sin(rad) * smokeRadius;
				WispParticleData data = WispParticleData.wisp(0.65F + (float) Math.random() * 0.45F, gs, gs, gs, 1);
				world.addParticle(data, getX() + x, getY() - 0.2, getZ() + z, 0, -(-0.155F - (float) Math.random() * 0.025F), 0);
			}
		}

		if (world.random.nextInt(20) == 0) {
			world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
		}

		if (world.isClient) {
			return;
		}

		if (age >= 300) {
			remove();
			return;
		}

		if (age > 45) {
			Box boundingBox = new Box(getX(), getY(), getZ(), getX(), getY(), getZ()).expand(radius, radius, radius);
			List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, boundingBox);

			if (entities.isEmpty()) {
				return;
			}

			for (LivingEntity entity : entities) {
				if (entity == null || MathHelper.pointDistancePlane(getX(), getY(), entity.getX(), entity.getY()) > radius) {
					continue;
				}

				entity.setOnFireFor(4);
			}
		}
	}

	@Override
	public boolean damage(@Nonnull DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void readCustomDataFromTag(@Nonnull CompoundTag var1) {}

	@Override
	protected void writeCustomDataToTag(@Nonnull CompoundTag var1) {}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
