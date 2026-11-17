/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.gui.monocle;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.helper.FilterHelper;

import java.util.List;

public record ItemFrameHud(ItemFrame itemFrame) implements MonocleHud {
	private static final int MAX_CONTENTS_COLUMNS = 9;
	private static final int MAX_CONTENTS_ROWS = 3;
	public static final int TEXT_ROW_HEIGHT = 12;

	@Override
	public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
		if (!itemFrame.getItem().isEmpty()) {
			ItemStack frameItem = itemFrame.getItem();
			List<ItemStack> contentItems = FilterHelper.getFilterStacks(frameItem);
			if (contentItems.isEmpty() ||
					contentItems.size() == 1 &&
							ItemStack.isSameItemSameComponents(frameItem, contentItems.getFirst())) {
				return;
			}

			Minecraft mc = Minecraft.getInstance();
			int x = mc.getWindow().getGuiScaledWidth() / 2 + 15;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 24;
			int maxWidth = mc.getWindow().getGuiScaledWidth() - x - 30;

			MutableComponent itemName = Component.empty().append(frameItem.getHoverName())
					.withStyle(frameItem.getRarity().color());
			if (frameItem.has(DataComponents.CUSTOM_NAME)) {
				itemName.withStyle(ChatFormatting.ITALIC);
			}
			MutableComponent text = Component.translatable("botaniamisc.monocle.frame.contains", itemName);

			List<FormattedCharSequence> lines = mc.font.split(text, maxWidth);
			int textWidth = lines.stream().mapToInt(mc.font::width).max().orElseThrow();
			int textYOffset = (lines.size() - 1) * TEXT_ROW_HEIGHT;

			int contentsWidth = Math.min(MAX_CONTENTS_COLUMNS, contentItems.size()) * 18;
			int contentsHeight = Math.min(MAX_CONTENTS_ROWS + 1,
					(contentItems.size() - 1) / MAX_CONTENTS_COLUMNS + 1) * 18;
			RenderHelper.renderHUDBox(gui, x - 4, y - 4,
					x + Math.max(textWidth, contentsWidth) + 24, y + textYOffset + contentsHeight + 20);
			gui.renderItem(frameItem, x, y);

			int textRow = 0;
			for (var line : lines) {
				gui.drawString(mc.font, line, x + 20, y + TEXT_ROW_HEIGHT * textRow + 4, 0xFFFFFF);
				textRow++;
			}

			int row = 1;
			int column = 0;
			for (ItemStack contentItem : contentItems) {
				if (++column > MAX_CONTENTS_COLUMNS) {
					column = 1;
					if (++row > MAX_CONTENTS_ROWS) {
						break;
					}
				}

				gui.renderItem(contentItem, x + 18 * column, y + 18 * row + textYOffset);
				gui.renderItemDecorations(mc.font, contentItem, x + 18 * column, y + 18 * row + textYOffset);
			}

			if (row > MAX_CONTENTS_ROWS) {
				MutableComponent remainingItemsHint = Component.translatable(
						"botaniamisc.monocle.frame.additional_stacks",
						contentItems.size() - MAX_CONTENTS_COLUMNS * MAX_CONTENTS_ROWS);
				gui.drawString(mc.font, remainingItemsHint, x + 24, y + 18 * row + 6 + textYOffset, 0xFFFFFF);
			}
		}
	}
}
