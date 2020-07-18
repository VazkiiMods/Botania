/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.util.Splashes;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

@Mixin(Splashes.class)
public class MixinSplashes {
	@Shadow
	@Final
	private List<String> possibleSplashes;

	@Inject(at = @At("RETURN"), method = "apply")
	public void addSplashes(List<String> splashes, IResourceManager resourceManager, IProfiler profiler, CallbackInfo ci) {
		if (ConfigHandler.CLIENT.splashesEnabled.get()) {
			possibleSplashes.add("Do not feed bread to elves!");
		}
	}
}
