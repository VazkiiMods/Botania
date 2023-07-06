/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.client.gui.ManaBarTooltipComponent;

import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsFabricMixin {
	@Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderManaBar(Font font, List<ClientTooltipComponent> components, int oldX, int oldY, ClientTooltipPositioner positioner, CallbackInfo ci, int width, int height, int i, int j, Vector2ic vector2ic, int x, int y) {
		for (ClientTooltipComponent component : components) {
			if (component instanceof ManaBarTooltipComponent manaBar) {
				manaBar.setContext(x, y, width);
			}
		}
	}
}
