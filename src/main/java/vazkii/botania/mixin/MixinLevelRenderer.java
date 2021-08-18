/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.SkyblockWorldInfo;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nullable;

/**
 * This Mixin implements the Garden of Glass skybox
 */
@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
	@Shadow
	@Nullable
	private VertexBuffer starBuffer;

	@Unique
	private static boolean isGogSky() {
		Level world = Minecraft.getInstance().level;
		boolean isGog = world.getLevelData() instanceof SkyblockWorldInfo && ((SkyblockWorldInfo) world.getLevelData()).isGardenOfGlass();
		return ConfigHandler.CLIENT.enableFancySkybox.getValue()
				&& world.dimension() == Level.OVERWORLD
				&& (ConfigHandler.CLIENT.enableFancySkyboxInNormalWorlds.getValue() || isGog);
	}

	/**
	 * Render planets and other extras, after the first invoke to ms.rotate(Y) after getRainStrength is called
	 */
	@Inject(
		method = "renderSky",
		slice = @Slice(
			from = @At(
				ordinal = 0, value = "INVOKE",
				target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"
			)
		),
		at = @At(
			shift = At.Shift.AFTER,
			ordinal = 0,
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V"
		),
		require = 0
	)
	private void renderExtras(PoseStack ms, Matrix4f projMat, float partialTicks, Runnable resetFog, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderExtra(ms, Minecraft.getInstance().level, partialTicks, 0);
		}
	}

	/**
	 * Make the sun bigger, replace any 30.0F seen before first call to bindTexture
	 */
	@ModifyConstant(
		method = "renderSky",
		slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindForSetup(Lnet/minecraft/resources/ResourceLocation;)V")),
		constant = { @Constant(floatValue = 30.0F) },
		require = 0
	)
	private float makeSunBigger(float oldValue) {
		if (isGogSky()) {
			return 60.0F;
		} else {
			return oldValue;
		}
	}

	/**
	 * Make the moon bigger, replace any 20.0F seen between first and second call to bindTexture
	 */
	@ModifyConstant(
		method = "renderSky",
		slice = @Slice(
			from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindForSetup(Lnet/minecraft/resources/ResourceLocation;)V"),
			to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindForSetup(Lnet/minecraft/resources/ResourceLocation;)V")
		),
		constant = @Constant(floatValue = 20.0F),
		require = 0
	)
	private float makeMoonBigger(float oldValue) {
		if (isGogSky()) {
			return 60.0F;
		} else {
			return oldValue;
		}
	}

	/**
	 * Render lots of extra stars
	 */
	@Inject(
		method = "renderSky",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"),
		require = 0
	)
	private void renderExtraStars(PoseStack ms, Matrix4f projMat, float partialTicks, Runnable resetFog, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderStars(starBuffer, ms, projMat, partialTicks, resetFog);
		}
	}
}
