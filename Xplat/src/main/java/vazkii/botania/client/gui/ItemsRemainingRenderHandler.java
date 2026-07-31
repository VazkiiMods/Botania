/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.client.integration.shared.LocaleHelper;
import vazkii.botania.network.clientbound.UpdateItemsRemainingPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.text.NumberFormat;

public final class ItemsRemainingRenderHandler {

	private static final int MAX_TICKS = 30;
	private static final int LEAVE_TICKS = 20;

	private static ItemStack stack = ItemStack.EMPTY;
	@Nullable
	private static Component customString;
	private static int ticks, count;

	public static void render(GuiGraphics gui, float partialTick) {
		PoseStack ms = gui.pose();
		if (ticks > 0 && !stack.isEmpty()) {
			int pos = MAX_TICKS - ticks;
			Minecraft mc = Minecraft.getInstance();
			int x = mc.getWindow().getGuiScaledWidth() / 2 + 10 + Math.max(0, pos - LEAVE_TICKS);
			int y = mc.getWindow().getGuiScaledHeight() / 2;

			int start = MAX_TICKS - LEAVE_TICKS;
			float alpha = ticks + partialTick > start ? 1F : (ticks + partialTick) / start;

			// RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
			int xp = x + (int) (16F * (1F - alpha));
			ms.pushPose();
			ms.translate(xp, y, 0F);
			ms.scale(alpha, 1F, 1F);
			gui.renderItem(stack, 0, 0);
			ms.popPose();

			Component text = customString != null ? customString : getRemainingCount(stack, count);

			int color = FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), 0xFFFFFF);
			gui.drawString(mc.font, text, x + 20, y + 6, color);
		}
	}

	private static Component getRemainingCount(ItemStack displayStack, int totalCount) {
		if (displayStack.isEmpty()) {
			return Component.empty();
		}
		if (totalCount == -1) {
			return Component.literal("\u221E");
		}
		if (totalCount >= 0) {
			int max = displayStack.getMaxStackSize();
			int stacks = totalCount / max;
			NumberFormat format = LocaleHelper.getIntegerFormat();

			String totalCountText = format.format(totalCount);
			if (stacks == 0) {
				return Component.literal(totalCountText);
			}
			int rem = totalCount % max;
			Component stacksText = Component.literal(format.format(stacks)).withStyle(ChatFormatting.AQUA);
			Component maxText = Component.literal(format.format(max)).withStyle(ChatFormatting.GRAY);
			if (rem > 0) {
				Component remText = Component.literal(format.format(rem)).withStyle(ChatFormatting.YELLOW);
				return Component.translatable("botaniamisc.template.parenthesis_suffix", totalCountText,
						Component.translatable("botaniamisc.count.stacks_with_remainder", stacksText, maxText, remText));
			}
			return Component.translatable("botaniamisc.template.parenthesis_suffix", totalCountText,
					Component.translatable("botaniamisc.count.stacks_no_remainder", stacksText, maxText));
		}
		return Component.empty();
	}

	public static void tick() {
		if (ticks > 0) {
			--ticks;
		}
	}

	public static void send(@Nullable Player player, ItemStack displayStack, int count) {
		send(player, displayStack, count, null);
	}

	public static void set(ItemStack stack, int count, @Nullable Component str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.isEmpty() ? 0 : MAX_TICKS;
	}

	public static void send(@Nullable Player player, ItemStack displayStack, int count, @Nullable Component str) {
		XplatAbstractions.INSTANCE.sendToPlayer(player, new UpdateItemsRemainingPacket(displayStack, count, str));
	}

	public static void send(Player player, ItemStack displayStack, TagKey<Item> itemTag) {
		int count = 0;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && stack.is(itemTag)) {
				count += stack.getCount();
			}
		}

		send(player, displayStack, count, null);
	}
}
