/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 2, 2015, 5:34:05 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

public class GuiButtonViewOnline extends GuiButtonLexicon {

	public GuiButtonViewOnline(int id, int x, int y) {
		super(id, x, y, 11, 11, "");
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
		int k = getHoverState(field_146123_n);

		par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(xPosition, yPosition, k == 2 ? 41 : 30, 200, 11, 11);

		List<String> tooltip = Arrays.asList(EnumChatFormatting.AQUA + StatCollector.translateToLocal("botaniamisc.viewOnline"));
		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
	}

}
