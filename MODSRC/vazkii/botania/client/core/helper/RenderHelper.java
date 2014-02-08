/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 5:40:38 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public final class RenderHelper {

	public static void renderTooltip(int x, int y, List<String> tooltipData) {
		int color = 0x505000ff;
		int color2 = 0xf0100010;

		renderTooltip(x, y, tooltipData, color, color2);
	}

	public static void renderTooltipGreen(int x, int y, List<String> tooltipData) {
		int color = 0x5000a000;
		int color2 = 0xf0001e00;

		renderTooltip(x, y, tooltipData, color, color2);
	}

	public static void renderTooltip(int x, int y, List<String> tooltipData, int color, int color2) {
		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		if(lighting)
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		if (!tooltipData.isEmpty()) {
			int var5 = 0;
			int var6;
			int var7;
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			for (var6 = 0; var6 < tooltipData.size(); ++var6) {
				var7 = fontRenderer.getStringWidth(tooltipData.get(var6));
				if (var7 > var5)
					var5 = var7;
			}
			var6 = x + 12;
			var7 = y - 12;
			int var9 = 8;
			if (tooltipData.size() > 1)
				var9 += 2 + (tooltipData.size() - 1) * 10;
			float z = 300.0F;
			drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2);
			drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2);
			drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2);
			drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2);
			drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2);
			int var12 = (color & 0xFFFFFF) >> 1 | color & -16777216;
			drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12);
			drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12);
			drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color);
			drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12);
			for (int var13 = 0; var13 < tooltipData.size(); ++var13) {
				String var14 = tooltipData.get(var13);
				fontRenderer.drawStringWithShadow(var14, var6, var7, -1);
				if (var13 == 0)
					var7 += 2;
				var7 += 10;
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(!lighting)
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public static void drawGradientRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
		float var7 = (par5 >> 24 & 255) / 255.0F;
		float var8 = (par5 >> 16 & 255) / 255.0F;
		float var9 = (par5 >> 8 & 255) / 255.0F;
		float var10 = (par5 & 255) / 255.0F;
		float var11 = (par6 >> 24 & 255) / 255.0F;
		float var12 = (par6 >> 16 & 255) / 255.0F;
		float var13 = (par6 >> 8 & 255) / 255.0F;
		float var14 = (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator var15 = Tessellator.instance;
		var15.startDrawingQuads();
		var15.setColorRGBA_F(var8, var9, var10, var7);
		var15.addVertex(par3, par2, z);
		var15.addVertex(par1, par2, z);
		var15.setColorRGBA_F(var12, var13, var14, var11);
		var15.addVertex(par1, par4, z);
		var15.addVertex(par3, par4, z);
		var15.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawTexturedModalRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(par1 + 0, par2 + par6, z, (par3 + 0) * f, (par4 + par6) * f1);
		tessellator.addVertexWithUV(par1 + par5, par2 + par6, z, (par3 + par5) * f, (par4 + par6) * f1);
		tessellator.addVertexWithUV(par1 + par5, par2 + 0, z, (par3 + par5) * f, (par4 + 0) * f1);
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, z, (par3 + 0) * f, (par4 + 0) * f1);
		tessellator.draw();
	}
}
