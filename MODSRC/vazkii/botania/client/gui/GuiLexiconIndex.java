/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:46:59 PM (GMT)]
 */
package vazkii.botania.client.gui;

import java.util.List;

import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.LexiconCategory;
import vazkii.botania.api.LexiconEntry;
import vazkii.botania.client.gui.button.GuiButtonBack;
import vazkii.botania.client.gui.button.GuiButtonInvisible;

public class GuiLexiconIndex extends GuiLexicon {

	LexiconCategory category;
	String title;
	
	public GuiLexiconIndex(LexiconCategory category) {
		this.category = category;
		title = StatCollector.translateToLocal(category.getUnlocalizedName());
	}

	@Override
	void drawHeader() {
		// NO-OP
	}
	
	@Override
	String getTitle() {
		return title;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2));
	}
	
	@Override
	void populateIndex() {
		List<LexiconEntry> entryList = category.entries;
		for(int i = 0; i < 12; i++) {
			GuiButtonInvisible button = (GuiButtonInvisible) buttonList.get(i);
			LexiconEntry entry = i >= entryList.size() ? null : entryList.get(i);
			if(entry != null)
				button.displayString = " " + StatCollector.translateToLocal(entry.getUnlocalizedName());
			else button.displayString = "";
		}
	}
}
