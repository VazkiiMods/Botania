/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 11, 2014, 2:27:15 AM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.TransientScaledResolution;
import vazkii.botania.client.lib.LibResources;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;

public final class InventoryBaublesButtonHandler {

	static ResourceLocation icon = new ResourceLocation(LibResources.GUI_BAUBLES);
	static boolean mouseDown = false;
	
	public static void renderBaublesIcon() {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen != null && (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiPlayerExpanded)) {
			boolean creative = mc.currentScreen instanceof GuiContainerCreative;
			boolean baubles = mc.currentScreen instanceof GuiPlayerExpanded;

			TransientScaledResolution res = new TransientScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int x = res.getScaledWidth() / 2 - 19;
			int y = res.getScaledHeight() / 2 - 75;

			if(creative) {
				GuiContainerCreative container = (GuiContainerCreative) mc.currentScreen;
				if(container.func_147056_g() == CreativeTabs.tabInventory.getTabIndex()) {
					x -= 26;
					y += 13;
				} else return;
			}
			
			if(!mc.thePlayer.getActivePotionEffects().isEmpty())
				x += 60;
				
			RenderHelper.disableStandardItemLighting();
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			mc.renderEngine.bindTexture(icon);
			Tessellator tess = Tessellator.instance;

			tess.startDrawingQuads();
			tess.addVertexWithUV(x * 2, y * 2 + 16, 0, 0, 1);
			tess.addVertexWithUV(x * 2 + 16, y * 2 + 16, 0, 1, 1);
			tess.addVertexWithUV(x * 2 + 16, y * 2, 0, 1, 0);
			tess.addVertexWithUV(x * 2, y * 2, 0, 0, 0);
			tess.draw();

			GL11.glScalef(2F, 2F, 2F);
			GL11.glDisable(GL11.GL_BLEND);

			int mouseX = Mouse.getX() * res.getScaledWidth() / mc.displayWidth;
			int mouseY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight;

			if(mouseX >= x && mouseX < x + 8 && mouseY >= y && mouseY < y + 8) {
				vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mouseX, mouseY, Arrays.asList(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal(baubles ? "botaniamisc.openInv" : "botaniamisc.openBaubles")));

				if(Mouse.isButtonDown(0) && !mouseDown) {
					if(baubles)
						mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
					else PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory(mc.thePlayer));
					mouseDown = true;
				}
			}
 		}
		
		if(mouseDown && !Mouse.isButtonDown(0))
			mouseDown = false;
	}

}
