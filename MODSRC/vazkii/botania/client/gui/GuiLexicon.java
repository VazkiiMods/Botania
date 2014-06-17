/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:48:05 PM (GMT)]
 */
package vazkii.botania.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.button.GuiButtonBookmark;
import vazkii.botania.client.gui.button.GuiButtonInvisible;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ItemLexicon;

public class GuiLexicon extends GuiScreen {

	public static GuiLexicon currentOpenLexicon = new GuiLexicon();
	public static ItemStack stackUsed;

	public static final int BOOKMARK_START = 1337;
	public static List<GuiLexicon> bookmarks = new ArrayList();
	boolean bookmarksNeedPopulation = false;

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEXICON);

	String title;
	int guiWidth = 146;
	int guiHeight = 180;
	int left, top;

	@Override
	public void initGui() {
		super.initGui();

		title = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getDisplayName();
		currentOpenLexicon = this;

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		buttonList.clear();
		if(isIndex()) {
			int x = 18;
			for(int i = 0; i < 12; i++) {
				int y = 16 + i * 12;
				buttonList.add(new GuiButtonInvisible(i, left + x, top + y, 110, 10, ""));
			}
			populateIndex();
		}			
		populateBookmarks();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		drawBookmark(left + guiWidth / 2, top - getTitleHeight(), getTitle(), true);
		String subtitle = getSubtitle();
		if(subtitle != null) {
			GL11.glScalef(0.5F, 0.5F, 1F);
			drawCenteredString(fontRendererObj, subtitle, left * 2 + guiWidth, (top - getTitleHeight() + 11) * 2, 0x00FF00);
			GL11.glScalef(2F, 2F, 1F);
		}

		drawHeader();

		if(bookmarksNeedPopulation) {
			populateBookmarks();
			bookmarksNeedPopulation = false;
		}
		
		super.drawScreen(par1, par2, par3);
	}

	public void drawBookmark(int x, int y, String s, boolean drawLeft) {
		// This function is called from the buttons so I can't use fontRendererObj
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		int l = font.getStringWidth(s.trim());
		int fontOff = 0;
		
		if(!drawLeft) {
			x += l / 2;
			fontOff = 2;
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0F, 0F, 0F, 0.25F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(x + l / 2 + 4, y, 54, 180, 6, 11);
		if(drawLeft)
			drawTexturedModalRect(x - l / 2 - 8, y, 61, 180, 6, 11);
		for(int i = 0; i < l + 6; i++)
			drawTexturedModalRect(x - l / 2 - 2 + i, y, 60, 180, 1, 11);

		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x + l / 2 + 3, y - 1, 54, 180, 6, 11);
		if(drawLeft)
			drawTexturedModalRect(x - l / 2 - 9, y - 1, 61, 180, 6, 11);
		for(int i = 0; i < l + 6; i++)
			drawTexturedModalRect(x - l / 2 - 3 + i, y - 1, 60, 180, 1, 11);
		GL11.glDisable(GL11.GL_BLEND);

		font.drawString(s, x - l / 2 + fontOff, y, 0x111111, false);
		font.setUnicodeFlag(unicode);
	}

	void drawHeader() {
		boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
		fontRendererObj.drawSplitString(String.format(StatCollector.translateToLocal("botania.gui.lexicon.header"), ItemLexicon.getEdition()), left + 18, top + 14, 110, 0);
		fontRendererObj.setUnicodeFlag(unicode);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id >= BOOKMARK_START) 
			handleBookmark(par1GuiButton);
		else {
			int i = par1GuiButton.id - 3;
			if(i < 0)
				return;

			List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
			LexiconCategory category = i >= categoryList.size() ? null : categoryList.get(i);

			if(category != null) {
				mc.displayGuiScreen(new GuiLexiconIndex(category));
				ClientTickHandler.notifyPageChange();
			}
		}
	}

	public void handleBookmark(GuiButton par1GuiButton) {
		int i = par1GuiButton.id - BOOKMARK_START;
		if(i == bookmarks.size() && !bookmarks.contains(this))
			bookmarks.add(this);
		else {
			if(isShiftKeyDown())
				bookmarks.remove(i);
			else {
				GuiLexicon bookmark = bookmarks.get(i);
				if(bookmark instanceof IParented && bookmark != this)
					((IParented) bookmark).setParent(this);
				Minecraft.getMinecraft().displayGuiScreen(bookmark);
				ClientTickHandler.notifyPageChange();
			}
		}
		
		bookmarksNeedPopulation = true;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public int bookmarkWidth(String b) {
		boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
		int width = fontRendererObj.getStringWidth(b) + 15;
		fontRendererObj.setUnicodeFlag(unicode);
		return width;
	}

	String getTitle() {
		return title;
	}

	String getSubtitle() {
		return null;
	}

	int getTitleHeight() {
		return getSubtitle() == null ? 12 : 16;
	}

	boolean isIndex() {
		return true;
	}

	void populateIndex() {
		List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
		for(int i = 3; i < 12; i++) {
			int i_ = i - 3;
			GuiButtonInvisible button = (GuiButtonInvisible) buttonList.get(i);
			LexiconCategory category = i_ >= categoryList.size() ? null : categoryList.get(i_);
			if(category != null)
				button.displayString = StatCollector.translateToLocal(category.getUnlocalizedName());
			else button.displayString = "";
		}
	}

	void populateBookmarks() {
		List remove = new ArrayList();
		List<GuiButton> buttons = buttonList;
		for(GuiButton button : buttons)
			if(button.id >= BOOKMARK_START)
				remove.add(button);
		buttonList.removeAll(remove);
		
		int len = bookmarks.size();
		boolean addEnabled = len < 10 && this instanceof IParented;
		for(int i = 0; i < len + (addEnabled ? 1 : 0); i++) {
			boolean isAdd = i == bookmarks.size();
			GuiLexicon gui = isAdd ? null : bookmarks.get(i);
			buttonList.add(new GuiButtonBookmark(BOOKMARK_START + i, left + 138, top + 18 + 14 * i, gui == null ? this : gui, gui == null ? "+" : gui.getTitle()));
		}
	}

}
