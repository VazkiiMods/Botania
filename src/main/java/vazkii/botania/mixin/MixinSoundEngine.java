/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.util.SoundCategory;

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
	private ISound tmpSound;

	@Inject(at = @At("HEAD"), method = "getClampedVolume")
	private void captureSound(ISound sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = sound;
	}

	@Unique
	private static boolean shouldSilence(ISound sound) {
		return sound.getCategory() != SoundCategory.VOICE
				&& sound.getCategory() != SoundCategory.MUSIC
				&& sound.getCategory() != SoundCategory.RECORDS
				&& sound.getCategory() != SoundCategory.AMBIENT;
	}

	@ModifyArg(index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"), method = "getClampedVolume")
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

	@Inject(at = @At("RETURN"), method = "getClampedVolume")
	private void clearSound(ISound sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = null;
	}
}
