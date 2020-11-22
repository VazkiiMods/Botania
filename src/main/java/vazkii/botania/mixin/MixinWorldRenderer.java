/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
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
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow
	@Final
	private VertexFormat skyVertexFormat;

	@Shadow
	@Nullable
	private VertexBuffer starsBuffer;

	@Unique
	private static boolean isGogSky() {
		World world = MinecraftClient.getInstance().world;
		boolean isGog = world.getLevelProperties() instanceof SkyblockWorldInfo && ((SkyblockWorldInfo) world.getLevelProperties()).isGardenOfGlass();
		return ConfigHandler.CLIENT.enableFancySkybox.getValue()
				&& world.getRegistryKey() == World.OVERWORLD
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
				target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"
			)
		),
		at = @At(
			shift = At.Shift.AFTER,
			ordinal = 0,
			value = "INVOKE",
			target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V"
		)
	)
	private void renderExtras(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderExtra(ms, MinecraftClient.getInstance().world, partialTicks, 0);
		}
	}

	/**
	 * Make the sun bigger, replace any 30.0F seen before first call to bindTexture
	 */
	@ModifyConstant(
		method = "renderSky",
		slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V")),
		constant = { @Constant(floatValue = 30.0F) }
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
			from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V"),
			to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V")
		),
		constant = { @Constant(floatValue = 20.0F) }
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
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_23787(F)F")
	)
	private void renderExtraStars(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderStars(skyVertexFormat, starsBuffer, ms, partialTicks);
		}
	}
}
