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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
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
		setMotion(Vector3d.ZERO);
		super.tick();

		float range = getWidth() / 2;
		float r = 0.2F;
		float g = 0F;
		float b = 0.2F;

		//Botania.proxy.wispFX(world, getPosX(), getPosY(), getPosZ(), r, g, b, 0.6F, -0.2F, 1);
		for (int i = 0; i < 6; i++) {
			WispParticleData data = WispParticleData.wisp(0.4F, r, g, b, (float) 1);
			world.addParticle(data, getPosX() - range + Math.random() * range * 2, getPosY(), getPosZ() - range + Math.random() * range * 2, 0, - -0.015F, 0);
		}

		if (ticksExisted >= 55) {
			world.playSound(null, getPosX(), getPosY(), getPosZ(), ModSounds.gaiaTrap, SoundCategory.NEUTRAL, 0.3F, 1F);

			float m = 0.35F;
			g = 0.4F;
			for (int i = 0; i < 25; i++) {
				WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
				world.addParticle(data, getPosX(), getPosY() + 1, getPosZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
			}

			if (!world.isRemote) {
				List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox());
				for (PlayerEntity player : players) {
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, summoner), 10);
					player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 25, 0));
					EffectInstance wither = new EffectInstance(Effects.WITHER, 120, 2);
					wither.getCurativeItems().clear();
					player.addPotionEffect(wither);
				}
			}

			remove();
		}
	}

	@Override
	protected void registerData() {}

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
