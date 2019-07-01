package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.IParented;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonBookmark extends GuiButtonLexicon {
	private final int ordinal;
	final GuiLexicon gui;

	public GuiButtonBookmark(int ordinal, int x, int y, GuiLexicon gui, String str) {
		super(x, y, gui.bookmarkWidth(str) + 5, 11, str, b -> {
			boolean modified = false;
			String key = gui.getNotesKey();
			if(ordinal == GuiLexicon.bookmarks.size()) {
				if(!GuiLexicon.bookmarkKeys.contains(key)) {
					GuiLexicon.bookmarks.add(gui.copy());
					GuiLexicon.bookmarkKeys.add(key);
					modified = true;
				}
			} else {
				if(Screen.hasShiftDown()) {
					GuiLexicon.bookmarks.remove(ordinal);
					GuiLexicon.bookmarkKeys.remove(ordinal);

					modified = true;
				} else {
					GuiLexicon bookmark = GuiLexicon.bookmarks.get(ordinal).copy();
					if(!bookmark.getTitle().equals(gui.getTitle())) {
						Minecraft.getInstance().displayGuiScreen(bookmark);
						if(bookmark instanceof IParented)
							((IParented) bookmark).setParent(gui);
						ClientTickHandler.notifyPageChange();
					}
				}
			}

			gui.bookmarksNeedPopulation = true;
			if(modified)
				PersistentVariableHelper.saveSafe();
		});
		this.ordinal = ordinal;
		this.gui = gui;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		gui.drawBookmark(x, y, getMessage(), false);
		isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getYImage(isHovered());

		List<String> tooltip = new ArrayList<>();
		if(getMessage().equals("+"))
			tooltip.add(I18n.format("botaniamisc.clickToAdd"));
		else {
			tooltip.add(I18n.format("botaniamisc.bookmark", ordinal));
			tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.clickToSee"));
			tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.shiftToRemove"));
		}

		int tooltipY = (tooltip.size() + 1) * 5;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}
