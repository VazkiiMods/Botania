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
package vazkii.botania.common.lexicon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.crafting.ModCrafingRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibLexicon;

public final class LexiconData {

	public static LexiconCategory categoryBasics;
	public static LexiconCategory categoryMana;
	public static LexiconCategory categoryFunctionalFlowers;
	public static LexiconCategory categoryGenerationFlowers;
	public static LexiconCategory categoryDevices;
	public static LexiconCategory categoryTools;
	public static LexiconCategory categoryMisc;

	public static LexiconEntry flowers;
	public static LexiconEntry apothecary;
	public static LexiconEntry lexicon;
	public static LexiconEntry wand;
	
	public static LexiconEntry pool;

	public static void init() {
		BotaniaAPI.addCategory(categoryBasics = new LexiconCategory(LibLexicon.CATEGORY_BASICS));
		BotaniaAPI.addCategory(categoryMana = new LexiconCategory(LibLexicon.CATEGORY_MANA));
		BotaniaAPI.addCategory(categoryFunctionalFlowers = new LexiconCategory(LibLexicon.CATEGORY_FUNCTIONAL_FLOWERS));
		BotaniaAPI.addCategory(categoryGenerationFlowers = new LexiconCategory(LibLexicon.CATEGORY_GENERATION_FLOWERS));
		BotaniaAPI.addCategory(categoryDevices = new LexiconCategory(LibLexicon.CATEGORY_DEVICES));
		BotaniaAPI.addCategory(categoryTools = new LexiconCategory(LibLexicon.CATEGORY_TOOLS));
		BotaniaAPI.addCategory(categoryMisc = new LexiconCategory(LibLexicon.CATEGORY_MISC));

		// BASICS ENTRIES
		flowers = new BLexiconEntry(LibLexicon.BASICS_FLOWERS, categoryBasics);
		flowers.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_FLOWERS), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipesPetals), new PageCraftingRecipe("4", ModCrafingRecipes.recipePestleAndMortar), new PageCraftingRecipe("5", ModCrafingRecipes.recipesDyes));

		apothecary = new BLexiconEntry(LibLexicon.BASICS_APOTHECARY, categoryBasics);
		apothecary.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_APOTHECARY), new PageText("2"), new PageText("3"), new PageCraftingRecipe("4", ModCrafingRecipes.recipesApothecary));

		lexicon = new BLexiconEntry(LibLexicon.BASICS_LEXICON, categoryBasics);
		lexicon.setPriority().setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLexicon));

		wand = new BLexiconEntry(LibLexicon.BASICS_WAND, categoryBasics);
		wand.setPriority().setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipesTwigWand));
		
		// MANA ENTRIES
		pool = new BLexiconEntry(LibLexicon.MANA_POOL, categoryMana);
		pool.setPriority().setLexiconPages(new PageText("0"), new PageManaInfusionRecipe("1", ModManaInfusionRecipes.manaPetalRecipes), new PagePetalRecipe("2", ModPetalRecipes.testRecipe), new PageRuneRecipe("3", ModRuneRecipes.recipeDebug));
	}

}
