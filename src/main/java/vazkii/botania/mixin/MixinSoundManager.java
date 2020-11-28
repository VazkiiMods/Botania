/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import vazkii.botania.common.block.subtile.functional.BergamuteEventHandler;

/**
 * Implements the wrapping of sounds for the bergamute
 */
@Mixin(SoundManager.class)
public class MixinSoundManager {
	@ModifyArg(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundSystem;play(Lnet/minecraft/client/sound/SoundInstance;)V"),
		method = "play(Lnet/minecraft/client/sound/SoundInstance;)V"
	)
	private SoundInstance mute(SoundInstance sound) {
		return BergamuteEventHandler.onSoundEvent(sound);
	}

	@ModifyArg(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundSystem;play(Lnet/minecraft/client/sound/SoundInstance;I)V"),
		index = 0,
		method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V"
	)
	private SoundInstance muteDelayed(SoundInstance sound) {
		return BergamuteEventHandler.onSoundEvent(sound);
	}
}
