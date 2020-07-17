/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class BergamuteEventHandler {

	private BergamuteEventHandler() {}

	private static final Random RAND = new Random();
	private static final float MULTIPLIER = 0.15F;

	public static void onSoundEvent(PlaySoundEvent evt) {
		ISound sound = evt.getResultSound();

		if (sound != null && shouldSilence(sound)) {
			if (sound instanceof ITickableSound) {
				evt.setResultSound(new WrappedTickableSound((ITickableSound) sound, MULTIPLIER));
			} else {
				SubTileBergamute berg = SubTileBergamute.getBergamuteNearby(sound.getX(), sound.getY(), sound.getZ());

				if (berg != null) {
					evt.setResultSound(new WrappedSound(sound, MULTIPLIER));

					if (RAND.nextBoolean()) {
						int color = TilePool.PARTICLE_COLOR;
						float red = (color >> 16 & 0xFF) / 255F;
						float green = (color >> 8 & 0xFF) / 255F;
						float blue = (color & 0xFF) / 255F;
						SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
						berg.getWorld().addParticle(data, berg.getEffectivePos().getX() + 0.3 + Math.random() * 0.5, berg.getEffectivePos().getY() + 0.5 + Math.random() * 0.5, berg.getEffectivePos().getZ() + 0.3 + Math.random() * 0.5, 0, 0, 0);
					}
				}
			}
		}
	}

	private static boolean shouldSilence(ISound sound) {
		return sound.getCategory() != SoundCategory.VOICE
				&& sound.getCategory() != SoundCategory.MUSIC
				&& sound.getCategory() != SoundCategory.RECORDS
				&& sound.getCategory() != SoundCategory.AMBIENT
				&& sound.getClass().getName().startsWith("net.minecraft.client.audio");
	}

	@OnlyIn(Dist.CLIENT)
	private static class WrappedSound implements ISound {

		private final ISound compose;
		private final float volMult;
		private final boolean recheck;

		private WrappedSound(ISound toWrap, float volMult) {
			compose = toWrap;
			this.volMult = volMult;
			recheck = toWrap instanceof TickableSound;
		}

		@Override
		public float getVolume() {
			float mult = recheck && SubTileBergamute.getBergamuteNearby(getX(), getY(), getZ()) == null ? 1F : volMult;
			return compose.getVolume() * mult;
		}

		@Nonnull
		@Override
		public ResourceLocation getSoundLocation() {
			return compose.getSoundLocation();
		}

		@Nullable
		@Override
		public SoundEventAccessor createAccessor(@Nonnull SoundHandler handler) {
			return compose.createAccessor(handler);
		}

		@Nonnull
		@Override
		public Sound getSound() {
			return compose.getSound();
		}

		@Nonnull
		@Override
		public SoundCategory getCategory() {
			return compose.getCategory();
		}

		@Override
		public boolean canRepeat() {
			return compose.canRepeat();
		}

		@Override
		public boolean isGlobal() {
			return compose.isGlobal();
		}

		@Override
		public int getRepeatDelay() {
			return compose.getRepeatDelay();
		}

		@Override
		public float getPitch() {
			return compose.getPitch();
		}

		@Override
		public double getX() {
			return compose.getX();
		}

		@Override
		public double getY() {
			return compose.getY();
		}

		@Override
		public double getZ() {
			return compose.getZ();
		}

		@Override
		public boolean canBeSilent() {
			return compose.canBeSilent();
		}

		@Nonnull
		@Override
		public AttenuationType getAttenuationType() {
			return compose.getAttenuationType();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class WrappedTickableSound extends WrappedSound implements ITickableSound {

		private final ITickableSound compose;

		private WrappedTickableSound(ITickableSound toWrap, float volMult) {
			super(toWrap, volMult);
			compose = toWrap;
		}

		@Override
		public boolean isDonePlaying() {
			return compose.isDonePlaying();
		}

		@Override
		public void tick() {
			compose.tick();
		}
	}
}
