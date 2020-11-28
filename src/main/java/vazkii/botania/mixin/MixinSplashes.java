/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashes {
	@Shadow
	@Final
	private List<String> splashTexts;

	/**
	 * Adds splash texts if enabled
	 */
	@Inject(at = @At("RETURN"), method = "apply")
	public void addSplashes(List<String> splashes, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
		if (ConfigHandler.CLIENT.splashesEnabled.getValue()) {
			splashTexts.add("Do not feed bread to elves!");
		}
	}
}
