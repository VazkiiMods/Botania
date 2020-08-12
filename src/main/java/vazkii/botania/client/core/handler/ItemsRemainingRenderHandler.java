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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketUpdateItemsRemaining;

import javax.annotation.Nullable;

import java.util.regex.Pattern;

public final class ItemsRemainingRenderHandler {

	private static final int maxTicks = 30;
	private static final int leaveTicks = 20;

	private static ItemStack stack = ItemStack.EMPTY;
	@Nullable
	private static Text customString;
	private static int ticks, count;

	@Environment(EnvType.CLIENT)
	public static void render(MatrixStack ms, float partTicks) {
		if (ticks > 0 && !stack.isEmpty()) {
			int pos = maxTicks - ticks;
			MinecraftClient mc = MinecraftClient.getInstance();
			int x = mc.getWindow().getScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = mc.getWindow().getScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			// RenderSystem.color4f(1F, 1F, 1F, alpha);
			int xp = x + (int) (16F * (1F - alpha));
			ms.push();
			ms.translate(xp, y, 0F);
			ms.scale(alpha, 1F, 1F);
			RenderSystem.pushMatrix();
			RenderSystem.multMatrix(ms.peek().getModel());
			mc.getItemRenderer().renderInGuiWithOverrides(stack, 0, 0);
			RenderSystem.popMatrix();
			ms.pop();

			Text text = LiteralText.EMPTY;

			if (customString == null) {
				if (!stack.isEmpty()) {
					text = stack.getName().shallowCopy().formatted(Formatting.GREEN);
					if (count >= 0) {
						int max = stack.getMaxCount();
						int stacks = count / max;
						int rem = count % max;

						if (stacks == 0) {
							text = new LiteralText(Integer.toString(count));
						} else {
							Text stacksText = new LiteralText(Integer.toString(stacks)).formatted(Formatting.AQUA);
							Text maxText = new LiteralText(Integer.toString(max)).formatted(Formatting.GRAY);
							Text remText = new LiteralText(Integer.toString(rem)).formatted(Formatting.YELLOW);
							text = new LiteralText(count + " (")
									.append(stacksText)
									.append("*")
									.append(maxText)
									.append("+")
									.append(remText)
									.append(")");
						}
					} else if (count == -1) {
						text = new LiteralText("\u221E");
					}
				}
			} else {
				text = customString;
			}

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.textRenderer.drawWithShadow(ms, text, x + 20, y + 6, color);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void tick() {
		if (ticks > 0) {
			--ticks;
		}
	}

	public static void send(PlayerEntity player, ItemStack stack, int count) {
		send(player, stack, count, null);
	}

	public static void set(ItemStack stack, int count, @Nullable Text str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.isEmpty() ? 0 : maxTicks;
	}

	public static void send(PlayerEntity entity, ItemStack stack, int count, @Nullable Text str) {
		PacketUpdateItemsRemaining.send(entity, stack, count, str);
	}

	public static void send(PlayerEntity player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack stack = player.inventory.getStack(i);
			if (!stack.isEmpty() && pattern.matcher(stack.getTranslationKey()).find()) {
				count += stack.getCount();
			}
		}

		send(player, displayStack, count, null);
	}

}
