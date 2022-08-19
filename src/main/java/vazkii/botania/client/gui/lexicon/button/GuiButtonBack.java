/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 9:54:21 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

public class GuiButtonBack extends GuiButtonLexicon {

	public GuiButtonBack(int par1, int par2, int par3) {
		super(par1, par2, par3, 18, 9, "");
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(enabled) {
			field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
			int k = getHoverState(field_146123_n);

			par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(xPosition, yPosition, 36, k == 2 ? 180 : 189, 18, 9);

			List<String> tooltip = getTooltip();
			int tooltipY = (tooltip.size() - 1) * 10;
			if(k == 2)
				RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
		}
	}

	public List<String> getTooltip() {
		return Arrays.asList(StatCollector.translateToLocal("botaniamisc.back"));
	}

}
