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
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public class MagicLandmineEntity extends Entity {
	public GaiaGuardianEntity summoner;

	public MagicLandmineEntity(EntityType<MagicLandmineEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		setDeltaMovement(Vec3.ZERO);
		super.tick();

		float range = getBbWidth() / 2;
		float r = 0.2F;
		float g = 0F;
		float b = 0.2F;

		//Proxy.INSTANCE.wispFX(world, getPosX(), getPosY(), getPosZ(), r, g, b, 0.6F, -0.2F, 1);
		for (int i = 0; i < 6; i++) {
			WispParticleData data = WispParticleData.wisp(0.4F, r, g, b, (float) 1);
			getLevel().addParticle(data, getX() - range + Math.random() * range * 2, getY(), getZ() - range + Math.random() * range * 2, 0, - -0.015F, 0);
		}

		if (tickCount >= 55) {
			getLevel().playSound(null, getX(), getY(), getZ(), BotaniaSounds.gaiaTrap, SoundSource.NEUTRAL, 1F, 1F);

			float m = 0.35F;
			g = 0.4F;
			for (int i = 0; i < 25; i++) {
				WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
				getLevel().addParticle(data, getX(), getY() + 1, getZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
			}

			if (!getLevel().isClientSide) {
				List<Player> players = getLevel().getEntitiesOfClass(Player.class, getBoundingBox());
				for (Player player : players) {
					player.hurt(DamageSource.indirectMagic(this, summoner), 10);
					player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 25, 0));
					MobEffectInstance wither = new MobEffectInstance(MobEffects.WITHER, 120, 2);
					// wither.getCurativeItems().clear();
					player.addEffect(wither);
				}
			}

			discard();
		}
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(@NotNull CompoundTag var1) {}

	@Override
	protected void addAdditionalSaveData(@NotNull CompoundTag var1) {}

	@NotNull
	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
