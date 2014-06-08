/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 8, 2014, 7:06:06 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconCategory;

public class WIPLexiconEntry extends BLexiconEntry implements IAddonEntry {

	public WIPLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
	}

	@Override
	public String getSubtitle() {
		return "botania.gui.lexicon.wip";
	}

}
