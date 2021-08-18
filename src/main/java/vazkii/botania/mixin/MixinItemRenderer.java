/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.IDurabilityExtension;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
	@Shadow
	protected abstract void fillRect(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

	@Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
	private void renderGuiItemOverlay(Font textRenderer, ItemStack stack, int x, int y, @Nullable String string, CallbackInfo info) {
		//this code adapted from https://github.com/TechReborn/RebornCore/blob/c382cd588b776e77565c74d077e7c183345e721e/src/main/java/reborncore/mixin/client/MixinItemRenderer.java
		if (stack.getItem() instanceof IDurabilityExtension) {
			IDurabilityExtension durabilityExtensions = (IDurabilityExtension) stack.getItem();
			if (!durabilityExtensions.showDurability(stack)) {
				return;
			}
			RenderSystem.disableDepthTest();
			RenderSystem.disableTexture();
			RenderSystem.disableBlend();

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuilder();

			int durability = (int) (13 * (1 - Math.max(0.0F, durabilityExtensions.getDurability(stack))));
			int color = durabilityExtensions.getDurabilityColor(stack);

			this.fillRect(bufferBuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
			this.fillRect(bufferBuilder, x + 2, y + 13, durability, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);

			RenderSystem.enableBlend();
			RenderSystem.enableTexture();
			RenderSystem.enableDepthTest();
		}
	}
}
