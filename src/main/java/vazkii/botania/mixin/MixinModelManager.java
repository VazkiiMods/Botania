/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.MiscellaneousIcons;

import java.util.Map;

@Mixin(ModelManager.class)
public class MixinModelManager {
	@Shadow
	private Map<ResourceLocation, BakedModel> bakedRegistry;

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/ModelBakery;getBakedTopLevelModels()Ljava/util/Map;", shift = At.Shift.AFTER), method = "apply")
	private void onModelBake(ModelBakery modelLoader, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
		MiscellaneousIcons.INSTANCE.onModelBake(modelLoader, this.bakedRegistry);
	}
}
