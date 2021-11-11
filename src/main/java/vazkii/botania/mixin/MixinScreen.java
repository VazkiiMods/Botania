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

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.client.core.handler.TooltipAdditionDisplayHandler;
import vazkii.botania.client.gui.ManaBarTooltipComponent;

import java.util.List;

@Mixin(Screen.class)
public class MixinScreen {
	@Shadow
	protected Font font;

	@Inject(method = "renderTooltipInternal", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderManaBar(PoseStack poseStack, List<ClientTooltipComponent> list, int oldX, int oldY, CallbackInfo ci, int w, int h, int x, int y) {
		for (ClientTooltipComponent component : list) {
			if (component instanceof ManaBarTooltipComponent manaBar) {
				TooltipAdditionDisplayHandler.onToolTipRender(poseStack, manaBar, w, x, y, font);
				return;
			}
		}
	}
}
