/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.client.gui.ManaBarTooltipComponent;

import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsFabricMixin {
	@WrapOperation(
		method = "renderTooltipInternal",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"
		)
	)
	private Vector2ic determineManaBarContext(ClientTooltipPositioner instance, int screenWidth, int screenHeight,
			int mouseX, int mouseY, int tooltipWidth, int tooltipHeight, Operation<Vector2ic> original,
			@Local(argsOnly = true) List<ClientTooltipComponent> components) {

		Vector2ic result = original.call(instance, screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
		for (ClientTooltipComponent component : components) {
			if (component instanceof ManaBarTooltipComponent manaBar) {
				manaBar.setContext(result.x(), result.y(), tooltipWidth);
			}
		}
		return result;
	}
}
