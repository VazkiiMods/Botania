/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 23, 2015, 9:22:10 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.regex.Pattern;

public final class ItemsRemainingRenderHandler {

	private static final int maxTicks = 30;
	private static final int leaveTicks = 20;

	private static ItemStack stack = ItemStack.EMPTY;
	private static String customString;
	private static int ticks, count;

	@SideOnly(Side.CLIENT)
	public static void render(ScaledResolution resolution, float partTicks) {
		if(ticks > 0 && !stack.isEmpty()) {
			int pos = maxTicks - ticks;
			Minecraft mc = Minecraft.getMinecraft();
			int x = resolution.getScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = resolution.getScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			GlStateManager.disableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.enableRescaleNormal();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GlStateManager.color(1F, 1F, 1F, alpha);
			RenderHelper.enableGUIStandardItemLighting();
			int xp = x + (int) (16F * (1F - alpha));
			GlStateManager.translate(xp, y, 0F);
			GlStateManager.scale(alpha, 1F, 1F);
			mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
			GlStateManager.scale(1F / alpha,1F, 1F);
			GlStateManager.translate(-xp, -y, 0F);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableBlend();

			String text = "";

			if(customString == null) {
				if(!stack.isEmpty()) {
					text = TextFormatting.GREEN + stack.getDisplayName();
					if(count >= 0) {
						int max = stack.getMaxStackSize();
						int stacks = count / max;
						int rem = count % max;

						if(stacks == 0)
							text = "" + count;
						else text = count + " (" + TextFormatting.AQUA + stacks + TextFormatting.RESET + "*" + TextFormatting.GRAY + max + TextFormatting.RESET + "+" + TextFormatting.YELLOW + rem + TextFormatting.RESET + ")";
					} else if(count == -1)
						text = "\u221E";
				}
			} else text = customString;

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 6, color);

			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void tick() {
		if(ticks > 0)
			--ticks;
	}

	public static void set(ItemStack stack, String str) {
		set(stack, 0, str);
	}

	public static void set(ItemStack stack, int count) {
		set(stack, count, null);
	}

	public static void set(ItemStack stack, int count, String str) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ItemsRemainingRenderHandler.customString = str;
		ticks = stack.isEmpty() ? 0 : maxTicks;
	}

	public static void set(EntityPlayer player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && pattern.matcher(stack.getTranslationKey()).find())
				count += stack.getCount();
		}

		set(displayStack, count);
	}

}
