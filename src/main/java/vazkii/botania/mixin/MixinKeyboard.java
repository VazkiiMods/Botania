/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

@Mixin(Keyboard.class)
public class MixinKeyboard {
	@Inject(at = @At("HEAD"), method = "onKey", cancellable = true)
	private void keyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
			ItemDodgeRing.onKeyDown();
			KonamiHandler.handleInput(key, action, modifiers);
			if (CorporeaInputHandler.buttonPressed(key, scancode)) {
				ci.cancel();
			}
		}
	}
}
