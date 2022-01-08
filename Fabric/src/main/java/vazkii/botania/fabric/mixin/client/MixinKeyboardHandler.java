/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
	@Inject(at = @At("HEAD"), method = "keyPress")
	private void keyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		if (window == Minecraft.getInstance().getWindow().getWindow()) {
			ItemDodgeRing.ClientLogic.onKeyDown();
			KonamiHandler.handleInput(key, action, modifiers);
		}
	}
}
