/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 9:12:15 PM (GMT)]
 */
package vazkii.botania.common.page;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.LexiconCategory;
import vazkii.botania.common.lib.LibLexicon;

public final class LexiconData {

	public static LexiconCategory categoryBasics;
	public static LexiconCategory categoryEnergy;
	public static LexiconCategory categoryPlants;
	public static LexiconCategory categoryMisc;
	
	public static void init() {
		BotaniaAPI.addCategory(categoryBasics = new LexiconCategory(LibLexicon.CATEGORY_BASICS));
		BotaniaAPI.addCategory(categoryEnergy = new LexiconCategory(LibLexicon.CATEGORY_ENERGY));
		BotaniaAPI.addCategory(categoryPlants = new LexiconCategory(LibLexicon.CATEGORY_PLANTS));
		BotaniaAPI.addCategory(categoryMisc = new LexiconCategory(LibLexicon.CATEGORY_MISC));

		
	}
	
}
