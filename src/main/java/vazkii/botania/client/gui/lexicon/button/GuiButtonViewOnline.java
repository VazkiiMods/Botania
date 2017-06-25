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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GuiButtonViewOnline extends GuiButtonLexicon {

	public GuiButtonViewOnline(int id, int x, int y) {
		super(id, x, y, 11, 11, "");
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
		int k = getHoverState(hovered);

		par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
		GlStateManager.color(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x, y, k == 2 ? 41 : 30, 200, 11, 11);

		List<String> tooltip = Collections.singletonList(TextFormatting.AQUA + I18n.format("botaniamisc.viewOnline"));
		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
	}

}
