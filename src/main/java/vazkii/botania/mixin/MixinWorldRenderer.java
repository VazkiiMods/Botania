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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.vector.Vector3f;
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
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelHoe;

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
				&& world.getDimensionKey() == World.OVERWORLD
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
		),
		require = 0
	)
	private void renderExtras(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderExtra(ms, Minecraft.getInstance().world, partialTicks, 0);
		}
	}

	/**
	 * Tilt the ground visually when it has been tilted by a hoe.
	 */
	@Inject(
		method = "renderBlockLayer(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/matrix/MatrixStack;DDD)V",
		at = @At("HEAD")
	)
	private void tiltGround(RenderType blockLayerIn, MatrixStack matrixStackIn, double xIn, double yIn, double zIn, CallbackInfo ci) {
		if (ItemManasteelHoe.shouldTilt()) {
			matrixStackIn.push();
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(5));
		}
	}

	/**
	 * Tilt the ground back after it has been tilted by
	 * {@link #tiltGround(RenderType, MatrixStack, double, double, double, CallbackInfo)}.
	 */
	@Inject(
		method = "renderBlockLayer(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/matrix/MatrixStack;DDD)V",
		at = @At("TAIL")
	)
	private void untiltGround(RenderType blockLayerIn, MatrixStack matrixStackIn, double xIn, double yIn, double zIn, CallbackInfo ci) {
		if (ItemManasteelHoe.shouldTilt()) {
			matrixStackIn.pop();
		}
	}

	/**
	 * Make the sun bigger, replace any 30.0F seen before first call to bindTexture
	 */
	@ModifyConstant(
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")),
		constant = @Constant(floatValue = 30.0F),
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
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		slice = @Slice(
			from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"),
			to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
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
		method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F"),
		require = 0
	)
	private void renderExtraStars(MatrixStack ms, float partialTicks, CallbackInfo ci) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderStars(skyVertexFormat, starVBO, ms, partialTicks);
		}
	}
}
