/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.handler.BotaniaSounds;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	public LocalPlayer player;

	/**
	 * Check for active gaia fight involving the local player, and return the appropriate gaia fight music.
	 */
	@Inject(
		method = "getSituationalMusic",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;level()Lnet/minecraft/world/level/Level;",
			ordinal = 0
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
				opcode = Opcodes.GETFIELD
			),
			to = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/gui/Gui;getBossOverlay()Lnet/minecraft/client/gui/components/BossHealthOverlay;"
			)
		),
		cancellable = true
	)
	void checkForGaiaFight(CallbackInfoReturnable<Music> cir) {
		Boolean gaiaFightHardMode = GaiaGuardianEntity.isGaiaFightHardMode(this.player);
		if (gaiaFightHardMode != null) {
			cir.setReturnValue(botania_getGaiaMusic(gaiaFightHardMode));
		}
	}

	@Unique
	private static Music botania_getGaiaMusic(Boolean gaiaFightHardMode) {
		return gaiaFightHardMode ? BotaniaSounds.GAIA2_BOSS : BotaniaSounds.GAIA1_BOSS;
	}
}
