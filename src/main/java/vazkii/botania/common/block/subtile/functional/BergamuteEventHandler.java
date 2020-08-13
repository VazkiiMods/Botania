/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class BergamuteEventHandler {

	private BergamuteEventHandler() {}

	private static final Random RAND = new Random();
	private static final float MULTIPLIER = 0.15F;

	public static SoundInstance onSoundEvent(SoundInstance sound) {

		if (sound != null && shouldSilence(sound)) {
			if (sound instanceof TickableSoundInstance) {
				return new WrappedTickableSound((TickableSoundInstance) sound, MULTIPLIER);
			} else {
				SubTileBergamute berg = SubTileBergamute.getBergamuteNearby(sound.getX(), sound.getY(), sound.getZ());

				if (berg != null) {
					if (RAND.nextBoolean()) {
						int color = TilePool.PARTICLE_COLOR;
						float red = (color >> 16 & 0xFF) / 255F;
						float green = (color >> 8 & 0xFF) / 255F;
						float blue = (color & 0xFF) / 255F;
						SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
						berg.getWorld().addParticle(data, berg.getEffectivePos().getX() + 0.3 + Math.random() * 0.5, berg.getEffectivePos().getY() + 0.5 + Math.random() * 0.5, berg.getEffectivePos().getZ() + 0.3 + Math.random() * 0.5, 0, 0, 0);
					}

					return new WrappedSound(sound, MULTIPLIER);
				}
			}
		}

		return sound;
	}

	private static boolean shouldSilence(SoundInstance sound) {
		return sound.getCategory() != SoundCategory.VOICE
				&& sound.getCategory() != SoundCategory.MUSIC
				&& sound.getCategory() != SoundCategory.RECORDS
				&& sound.getCategory() != SoundCategory.AMBIENT
				&& sound.getClass().getName().startsWith("net.minecraft.client.sound");
	}

	@Environment(EnvType.CLIENT)
	private static class WrappedSound implements SoundInstance {

		private final SoundInstance compose;
		private final float volMult;
		private final boolean recheck;

		private WrappedSound(SoundInstance toWrap, float volMult) {
			compose = toWrap;
			this.volMult = volMult;
			recheck = toWrap instanceof MovingSoundInstance;
		}

		@Override
		public float getVolume() {
			float mult = recheck && SubTileBergamute.getBergamuteNearby(getX(), getY(), getZ()) == null ? 1F : volMult;
			return compose.getVolume() * mult;
		}

		@Nonnull
		@Override
		public Identifier getId() {
			return compose.getId();
		}

		@Nullable
		@Override
		public WeightedSoundSet getSoundSet(@Nonnull SoundManager handler) {
			return compose.getSoundSet(handler);
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
		public boolean isRepeatable() {
			return compose.isRepeatable();
		}

		@Override
		public boolean isLooping() {
			return compose.isLooping();
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
		public boolean shouldAlwaysPlay() {
			return compose.shouldAlwaysPlay();
		}

		@Nonnull
		@Override
		public AttenuationType getAttenuationType() {
			return compose.getAttenuationType();
		}
	}

	@Environment(EnvType.CLIENT)
	private static class WrappedTickableSound extends WrappedSound implements TickableSoundInstance {

		private final TickableSoundInstance compose;

		private WrappedTickableSound(TickableSoundInstance toWrap, float volMult) {
			super(toWrap, volMult);
			compose = toWrap;
		}

		@Override
		public boolean isDone() {
			return compose.isDone();
		}

		@Override
		public void tick() {
			compose.tick();
		}
	}
}
