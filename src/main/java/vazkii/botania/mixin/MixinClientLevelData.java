/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.multiplayer.ClientLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.client.core.SkyblockWorldInfo;

@Mixin(ClientLevel.ClientLevelData.class)
public abstract class MixinClientLevelData implements SkyblockWorldInfo {
	private boolean gardenOfGlass;

	@Override
	public boolean isGardenOfGlass() {
		return gardenOfGlass;
	}

	@Override
	public void markGardenOfGlass() {
		gardenOfGlass = true;
	}

	@Inject(at = @At("HEAD"), method = "getHorizonHeight", cancellable = true)
	private void gogHorizon(CallbackInfoReturnable<Double> cir) {
		if (gardenOfGlass) {
			cir.setReturnValue(0.0);
		}
	}

	@Inject(at = @At("HEAD"), method = "getClearColorScale", cancellable = true)
	private void gogFog(CallbackInfoReturnable<Double> cir) {
		if (gardenOfGlass) {
			cir.setReturnValue(1.0);
		}
	}

}
