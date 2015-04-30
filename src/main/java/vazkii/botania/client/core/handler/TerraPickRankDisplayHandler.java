/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 23, 2015, 4:24:56 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import vazkii.botania.common.item.equipment.tool.ItemTerraPick;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public final class TerraPickRankDisplayHandler {

	public static void render() {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui != null && gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			Slot slot = ReflectionHelper.getPrivateValue(GuiContainer.class, container, LibObfuscation.THE_SLOT);
			if(slot != null && slot.getHasStack()) {
				ItemStack stack = slot.getStack();
				if(stack != null && stack.getItem() instanceof ItemTerraPick) {
					ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
					FontRenderer font = mc.fontRenderer;
					int mouseX = Mouse.getX() * res.getScaledWidth() / mc.displayWidth;
					int mouseY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight;

					List<String> tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
					int width = 0;
					for(String s : tooltip)
						width = Math.max(width, font.getStringWidth(s));

					int offx = 11;
					int offy = 17;
					int height = 3;

					int level = ItemTerraPick.getLevel(stack);
					int max = ItemTerraPick.LEVELS[Math.min(ItemTerraPick.LEVELS.length - 1, level + 1)];
					boolean ss = level >= ItemTerraPick.LEVELS.length - 1;
					int curr = ItemTerraPick.getMana_(stack);
					float percent = level == 0 ? 0F : (float) curr / (float) max;
					int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percent));
					float huePer = width == 0 ? 0F : 1F / width;
					float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

					GL11.glDisable(GL11.GL_DEPTH_TEST);
					Gui.drawRect(mouseX + offx - 1, mouseY - offy - height - 1, mouseX + offx + width + 1, mouseY - offy, 0xFF000000);
					for(int i = 0; i < rainbowWidth; i++)
						Gui.drawRect(mouseX + offx + i, mouseY - offy - height, mouseX + offx + i + 1, mouseY - offy, Color.HSBtoRGB(hueOff + huePer * i, 1F, 1F));
					Gui.drawRect(mouseX + offx + rainbowWidth, mouseY - offy - height, mouseX + offx + width, mouseY - offy, 0xFF555555);

					String rank = StatCollector.translateToLocal("botania.rank" + level).replaceAll("&", "\u00a7");
					boolean light = GL11.glGetBoolean(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_LIGHTING);
					font.drawStringWithShadow(rank, mouseX + offx, mouseY - offy - 12, 0xFFFFFF);
					if(!ss) {
						rank = StatCollector.translateToLocal("botania.rank" + (level + 1)).replaceAll("&", "\u00a7");
						font.drawStringWithShadow(rank, mouseX + offx + width - font.getStringWidth(rank), mouseY - offy - 12, 0xFFFFFF);
					}

					if(light)
						GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
				}
			}
		}
	}

}
