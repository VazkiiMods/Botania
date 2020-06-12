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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class TooltipHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTooltipEvent(ItemTooltipEvent event) {
		if (ItemKeepIvy.hasIvy(event.getItemStack())) {
			event.getToolTip().add(new TranslationTextComponent("botaniamisc.hasKeepIvy"));
		}
	}

	public static ITextComponent getShiftInfoTooltip() {
		ITextComponent shift = new StringTextComponent("SHIFT").applyTextStyle(TextFormatting.AQUA);
		return new TranslationTextComponent("botaniamisc.shiftinfo", shift).applyTextStyle(TextFormatting.GRAY);
	}

	public static void addOnShift(List<ITextComponent> tooltip, Runnable lambda) {
		if (Screen.hasShiftDown()) {
			lambda.run();
		} else {
			tooltip.add(getShiftInfoTooltip());
		}
	}

}
