/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:23:47 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import java.util.ArrayList;
import java.util.List;

public final class LexiconCategory {

	public final String unlocalizedName;
	public final List<LexiconEntry> entries = new ArrayList<LexiconEntry>();

	/**
	 * @param unlocalizedName The unlocalized name of this category. This will be localized by the client display.
	 */
	public LexiconCategory(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}
}
