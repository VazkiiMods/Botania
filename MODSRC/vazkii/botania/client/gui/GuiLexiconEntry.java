/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:47:06 PM (GMT)]
 */
package vazkii.botania.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.LexiconEntry;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.client.gui.button.GuiButtonBack;
import vazkii.botania.client.gui.button.GuiButtonPage;

public class GuiLexiconEntry extends GuiLexicon implements IGuiLexiconEntry {

	int page = 0;
	LexiconEntry entry;
	GuiLexiconIndex index;
	String title;

	GuiButton leftButton, rightButton;
	
	public GuiLexiconEntry(LexiconEntry entry, GuiLexiconIndex index) {
		this.entry = entry;
		this.index = index;
		
		title = StatCollector.translateToLocal(entry.getUnlocalizedName());
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.add(new GuiButtonBack(0, left + guiWidth / 2 - 8, top + guiHeight + 2));
		buttonList.add(leftButton = new GuiButtonPage(1, left, top + guiHeight - 10, false));
		buttonList.add(rightButton = new GuiButtonPage(2, left + guiWidth - 18, top + guiHeight - 10, true));
		
		updatePageButtons();
	}
	
	@Override
	public LexiconEntry getEntry() {
		return entry;
	}
	
	@Override
	public int getPageOn() {
		return page;
	}
	
	@Override
	boolean isIndex() {
		return false;
	}
	
	@Override
	void drawHeader() {
		// NO-OP
	}
	
	@Override
	String getTitle() {
		return String.format("%s (%s/%s)", title, (page + 1), entry.pages.size());
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch(par1GuiButton.id) {
		case 0 :
			mc.displayGuiScreen(index);
			break;
		case 1 :
			page--;
			break;
		case 2 : 
			page++;
			break;
		}
		updatePageButtons();
	}
	
	public void updatePageButtons() {
		leftButton.enabled = page != 0;
		rightButton.enabled = page + 1 < entry.pages.size();
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}
	
}
