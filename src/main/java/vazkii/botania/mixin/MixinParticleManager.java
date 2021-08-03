/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.fx.FXWisp;

import java.util.List;

@Mixin(ParticleEngine.class)
public class MixinParticleManager {
	@Mutable
	@Final
	@Shadow
	private static List<ParticleRenderType> RENDER_ORDER;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void addTypes(ClientLevel world, TextureManager textureManager, CallbackInfo ci) {
		RENDER_ORDER = ImmutableList.<ParticleRenderType>builder().addAll(RENDER_ORDER)
				.add(FXWisp.NORMAL_RENDER, FXWisp.DIW_RENDER)
				.add(FXSparkle.NORMAL_RENDER, FXSparkle.CORRUPT_RENDER)
				.add(FXLightning.RENDER)
				.build();
	}
}
