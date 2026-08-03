/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.handler.BotaniaSounds;

@Mixin(MusicManager.class)
public class MusicManagerMixin {
	@Unique
	private boolean botania_playingGaiaFightMusic = false;

	/**
	 * Check for a change away from the gaia fight music as the situationally appropriate music and immediately stop it.
	 */
	@Inject(
		method = "tick", at = @At(
			value = "FIELD", opcode = Opcodes.GETFIELD,
			target = "Lnet/minecraft/client/sounds/MusicManager;currentMusic:Lnet/minecraft/client/resources/sounds/SoundInstance;"
		)
	)
	private void botania_stopGaiaMusicIfFightOver(CallbackInfo ci, @Local @Nullable Music music) {
		if (music != null && botania_isGaiaMusic(music)) {
			botania_playingGaiaFightMusic = true;
		} else if (botania_playingGaiaFightMusic) {
			botania_playingGaiaFightMusic = false;
			((MusicManager) (Object) this).stopPlaying();
		}
	}

	@Unique
	private static boolean botania_isGaiaMusic(Music music) {
		return BotaniaSounds.GAIA_BOSS_MUSIC.contains(music);
	}
}
