/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.network.PacketUpdateItemsRemaining;

import javax.annotation.Nullable;

import java.util.regex.Pattern;

public final class ItemsRemainingRenderHandler {

	private static final int maxTicks = 30;
	private static final int leaveTicks = 20;

	private static ItemStack stack = ItemStack.EMPTY;
	@Nullable
	private static Component customString;
	private static int ticks, count;

	@Environment(EnvType.CLIENT)
	public static void render(PoseStack ms, float partTicks) {
		if (ticks > 0 && !stack.isEmpty()) {
			int pos = maxTicks - ticks;
			Minecraft mc = Minecraft.getInstance();
			int x = mc.getWindow().getGuiScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = mc.getWindow().getGuiScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			// RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
			int xp = x + (int) (16F * (1F - alpha));
			ms.pushPose();
			ms.translate(xp, y, 0F);
			ms.scale(alpha, 1F, 1F);
			PoseStack mvStack = RenderSystem.getModelViewStack();
			mvStack.pushPose();
			mvStack.mulPoseMatrix(ms.last().pose());
			RenderSystem.applyModelViewMatrix();
			mc.getItemRenderer().renderAndDecorateItem(stack, 0, 0);
			mvStack.popPose();
			RenderSystem.applyModelViewMatrix();
			ms.popPose();

			Component text = TextComponent.EMPTY;

			if (customString == null) {
				if (!stack.isEmpty()) {
					text = stack.getHoverName().copy().withStyle(ChatFormatting.GREEN);
					if (count >= 0) {
						int max = stack.getMaxStackSize();
						int stacks = count / max;
						int rem = count % max;

						if (stacks == 0) {
							text = new TextComponent(Integer.toString(count));
						} else {
							Component stacksText = new TextComponent(Integer.toString(stacks)).withStyle(ChatFormatting.AQUA);
							Component maxText = new TextComponent(Integer.toString(max)).withStyle(ChatFormatting.GRAY);
							Component remText = new TextComponent(Integer.toString(rem)).withStyle(ChatFormatting.YELLOW);
							text = new TextComponent(count + " (")
									.append(stacksText)
									.append("*")
									.append(maxText)
									.append("+")
									.append(remText)
									.append(")");
						}
					} else if (count == -1) {
						text = new TextComponent("\u221E");
					}
				}
			} else {
				text = customString;
			}

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.font.drawShadow(ms, text, x + 20, y + 6, color);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void tick() {
		if (ticks > 0) {
			--ticks;
		}
	}

	public static void send(Player player, ItemStack stack, int count) {
		send(player, stack, count, null);
	}

	public static void set(ItemStack stack, int count, @Nullable Component str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.isEmpty() ? 0 : maxTicks;
	}

	public static void send(Player entity, ItemStack stack, int count, @Nullable Component str) {
		PacketUpdateItemsRemaining.send(entity, stack, count, str);
	}

	public static void send(Player player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && pattern.matcher(stack.getDescriptionId()).find()) {
				count += stack.getCount();
			}
		}

		send(player, displayStack, count, null);
	}

}
