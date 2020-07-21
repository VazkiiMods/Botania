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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityMagicLandmine extends Entity {
	public EntityDoppleganger summoner;

	public EntityMagicLandmine(EntityType<EntityMagicLandmine> type, World world) {
		super(type, world);
	}

	@Override
	public void tick() {
		setVelocity(Vec3d.ZERO);
		super.tick();

		float range = getWidth() / 2;
		float r = 0.2F;
		float g = 0F;
		float b = 0.2F;

		//Botania.proxy.wispFX(world, getPosX(), getPosY(), getPosZ(), r, g, b, 0.6F, -0.2F, 1);
		for (int i = 0; i < 6; i++) {
			WispParticleData data = WispParticleData.wisp(0.4F, r, g, b, (float) 1);
			world.addParticle(data, getX() - range + Math.random() * range * 2, getY(), getZ() - range + Math.random() * range * 2, 0, - -0.015F, 0);
		}

		if (age >= 55) {
			world.playSound(null, getX(), getY(), getZ(), ModSounds.gaiaTrap, SoundCategory.NEUTRAL, 0.3F, 1F);

			float m = 0.35F;
			g = 0.4F;
			for (int i = 0; i < 25; i++) {
				WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
				world.addParticle(data, getX(), getY() + 1, getZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
			}

			if (!world.isClient) {
				List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, getBoundingBox());
				for (PlayerEntity player : players) {
					player.damage(DamageSource.magic(this, summoner), 10);
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0));
					StatusEffectInstance wither = new StatusEffectInstance(StatusEffects.WITHER, 120, 2);
					wither.getCurativeItems().clear();
					player.addStatusEffect(wither);
				}
			}

			remove();
		}
	}

	@Override
	protected void initDataTracker() {}

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
