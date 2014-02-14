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
	public static LexiconEntry pureDaisy;
	public static LexiconEntry runicAltar;

	public static LexiconEntry manaIntro;
	public static LexiconEntry spreader;
	public static LexiconEntry pool;
	public static LexiconEntry lensVelocity;
	public static LexiconEntry lensPotency;
	public static LexiconEntry lensResistance;
	public static LexiconEntry lensEfficiency;
	public static LexiconEntry lensBounce;
	public static LexiconEntry lensGravity;
	public static LexiconEntry lensBore;
	public static LexiconEntry lensDamaging;

	public static LexiconEntry functionalIntro;
	public static LexiconEntry bellethorne;
	
	public static LexiconEntry generatingIntro;
	public static LexiconEntry daybloom;
	
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

		pureDaisy = new BLexiconEntry(LibLexicon.BASICS_PURE_DAISY, categoryBasics);
		pureDaisy.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_PURE_DAISY), new PagePetalRecipe("2", ModPetalRecipes.pureDaisyRecipe));

		runicAltar = new BLexiconEntry(LibLexicon.BASICS_RUNE_ALTAR, categoryBasics);
		runicAltar.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeRuneAltar), new PageText("3"), new PageRuneRecipe("4", ModRuneRecipes.recipeWaterRune), new PageRuneRecipe("5", ModRuneRecipes.recipeEarthRune), new PageRuneRecipe("6", ModRuneRecipes.recipeAirRune), new PageRuneRecipe("7", ModRuneRecipes.recipeFireRune),
				new PageRuneRecipe("8", ModRuneRecipes.recipeSpringRune), new PageRuneRecipe("9", ModRuneRecipes.recipeSummerRune), new PageRuneRecipe("10", ModRuneRecipes.recipeAutumnRune), new PageRuneRecipe("11", ModRuneRecipes.recipeWinterRune),  new PageRuneRecipe("12", ModRuneRecipes.recipeManaRune),
				new PageRuneRecipe("13", ModRuneRecipes.recipeLustRune), new PageRuneRecipe("14", ModRuneRecipes.recipeGluttonyRune), new PageRuneRecipe("15", ModRuneRecipes.recipeGreedRune), new PageRuneRecipe("16", ModRuneRecipes.recipeSlothRune), new PageRuneRecipe("17", ModRuneRecipes.recipeEnvyRune), new PageRuneRecipe("18", ModRuneRecipes.recipePrideRune));

		// MANA ENTRIES
		manaIntro = new BLexiconEntry(LibLexicon.MANA_INTRO, categoryMana);
		manaIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		spreader = new BLexiconEntry(LibLexicon.MANA_SPREADER, categoryMana);
		spreader.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_SPREADER), new PageText("2"), new PageText("3"), new PageText("4"), new PageCraftingRecipe("5", ModCrafingRecipes.recipesSpreader), new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipeManaLens), new PageCraftingRecipe("8", ModCrafingRecipes.recipesLensDying), new PageCraftingRecipe("9", ModCrafingRecipes.recipeRainbowLens));

		pool = new BLexiconEntry(LibLexicon.MANA_POOL, categoryMana);
		pool.setPriority().setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePool), new PageText("2"), new PageManaInfusionRecipe("3", ModManaInfusionRecipes.manasteelRecipe), new PageManaInfusionRecipe("4", ModManaInfusionRecipes.manaPearlRecipe), new PageManaInfusionRecipe("5", ModManaInfusionRecipes.manaDiamondRecipe), new PageManaInfusionRecipe("6", ModManaInfusionRecipes.manaPetalRecipes));
		
		lensVelocity = new BLexiconEntry(LibLexicon.MANA_LENS_VELOCITY, categoryMana);
		lensVelocity.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensVelocity));
		
		lensPotency = new BLexiconEntry(LibLexicon.MANA_LENS_POTENCY, categoryMana);
		lensPotency.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensPotency));
		
		lensResistance = new BLexiconEntry(LibLexicon.MANA_LENS_RESISTANCE, categoryMana);
		lensResistance.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensResistance));
		
		lensEfficiency = new BLexiconEntry(LibLexicon.MANA_LENS_EFFICIENCY, categoryMana);
		lensEfficiency.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensEfficiency));
		
		lensBounce = new BLexiconEntry(LibLexicon.MANA_LENS_BOUNCE, categoryMana);
		lensBounce.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensBounce));
		
		lensGravity = new BLexiconEntry(LibLexicon.MANA_LENS_GRAVITY, categoryMana);
		lensGravity.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensGravity));
		
		lensBore = new BLexiconEntry(LibLexicon.MANA_LENS_BORE, categoryMana);
		lensBore.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensBore));
		
		lensDamaging = new BLexiconEntry(LibLexicon.MANA_LENS_DAMAGING, categoryMana);
		lensDamaging.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensDamaging));

		// FUNCTIONAL FLOWERS ENTRIES
		functionalIntro = new BLexiconEntry(LibLexicon.FFLOWER_INTRO, categoryFunctionalFlowers);
		functionalIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));
		
		bellethorne = new BLexiconEntry(LibLexicon.FFLOWER_BELLETHORNE, categoryFunctionalFlowers);
		bellethorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.bellethorneRecipe));
		
		// GENERATING FLOWERS ENTRIES
		generatingIntro = new BLexiconEntry(LibLexicon.GFLOWER_INTRO, categoryGenerationFlowers);
		generatingIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));
		
		daybloom = new BLexiconEntry(LibLexicon.GFLOWER_DAYBLOOM, categoryGenerationFlowers);
		daybloom.setPriority().setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.daybloomRecipe));
	}

}
