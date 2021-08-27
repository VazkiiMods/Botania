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

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.client.core.handler.RenderLexicon;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

	@Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
	private void renderFirstPersonItem(LivingEntity livingEntity, ItemStack stack, ItemTransforms.TransformType transformType,
			boolean leftHanded, PoseStack poseStack, MultiBufferSource buffers, int light, CallbackInfo ci) {
		if (RenderLexicon.renderHand(stack, leftHanded, poseStack, buffers, light)) {
			ci.cancel();
		}
	}
}
