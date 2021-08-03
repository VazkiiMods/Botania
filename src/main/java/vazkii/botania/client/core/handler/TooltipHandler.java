/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import vazkii.botania.common.item.ItemKeepIvy;

import java.util.List;

public final class TooltipHandler {

	public static void onTooltipEvent(ItemStack stack, TooltipFlag ctx, List<Component> tooltip) {
		if (ItemKeepIvy.hasIvy(stack)) {
			tooltip.add(new TranslatableComponent("botaniamisc.hasKeepIvy"));
		}
	}

	public static Component getShiftInfoTooltip() {
		Component shift = new TextComponent("SHIFT").withStyle(ChatFormatting.AQUA);
		return new TranslatableComponent("botaniamisc.shiftinfo", shift).withStyle(ChatFormatting.GRAY);
	}

	public static void addOnShift(List<Component> tooltip, Runnable lambda) {
		if (Screen.hasShiftDown()) {
			lambda.run();
		} else {
			tooltip.add(getShiftInfoTooltip());
		}
	}

}
