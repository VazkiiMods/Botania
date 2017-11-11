/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 2, 2015, 6:01:26 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonHistory extends GuiButtonLexicon {

	final GuiLexicon gui;

	public GuiButtonHistory(int par1, int par2, int par3, String str, GuiLexicon gui) {
		super(par1, par2, par3, gui.bookmarkWidth(str) + 5, 11, str);
		this.gui = gui;
	}

	@Override
	public void drawButton(@Nonnull Minecraft mc, int par2, int par3, float partialTicks) {
		gui.drawBookmark(x, y, displayString, false);
		hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
		int k = getHoverState(hovered);

		List<String> tooltip = new ArrayList<>();
		tooltip.add(I18n.format("botaniamisc.historyLong"));
		tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.historyDesc"));

		int tooltipY = (tooltip.size() + 1) * 5;
		if(k == 2)
			RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
	}

}
