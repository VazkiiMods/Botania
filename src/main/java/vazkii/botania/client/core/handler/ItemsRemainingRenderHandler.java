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

import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class ItemsRemainingRenderHandler {

	private static int maxTicks = 30;
	private static int leaveTicks = 20;

	private static ItemStack stack;
	private static int ticks, count;

	@SideOnly(Side.CLIENT)
	public static void render(ScaledResolution resolution, float partTicks) {
		if(ticks > 0 && stack != null) {
			int pos = maxTicks - ticks;
			Minecraft mc = Minecraft.getMinecraft();
			int x = resolution.getScaledWidth() / 2 + 10 + Math.max(0, pos - leaveTicks);
			int y = resolution.getScaledHeight() / 2;

			int start = maxTicks - leaveTicks;
			float alpha = ticks + partTicks > start ? 1F : (ticks + partTicks) / start;

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glColor4f(1F, 1F, 1F, alpha);
			RenderHelper.enableGUIStandardItemLighting();
			int xp = x + (int) (16F * (1F - alpha));
			GL11.glTranslatef(xp, y, 0F);
			GL11.glScalef(alpha, 1F, 1F);
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0);
			GL11.glScalef(1F / alpha,1F, 1F);
			GL11.glTranslatef(-xp, -y, 0F);
			RenderHelper.disableStandardItemLighting();
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glEnable(GL11.GL_BLEND);

			String text = EnumChatFormatting.GREEN + stack.getDisplayName();
			if(count >= 0) {
				int max = stack.getMaxStackSize();
				int stacks = count / max;
				int rem = count % max;

				if(stacks == 0)
					text = "" + count;
				else text = count + " (" + EnumChatFormatting.AQUA + stacks + EnumChatFormatting.RESET + "*" + EnumChatFormatting.GRAY + max + EnumChatFormatting.RESET + "+" + EnumChatFormatting.YELLOW + rem + EnumChatFormatting.RESET + ")";
			} else if(count == -1)
				text = "\u221E";

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 6, color);

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void tick() {
		if(ticks > 0)
			--ticks;
	}

	public static void set(ItemStack stack, int count) {
		ItemsRemainingRenderHandler.stack = stack;
		ItemsRemainingRenderHandler.count = count;
		ticks = stack == null ? 0 : maxTicks;
	}

	public static void set(EntityPlayer player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null && pattern.matcher(stack.getUnlocalizedName()).find())
				count += stack.stackSize;
		}

		set(displayStack, count);
	}

}
