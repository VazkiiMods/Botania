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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import vazkii.botania.common.item.ItemKeepIvy;

import java.util.List;

public final class TooltipHandler {

	public static void onTooltipEvent(ItemTooltipEvent event) {
		if (ItemKeepIvy.hasIvy(event.getItemStack())) {
			event.getToolTip().add(new TranslatableText("botaniamisc.hasKeepIvy"));
		}
	}

	public static Text getShiftInfoTooltip() {
		Text shift = new LiteralText("SHIFT").formatted(Formatting.AQUA);
		return new TranslatableText("botaniamisc.shiftinfo", shift).formatted(Formatting.GRAY);
	}

	public static void addOnShift(List<Text> tooltip, Runnable lambda) {
		if (Screen.hasShiftDown()) {
			lambda.run();
		} else {
			tooltip.add(getShiftInfoTooltip());
		}
	}

}
