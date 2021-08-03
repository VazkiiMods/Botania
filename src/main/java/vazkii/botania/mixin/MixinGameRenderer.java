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

import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.AstrolabePreviewHandler;
import vazkii.botania.client.core.handler.BoundTileRenderer;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.common.item.ItemCraftingHalo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V",
			shift = At.Shift.AFTER
		),
		method = "renderLevel"
	)
	private void renderWorldLast(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo ci) {
		ItemCraftingHalo.onRenderWorldLast(tickDelta, matrix);
		LightningHandler.onRenderWorldLast(tickDelta, matrix);
		BoundTileRenderer.onWorldRenderLast(matrix);
		AstrolabePreviewHandler.onWorldRenderLast(matrix);
		RenderMagicLandmine.onWorldRenderLast();
	}
}
