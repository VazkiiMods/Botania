/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketUpdateItemsRemaining;

import javax.annotation.Nullable;

import java.util.regex.Pattern;

public final class ItemsRemainingRenderHandler {

	private static final int maxTicks = 30;
	private static final int leaveTicks = 20;

	private static ItemStack stack = ItemStack.EMPTY;
	@Nullable
	private static ITextComponent customString;
	private static int ticks, count;

	@OnlyIn(Dist.CLIENT)
	public static void render(MatrixStack ms, float partTicks) {
		if (ticks > 0 && !stack.isEmpty()) {
			int pos = maxTicks - ticks;
			Minecraft mc = Minecraft.getInstance();
			int x = mc.getMainWindow().getScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = mc.getMainWindow().getScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			RenderSystem.disableAlphaTest();
			RenderSystem.enableBlend();
			RenderSystem.enableRescaleNormal();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			RenderSystem.color4f(1F, 1F, 1F, alpha);
			int xp = x + (int) (16F * (1F - alpha));
			ms.translate(xp, y, 0F);
			ms.scale(alpha, 1F, 1F);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
			ms.scale(1F / alpha, 1F, 1F);
			ms.translate(-xp, -y, 0F);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.enableBlend();

			ITextComponent text = StringTextComponent.field_240750_d_;

			if (customString == null) {
				if (!stack.isEmpty()) {
					text = stack.getDisplayName().func_230532_e_().func_240699_a_(TextFormatting.GREEN);
					if (count >= 0) {
						int max = stack.getMaxStackSize();
						int stacks = count / max;
						int rem = count % max;

						if (stacks == 0) {
							text = new StringTextComponent(Integer.toString(count));
						} else {
							ITextComponent stacksText = new StringTextComponent(Integer.toString(stacks)).func_240699_a_(TextFormatting.AQUA);
							ITextComponent maxText = new StringTextComponent(Integer.toString(max)).func_240699_a_(TextFormatting.GRAY);
							ITextComponent remText = new StringTextComponent(Integer.toString(rem)).func_240699_a_(TextFormatting.YELLOW);
							text = new StringTextComponent(count + " (")
									.func_230529_a_(stacksText)
									.func_240702_b_("*")
									.func_230529_a_(maxText)
									.func_240702_b_("+")
									.func_230529_a_(remText)
									.func_240702_b_(")");
						}
					} else if (count == -1) {
						text = new StringTextComponent("\u221E");
					}
				}
			} else {
				text = customString;
			}

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.fontRenderer.func_238407_a_(ms, text, x + 20, y + 6, color);

			RenderSystem.disableBlend();
			RenderSystem.enableAlphaTest();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void tick() {
		if (ticks > 0) {
			--ticks;
		}
	}

	public static void send(PlayerEntity player, ItemStack stack, int count) {
		send(player, stack, count, null);
	}

	public static void set(ItemStack stack, int count, @Nullable ITextComponent str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.isEmpty() ? 0 : maxTicks;
	}

	public static void send(PlayerEntity entity, ItemStack stack, int count, @Nullable ITextComponent str) {
		PacketHandler.sendTo((ServerPlayerEntity) entity, new PacketUpdateItemsRemaining(stack, count, str));
	}

	public static void send(PlayerEntity player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && pattern.matcher(stack.getTranslationKey()).find()) {
				count += stack.getCount();
			}
		}

		send(player, displayStack, count, null);
	}

}
