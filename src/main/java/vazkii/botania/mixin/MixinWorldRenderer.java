/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
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
	private VertexBuffer starVBO;

	@Unique
	private static boolean isGogSky() {
		World world = Minecraft.getInstance().world;
		boolean isGog = world.getWorldInfo() instanceof SkyblockWorldInfo && ((SkyblockWorldInfo) world.getWorldInfo()).isGardenOfGlass();
		return ConfigHandler.CLIENT.enableFancySkybox.get()
				&& world.func_234923_W_() == World.field_234918_g_
				&& (ConfigHandler.CLIENT.enableFancySkyboxInNormalWorlds.get() || isGog);
	}

	/**
	 * Render planets and other extras, after the first invoke to ms.rotate(Y) after getRainStrength is called
	 */
	@Inject(
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		slice = @Slice(
			from = @At(
				ordinal = 0, value = "INVOKE",
				target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"
			)
		),
		at = @At(
			shift = At.Shift.AFTER,
			ordinal = 0,
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/matrix/MatrixStack;rotate(Lnet/minecraft/util/math/vector/Quaternion;)V"
		)
	)
	private void renderExtras(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderExtra(ms, Minecraft.getInstance().world, partialTicks, 0);
		}
	}

	/**
	 * Make the sun bigger, replace any 30.0F seen before first call to bindTexture
	 */
	@ModifyConstant(
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")),
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
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		slice = @Slice(
			from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"),
			to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
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
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F")
	)
	private void renderExtraStars(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderStars(skyVertexFormat, starVBO, ms, partialTicks);
		}
	}
}
