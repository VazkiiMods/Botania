/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.subtile.functional.SubTileBergamute;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
	@Unique
	private SoundInstance tmpSound;

	@Inject(at = @At("HEAD"), method = "calculateVolume")
	private void captureSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = sound;
	}

	@Unique
	private static boolean shouldSilence(SoundInstance sound) {
		return sound.getSource() != SoundSource.VOICE
				&& sound.getSource() != SoundSource.MUSIC
				&& sound.getSource() != SoundSource.RECORDS
				&& sound.getSource() != SoundSource.AMBIENT;
	}

	@ModifyArg(index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), method = "calculateVolume")
	private float bergamuateAttenuate(float volume) {
		if (shouldSilence(tmpSound)) {
			int count = SubTileBergamute.countFlowersAround(tmpSound);
			if (count > 0) {
				// If the multiplier here is adjusted, see also SubTileBergamute.getBergamutesNearby
				return volume * (float) Math.pow(0.5, count);
			}
		}
		return volume;
	}

	@Inject(at = @At("RETURN"), method = "calculateVolume")
	private void clearSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = null;
	}
}
