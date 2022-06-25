/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin.client;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.subtile.functional.SubTileBergamute;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
	@Unique
	@Nullable
	private SoundInstance tmpSound;

	@Unique
	private static Set<SoundInstance> mutedSounds;

	// calculateVolume(float, SoundSource) can be called from two different places, capture in each of them

	@Inject(at = @At("HEAD"), method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F")
	private void captureSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = sound;
	}

	@Inject(at = @At("HEAD"), method = "play")
	private void captureSound2(SoundInstance sound, CallbackInfo ci) {
		tmpSound = sound;
	}

	@Unique
	private static boolean shouldSilence(SoundInstance sound) {
		return sound.getSource() != SoundSource.VOICE
				&& sound.getSource() != SoundSource.MUSIC
				&& sound.getSource() != SoundSource.RECORDS
				&& sound.getSource() != SoundSource.AMBIENT;
	}

	@ModifyArg(index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F")
	private float bergamuateAttenuate(float volume) {
		if (tmpSound != null && shouldSilence(tmpSound)) {
			// We halve the volume for each flower (see return below)
			// halving 8 times already brings the multiplier to near zero, so no
			// need to keep going if we've seen more than 8.
			var level = Minecraft.getInstance().level;
			Pair<Integer, SubTileBergamute> countAndBerg = level == null
					? Pair.of(0, null)
					: SubTileBergamute.getBergamutesNearby(level, tmpSound.getX(), tmpSound.getY(), tmpSound.getZ(), 8);
			int count = countAndBerg.getFirst();
			if (count > 0) {
				if (mutedSounds == null) {
					mutedSounds = Collections.newSetFromMap(new WeakHashMap<>());
				}
				if (mutedSounds.add(tmpSound) && Math.random() < 0.5) {
					SubTileBergamute.particle(countAndBerg.getSecond());
				}

				// If the multiplier here is adjusted, also adjust the count constant passed to getBergamutesNearby
				return volume * (float) Math.pow(0.5, count);
			}
		}
		return volume;
	}

	@Inject(at = @At("RETURN"), method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F")
	private void clearSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = null;
	}

	@Inject(at = @At("RETURN"), method = "play")
	private void clearSound2(SoundInstance sound, CallbackInfo ci) {
		tmpSound = null;
	}
}
