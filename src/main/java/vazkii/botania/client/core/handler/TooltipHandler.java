/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import vazkii.botania.common.item.ItemKeepIvy;

import java.util.List;

public final class TooltipHandler {

	public static void onTooltipEvent(ItemTooltipEvent event) {
		if (ItemKeepIvy.hasIvy(event.getItemStack())) {
			event.getToolTip().add(new TranslationTextComponent("botaniamisc.hasKeepIvy"));
		}
	}

	public static ITextComponent getShiftInfoTooltip() {
		ITextComponent shift = new StringTextComponent("SHIFT").func_240699_a_(TextFormatting.AQUA);
		return new TranslationTextComponent("botaniamisc.shiftinfo", shift).func_240699_a_(TextFormatting.GRAY);
	}

	public static void addOnShift(List<ITextComponent> tooltip, Runnable lambda) {
		if (Screen.hasShiftDown()) {
			lambda.run();
		} else {
			tooltip.add(getShiftInfoTooltip());
		}
	}

}
