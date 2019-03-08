package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonBookmark extends GuiButtonLexicon {

	final GuiLexicon gui;

	public GuiButtonBookmark(int par1, int par2, int par3, GuiLexicon gui, String str) {
		super(par1, par2, par3, gui.bookmarkWidth(str) + 5, 11, str);
		this.gui = gui;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		gui.drawBookmark(x, y, displayString, false);
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getHoverState(hovered);

		List<String> tooltip = new ArrayList<>();
		if(displayString.equals("+"))
			tooltip.add(I18n.format("botaniamisc.clickToAdd"));
		else {
			tooltip.add(I18n.format("botaniamisc.bookmark", id - GuiLexicon.BOOKMARK_START + 1));
			tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.clickToSee"));
			tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.shiftToRemove"));
		}

		int tooltipY = (tooltip.size() + 1) * 5;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}
