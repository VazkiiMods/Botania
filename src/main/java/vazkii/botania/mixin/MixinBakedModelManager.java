package vazkii.botania.mixin;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class MixinBakedModelManager {
	@Shadow private Map<Identifier, BakedModel> models;

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/model/ModelLoader;getBakedModelMap()Ljava/util/Map;", shift = At.Shift.AFTER), method = "apply(Lnet/minecraft/client/render/model/ModelLoader;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V")
	private void onModelBake(ModelLoader modelLoader, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
		MiscellaneousIcons.INSTANCE.onModelBake(modelLoader, this.models);
	}
}
