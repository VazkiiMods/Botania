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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityFlameRing extends Entity {
	@ObjectHolder(LibMisc.MOD_ID + ":flame_ring") public static EntityType<EntityFlameRing> TYPE;

	public EntityFlameRing(EntityType<EntityFlameRing> type, World world) {
		super(type, world);
	}

	public EntityFlameRing(World world) {
		this(TYPE, world);
	}

	@Override
	protected void registerData() {}

	@Override
	public void baseTick() {
		super.baseTick();

		float radius = 5F;
		float renderRadius = (float) (radius - Math.random());

		for (int i = 0; i < Math.min(90, ticksExisted); i++) {
			float a = i;
			if (a % 2 == 0) {
				a = 45 + a;
			}

			if (world.rand.nextInt(ticksExisted < 90 ? 8 : 20) == 0) {
				float rad = (float) (a * 4 * Math.PI / 180F);
				double x = Math.cos(rad) * renderRadius;
				double z = Math.sin(rad) * renderRadius;

				WispParticleData data1 = WispParticleData.wisp(0.65F + (float) Math.random() * 0.45F, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
				world.addParticle(data1, getPosX() + x, getPosY() - 0.2, getPosZ() + z, (float) (Math.random() - 0.5F) * 0.15F, 0.055F + (float) Math.random() * 0.025F, (float) (Math.random() - 0.5F) * 0.15F);

				float gs = (float) Math.random() * 0.15F;
				float smokeRadius = (float) (renderRadius - Math.random() * renderRadius * 0.9);
				x = Math.cos(rad) * smokeRadius;
				z = Math.sin(rad) * smokeRadius;
				WispParticleData data = WispParticleData.wisp(0.65F + (float) Math.random() * 0.45F, gs, gs, gs, 1);
				world.addParticle(data, getPosX() + x, getPosY() - 0.2, getPosZ() + z, 0, -(-0.155F - (float) Math.random() * 0.025F), 0);
			}
		}

		if (world.rand.nextInt(20) == 0) {
			world.playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
		}

		if (world.isRemote) {
			return;
		}

		if (ticksExisted >= 300) {
			remove();
			return;
		}

		if (ticksExisted > 45) {
			AxisAlignedBB boundingBox = new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX(), getPosY(), getPosZ()).grow(radius, radius, radius);
			List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, boundingBox);

			if (entities.isEmpty()) {
				return;
			}

			for (LivingEntity entity : entities) {
				if (entity == null || MathHelper.pointDistancePlane(getPosX(), getPosY(), entity.getPosX(), entity.getPosY()) > radius) {
					continue;
				}

				entity.setFire(4);
			}
		}
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void readAdditional(@Nonnull CompoundNBT var1) {}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT var1) {}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
