/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 9:47:21 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.LexiconCategory;
import vazkii.botania.api.LexiconEntry;
import vazkii.botania.api.page.LexiconPage;

public class BLexiconEntry extends LexiconEntry {

	public BLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName);
		BotaniaAPI.addEntry(this, category);
	}

	@Override
	public void setLexiconPages(LexiconPage... pages) {
		for(LexiconPage page : pages)
			page.unlocalizedName = "botania.page." + getLazyUnlocalizedName() + page.unlocalizedName;

		super.setLexiconPages(pages);
	}

	@Override
	public String getUnlocalizedName() {
		return "botania.entry." + super.getUnlocalizedName();
	}

	public String getLazyUnlocalizedName() {
		return super.getUnlocalizedName();
	}

}
