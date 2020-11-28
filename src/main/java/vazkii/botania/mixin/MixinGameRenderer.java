/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

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
			target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
			shift = At.Shift.AFTER
		),
		method = "renderWorld"
	)
	private void renderWorldLast(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		ItemCraftingHalo.onRenderWorldLast(tickDelta, matrix);
		LightningHandler.onRenderWorldLast(tickDelta, matrix);
		BoundTileRenderer.onWorldRenderLast(matrix);
		AstrolabePreviewHandler.onWorldRenderLast(matrix);
		RenderMagicLandmine.onWorldRenderLast();
	}
}
