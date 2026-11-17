/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.fx.BotaniaParticleRenderType;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.fx.FXWisp;

import java.util.List;

@Mixin(ParticleEngine.class)
public class ParticleEngineFabricMixin {
	@Mutable
	@Final
	@Shadow
	private static List<ParticleRenderType> RENDER_ORDER;

	@Inject(at = @At("RETURN"), method = "<clinit>")
	private static void addTypes(CallbackInfo ci) {
		RENDER_ORDER = ImmutableList.<ParticleRenderType>builder().addAll(RENDER_ORDER)
				.add(FXWisp.NORMAL_RENDER, FXWisp.DIW_RENDER)
				.add(FXSparkle.NORMAL_RENDER, FXSparkle.CORRUPT_RENDER)
				.build();
	}

	// FIXME, HACK: ParticleRenderType no longer has an end() method, but our particle render types need to reset things
	// (presumably the render types should define shaders instead of doing things they need to reset afterwards)
	@Inject(
		method = "render",
		at = @At(value = "JUMP", opcode = Opcodes.GOTO, ordinal = 0),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V")
		)
	)
	private void afterRenderedType(LightTexture lightTexture, Camera camera, float partialTick, CallbackInfo ci,
			@Local ParticleRenderType particleRenderType) {
		if (particleRenderType instanceof BotaniaParticleRenderType botaniaParticleRenderType) {
			botaniaParticleRenderType.end();
		}
	}
}
