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

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
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
	public static LexiconEntry distributor;
	public static LexiconEntry manaVoid;
	public static LexiconEntry lensVelocity;
	public static LexiconEntry lensPotency;
	public static LexiconEntry lensResistance;
	public static LexiconEntry lensEfficiency;
	public static LexiconEntry lensBounce;
	public static LexiconEntry lensGravity;
	public static LexiconEntry lensBore;
	public static LexiconEntry lensDamaging;
	public static LexiconEntry lensPhantom;
	public static LexiconEntry lensMagnet;
	public static LexiconEntry lensExplosive;
	public static LexiconEntry manaTransport;
	public static LexiconEntry manaDetector;
	public static LexiconEntry compositeLens;

	public static LexiconEntry functionalIntro;
	public static LexiconEntry jadedAmaranthus;
	public static LexiconEntry bellethorne;
	public static LexiconEntry heiseiDream;
	public static LexiconEntry tigerseye;
	public static LexiconEntry orechid;
	public static LexiconEntry fallenKanade;
	public static LexiconEntry exoflame;
	public static LexiconEntry agricarnation;
	public static LexiconEntry hopperhock;

	public static LexiconEntry generatingIntro;
	public static LexiconEntry daybloom;
	public static LexiconEntry nightshade;
	public static LexiconEntry endoflame;
	public static LexiconEntry hydroangeas;
	public static LexiconEntry thermalily;
	public static LexiconEntry arcaneRose;

	public static LexiconEntry pistonRelay;
	public static LexiconEntry pylon;
	public static LexiconEntry manaEnchanting;
	public static LexiconEntry turntable;

	public static LexiconEntry manaBlaster;

	public static LexiconEntry unstableBlocks;
	public static LexiconEntry decorativeBlocks;

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
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingwood), pureDaisy, 1);
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingrock), pureDaisy, 1);

		runicAltar = new BLexiconEntry(LibLexicon.BASICS_RUNE_ALTAR, categoryBasics);
		runicAltar.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipesRuneAltar), new PageText("3"), new PageRuneRecipe("4", ModRuneRecipes.recipeWaterRune), new PageRuneRecipe("5", ModRuneRecipes.recipesEarthRune), new PageRuneRecipe("6", ModRuneRecipes.recipeAirRune), new PageRuneRecipe("7", ModRuneRecipes.recipeFireRune),
				new PageRuneRecipe("8", ModRuneRecipes.recipeSpringRune), new PageRuneRecipe("9", ModRuneRecipes.recipeSummerRune), new PageRuneRecipe("10", ModRuneRecipes.recipeAutumnRune), new PageRuneRecipe("11", ModRuneRecipes.recipeWinterRune),  new PageRuneRecipe("12", ModRuneRecipes.recipeManaRune),
				new PageRuneRecipe("13", ModRuneRecipes.recipeLustRune), new PageRuneRecipe("14", ModRuneRecipes.recipeGluttonyRune), new PageRuneRecipe("15", ModRuneRecipes.recipeGreedRune), new PageRuneRecipe("16", ModRuneRecipes.recipeSlothRune), new PageRuneRecipe("17", ModRuneRecipes.recipeWrathRune), new PageRuneRecipe("18", ModRuneRecipes.recipeEnvyRune), new PageRuneRecipe("19", ModRuneRecipes.recipePrideRune));

		// MANA ENTRIES
		manaIntro = new BLexiconEntry(LibLexicon.MANA_INTRO, categoryMana);
		manaIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		spreader = new BLexiconEntry(LibLexicon.MANA_SPREADER, categoryMana);
		spreader.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_SPREADER), new PageText("2"), new PageText("3"), new PageText("4"), new PageCraftingRecipe("5", ModCrafingRecipes.recipesSpreader), new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipeManaLens), new PageCraftingRecipe("8", ModCrafingRecipes.recipesLensDying), new PageCraftingRecipe("9", ModCrafingRecipes.recipeRainbowLens));

		pool = new BLexiconEntry(LibLexicon.MANA_POOL, categoryMana);
		pool.setPriority().setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePool), new PageText("2"), new PageManaInfusionRecipe("3", ModManaInfusionRecipes.manasteelRecipe), new PageManaInfusionRecipe("4", ModManaInfusionRecipes.manaPearlRecipe), new PageManaInfusionRecipe("5", ModManaInfusionRecipes.manaDiamondRecipe), new PageManaInfusionRecipe("6", ModManaInfusionRecipes.manaPetalRecipes), new PageManaInfusionRecipe("7", ModManaInfusionRecipes.manaCookieRecipe));

		distributor = new BLexiconEntry(LibLexicon.MANA_DISTRIBUTOR, categoryMana);
		distributor.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeDistributor));

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

		lensPhantom = new BLexiconEntry(LibLexicon.MANA_LENS_PHANTOM, categoryMana);
		lensPhantom.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensPhantom));

		lensMagnet = new BLexiconEntry(LibLexicon.MANA_LENS_MAGNET, categoryMana);
		lensMagnet.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensMagnet));

		lensExplosive = new BLexiconEntry(LibLexicon.MANA_LENS_EXPLOSIVE, categoryMana);
		lensExplosive.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensExplosive));

		manaVoid = new BLexiconEntry(LibLexicon.MANA_VOID, categoryMana);
		manaVoid.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaVoid));

		manaTransport = new BLexiconEntry(LibLexicon.MANA_TRANSPORT, categoryMana);
		manaTransport.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipesManaTablet));

		manaDetector = new BLexiconEntry(LibLexicon.MANA_DETECTOR, categoryMana);
		manaDetector.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaDetector));

		compositeLens = new BLexiconEntry(LibLexicon.MANA_COMPOSITE_LENS, categoryMana);
		compositeLens.setLexiconPages(new PageText("0"));

		// FUNCTIONAL FLOWERS ENTRIES
		functionalIntro = new BLexiconEntry(LibLexicon.FFLOWER_INTRO, categoryFunctionalFlowers);
		functionalIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		jadedAmaranthus = new BLexiconEntry(LibLexicon.FFLOWER_JADED_AMARANTHUS, categoryFunctionalFlowers);
		jadedAmaranthus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.jadedAmaranthusRecipe));

		bellethorne = new BLexiconEntry(LibLexicon.FFLOWER_BELLETHORNE, categoryFunctionalFlowers);
		bellethorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.bellethorneRecipe));

		heiseiDream = new BLexiconEntry(LibLexicon.FFLOWER_HEISEI_DREAM, categoryFunctionalFlowers);
		heiseiDream.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.heiseiDreamRecipe));

		tigerseye = new BLexiconEntry(LibLexicon.FFLOWER_TIGERSEYE, categoryFunctionalFlowers);
		tigerseye.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.tigerseyeRecipe));

		orechid = new BLexiconEntry(LibLexicon.FFLOWER_ORECHID, categoryFunctionalFlowers);
		orechid.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.orechidRecipe));

		fallenKanade = new BLexiconEntry(LibLexicon.FFLOWER_FALLEN_KANADE, categoryFunctionalFlowers);
		fallenKanade.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.fallenKanadeRecipe));

		exoflame = new BLexiconEntry(LibLexicon.FFLOWER_EXOFLAME, categoryFunctionalFlowers);
		exoflame.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.exoflameRecipe));

		agricarnation = new BLexiconEntry(LibLexicon.FFLOWER_AGRICARNATION, categoryFunctionalFlowers);
		agricarnation.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.agricarnationRecipe));

		hopperhock = new BLexiconEntry(LibLexicon.FFLOWER_HOPPERHOCK, categoryFunctionalFlowers);
		hopperhock.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModPetalRecipes.hopperhockRecipe));

		// GENERATING FLOWERS ENTRIES
		generatingIntro = new BLexiconEntry(LibLexicon.GFLOWER_INTRO, categoryGenerationFlowers);
		generatingIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		daybloom = new BLexiconEntry(LibLexicon.GFLOWER_DAYBLOOM, categoryGenerationFlowers);
		daybloom.setPriority().setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.daybloomRecipe));

		nightshade = new BLexiconEntry(LibLexicon.GFLOWER_NIGHTSHADE, categoryGenerationFlowers);
		nightshade.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.nightshadeRecipe));

		endoflame = new BLexiconEntry(LibLexicon.GFLOWER_ENDOFLAME, categoryGenerationFlowers);
		endoflame.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModPetalRecipes.endoflameRecipe));

		hydroangeas = new BLexiconEntry(LibLexicon.GFLOWER_HYDROANGEAS, categoryGenerationFlowers);
		hydroangeas.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.hydroangeasRecipe));

		thermalily = new BLexiconEntry(LibLexicon.GFLOWER_THERMALILY, categoryGenerationFlowers);
		thermalily.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.thermalilyRecipe));

		arcaneRose = new BLexiconEntry(LibLexicon.GFLOWER_ARCANE_ROSE, categoryGenerationFlowers);
		arcaneRose.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.arcaneRoseRecipe));

		// DEVICES ENTRIES
		pistonRelay = new BLexiconEntry(LibLexicon.DEVICE_PISTON_RELAY, categoryDevices);
		pistonRelay.setLexiconPages(new PageText("0"), new PageText("1"), new PageManaInfusionRecipe("2", ModManaInfusionRecipes.pistonRelayRecipe));

		pylon = new BLexiconEntry(LibLexicon.DEVICE_PYLON, categoryDevices);
		pylon.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePylon));

		manaEnchanting = new BLexiconEntry(LibLexicon.DEVICE_MANA_ENCHANTING, categoryDevices);
		manaEnchanting.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageImage("2", LibResources.ENTRY_ENCHANTER0), new PageImage("3", LibResources.ENTRY_ENCHANTER1), new PageImage("4", LibResources.ENTRY_ENCHANTER2), new PageImage("5", LibResources.ENTRY_ENCHANTER3),
				new PageText("6"), new PageText("7"), new PageText("8"), new PageText("9"), new PageText("10"));

		turntable = new BLexiconEntry(LibLexicon.DEVICE_TURNTABLE, categoryDevices);
		turntable.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTurntable));

		// TOOLS ENTRIES
		manaBlaster = new BLexiconEntry(LibLexicon.TOOL_MANA_BLASTER, categoryTools);
		manaBlaster.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipeManaBlaster));

		// MISCLENAEOUS ENTRIES
		unstableBlocks = new BLexiconEntry(LibLexicon.MISC_UNSTABLE_BLOCKS, categoryMisc);
		unstableBlocks.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_UNSTABLE_BLOCK), new PageCraftingRecipe("2", ModCrafingRecipes.recipesUnstableBlocks), new PageText("3"), new PageImage("4", LibResources.ENTRY_UNSTABLE_BEACON), new PageCraftingRecipe("5", ModCrafingRecipes.recipesManaBeacons), new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipesSignalFlares));

		decorativeBlocks = new BLexiconEntry(LibLexicon.MISC_DECORATIVE_BLOCKS, categoryMisc);
		decorativeBlocks.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.livingrockDecorRecipe1), new PageCraftingRecipe("2", ModCrafingRecipes.livingrockDecorRecipe2), new PageCraftingRecipe("3", ModCrafingRecipes.livingrockDecorRecipe3), new PageCraftingRecipe("4", ModCrafingRecipes.livingrockDecorRecipe4),
				new PageCraftingRecipe("5", ModCrafingRecipes.livingwoodDecorRecipe1), new PageCraftingRecipe("6", ModCrafingRecipes.livingwoodDecorRecipe2), new PageCraftingRecipe("7", ModCrafingRecipes.livingwoodDecorRecipe3), new PageCraftingRecipe("8", ModCrafingRecipes.livingwoodDecorRecipe4));
	}
}
