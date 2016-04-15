package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonBookmark extends GuiButtonLexicon {

	GuiLexicon gui;

	public GuiButtonBookmark(int par1, int par2, int par3, GuiLexicon gui, String str) {
		super(par1, par2, par3, gui.bookmarkWidth(str) + 5, 11, str);
		this.gui = gui;
	}

	@Override
	public void drawButton(Minecraft mc, int par2, int par3) {
		gui.drawBookmark(xPosition, yPosition, displayString, false);
		hovered = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
		int k = getHoverState(hovered);

		List<String> tooltip = new ArrayList<>();
		if(displayString.equals("+"))
			tooltip.add(I18n.translateToLocal("botaniamisc.clickToAdd"));
		else {
			tooltip.add(String.format(I18n.translateToLocal("botaniamisc.bookmark"), id - GuiLexicon.BOOKMARK_START + 1));
			tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("botaniamisc.clickToSee"));
			tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("botaniamisc.shiftToRemove"));
		}

		int tooltipY = (tooltip.size() + 1) * 5;
		if(k == 2)
			RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
	}

}
