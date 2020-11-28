/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.gui.hud.DebugHud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.client.core.handler.DebugHandler;

import java.util.List;

@Mixin(DebugHud.class)
public class MixinDebugHud {
	@Inject(at = @At("RETURN"), method = "getLeftText")
	private void addLeftText(CallbackInfoReturnable<List<String>> cir) {
		DebugHandler.onDrawDebugText(cir.getReturnValue());
	}
}
