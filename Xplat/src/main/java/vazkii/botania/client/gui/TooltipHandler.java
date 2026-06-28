/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import vazkii.botania.common.item.ResoluteIvyItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public final class TooltipHandler {

	public static void onTooltipEvent(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> lines) {
		if (ResoluteIvyItem.hasIvy(stack)) {
			lines.add(Component.translatable("botaniamisc.hasKeepIvy"));
		}
	}

	public static Component getShiftInfoTooltip() {
		Component shift = Component.translatable("key.keyboard.shift").withStyle(ChatFormatting.AQUA);
		return Component.translatable("botaniamisc.shiftinfo", shift).withStyle(ChatFormatting.GRAY);
	}

	public static void addOnShift(List<Component> tooltip, TooltipFlag flags, Runnable lambda) {
		if (XplatAbstractions.instance().shouldShowExtendedItemTooltip(flags)) {
			lambda.run();
		} else {
			tooltip.add(getShiftInfoTooltip());
		}
	}

}
