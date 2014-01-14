/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:15:28 PM (GMT)]
 */
package vazkii.botania.api;

import java.util.ArrayList;
import java.util.List;

import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IInternalMethodHandler;

public final class BotaniaAPI {

	private static List<LexiconCategory> categories = new ArrayList<LexiconCategory>();
	private static List<LexiconEntry> allEntries = new ArrayList<LexiconEntry>();
	
	/**
	 * The internal method handler in use. Do not overwrite.
	 * @see IInternalMethodHandler
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();
	
	/**
	 * Adds a category to the list of registered categories to appear in the Lexicon.
	 */
	public static void addCategory(LexiconCategory category) {
		categories.add(category);
	}
	
	/**
	 * Gets all registered categories.
	 */
	public static List<LexiconCategory> getAllCategories() {
		return categories;
	}
	
	/**
	 * Registers a Lexicon Entry and adds it to the category passed in.
	 */
	public static void addEntry(LexiconEntry entry, LexiconCategory category) {
		allEntries.add(entry);
		category.entries.add(entry);
	}
}
