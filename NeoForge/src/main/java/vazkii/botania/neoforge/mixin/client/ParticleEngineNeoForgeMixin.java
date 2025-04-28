package vazkii.botania.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.fx.BotaniaParticleRenderType;

import java.util.function.Predicate;

@Mixin(ParticleEngine.class)
public class ParticleEngineNeoForgeMixin {
	// FIXME, HACK: ParticleRenderType no longer has an end() method, but our particle render types need to reset things
	// (presumably the render types should define shaders instead of doing things they need to reset afterwards)
	@Inject(
		method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V",
		at = @At(value = "JUMP", opcode = Opcodes.GOTO),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V"),
			to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V")
		)
	)
	private void afterRenderedType(LightTexture lightTexture, Camera camera, float partialTick, Frustum frustum,
			Predicate<ParticleRenderType> renderTypePredicate, CallbackInfo ci,
			@Local ParticleRenderType particleRenderType) {
		if (particleRenderType instanceof BotaniaParticleRenderType botaniaParticleRenderType) {
			botaniaParticleRenderType.end();
		}
	}
}
