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
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.crafting.ModCrafingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageTerrasteel;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibLexicon;

public final class LexiconData {

	public static LexiconCategory categoryBasics;
	public static LexiconCategory categoryMana;
	public static LexiconCategory categoryFunctionalFlowers;
	public static LexiconCategory categoryGenerationFlowers;
	public static LexiconCategory categoryDevices;
	public static LexiconCategory categoryTools;
	public static LexiconCategory categoryBaubles;
	public static LexiconCategory categoryAlfhomancy;
	public static LexiconCategory categoryMisc;

	public static LexiconEntry flowers;
	public static LexiconEntry apothecary;
	public static LexiconEntry lexicon;
	public static LexiconEntry wand;
	public static LexiconEntry pureDaisy;
	public static LexiconEntry runicAltar;
	public static LexiconEntry terrasteel;

	public static LexiconEntry manaIntro;
	public static LexiconEntry spreader;
	public static LexiconEntry pool;
	public static LexiconEntry lenses;
	public static LexiconEntry distributor;
	public static LexiconEntry manaVoid;
	public static LexiconEntry manaTransport;
	public static LexiconEntry manaDetector;
	public static LexiconEntry redstoneSpreader;
	public static LexiconEntry manastar;
	public static LexiconEntry dreamwoodSpreader;
	public static LexiconEntry paintLens;

	public static LexiconEntry functionalIntro;
	public static LexiconEntry jadedAmaranthus;
	public static LexiconEntry bellethorne;
	public static LexiconEntry dreadthorne;
	public static LexiconEntry heiseiDream;
	public static LexiconEntry tigerseye;
	public static LexiconEntry orechid;
	public static LexiconEntry fallenKanade;
	public static LexiconEntry exoflame;
	public static LexiconEntry agricarnation;
	public static LexiconEntry hopperhock;
	public static LexiconEntry tangleberrie;
	public static LexiconEntry jiyuulia;
	public static LexiconEntry rannuncarpus;
	public static LexiconEntry hyacidus;
	public static LexiconEntry pollidisiac;
	public static LexiconEntry clayconia;
	public static LexiconEntry loonium;
	public static LexiconEntry daffomill;
	public static LexiconEntry vinculotus;

	public static LexiconEntry generatingIntro;
	public static LexiconEntry daybloom;
	public static LexiconEntry nightshade;
	public static LexiconEntry endoflame;
	public static LexiconEntry hydroangeas;
	public static LexiconEntry thermalily;
	public static LexiconEntry arcaneRose;
	public static LexiconEntry munchdew;
	public static LexiconEntry entropinnyum;
	public static LexiconEntry kekimurus;
	public static LexiconEntry gourmaryllis;

	public static LexiconEntry pistonRelay;
	public static LexiconEntry pylon;
	public static LexiconEntry manaEnchanting;
	public static LexiconEntry turntable;
	public static LexiconEntry alchemy;
	public static LexiconEntry openCrate;
	public static LexiconEntry forestEye;
	public static LexiconEntry forestDrum;
	public static LexiconEntry platform;
	public static LexiconEntry conjurationCatalyst;
	public static LexiconEntry spectralPlatform;
	public static LexiconEntry gatherDrum;
	public static LexiconEntry spawnerClaw;
	public static LexiconEntry craftCrate;
	public static LexiconEntry enderEyeBlock;

	public static LexiconEntry manaBlaster;
	public static LexiconEntry grassSeeds;
	public static LexiconEntry dirtRod;
	public static LexiconEntry terraformRod;
	public static LexiconEntry manasteelGear;
	public static LexiconEntry terrasteelArmor;
	public static LexiconEntry grassHorn;
	public static LexiconEntry terraBlade;
	public static LexiconEntry terraPick;
	public static LexiconEntry enderDagger;
	public static LexiconEntry waterRod;
	public static LexiconEntry elfGear;
	public static LexiconEntry openBucket;
	public static LexiconEntry spawnerMover;
	public static LexiconEntry rainbowRod;
	public static LexiconEntry tornadoRod;
	public static LexiconEntry fireRod;
	public static LexiconEntry vineBall;
	public static LexiconEntry laputaShard;
	public static LexiconEntry virus;
	public static LexiconEntry skyDirtRod;

	public static LexiconEntry baublesIntro;
	public static LexiconEntry tinyPlanet;
	public static LexiconEntry manaRing;
	public static LexiconEntry auraRing;
	public static LexiconEntry travelBelt;
	public static LexiconEntry knockbacklBelt;
	public static LexiconEntry icePendant;
	public static LexiconEntry lavaPendant;
	public static LexiconEntry goldLaurel;
	public static LexiconEntry waterRing;
	public static LexiconEntry miningRing;
	public static LexiconEntry magnetRing;
	public static LexiconEntry divaCharm;
	public static LexiconEntry flightTiara;
	public static LexiconEntry pixieRing;
	public static LexiconEntry superTravelBelt;
	public static LexiconEntry reachRing;

	public static LexiconEntry alfhomancyIntro;
	public static LexiconEntry elvenMessage;
	public static LexiconEntry elvenResources;
	public static LexiconEntry gaiaRitual;

	public static LexiconEntry unstableBlocks;
	public static LexiconEntry decorativeBlocks;
	public static LexiconEntry dispenserTweaks;
	public static LexiconEntry shinyFlowers;
	public static LexiconEntry prismarine;
	public static LexiconEntry shedding;
	public static LexiconEntry tinyPotato;
	public static LexiconEntry headCreating;
	public static LexiconEntry azulejo;

	public static void init() {
		BotaniaAPI.addCategory(categoryBasics = new LexiconCategory(LibLexicon.CATEGORY_BASICS));
		BotaniaAPI.addCategory(categoryMana = new LexiconCategory(LibLexicon.CATEGORY_MANA));
		BotaniaAPI.addCategory(categoryFunctionalFlowers = new LexiconCategory(LibLexicon.CATEGORY_FUNCTIONAL_FLOWERS));
		BotaniaAPI.addCategory(categoryGenerationFlowers = new LexiconCategory(LibLexicon.CATEGORY_GENERATION_FLOWERS));
		BotaniaAPI.addCategory(categoryDevices = new LexiconCategory(LibLexicon.CATEGORY_DEVICES));
		BotaniaAPI.addCategory(categoryTools = new LexiconCategory(LibLexicon.CATEGORY_TOOLS));
		BotaniaAPI.addCategory(categoryBaubles = new LexiconCategory(LibLexicon.CATEGORY_BAUBLES));
		BotaniaAPI.addCategory(categoryAlfhomancy = new LexiconCategory(LibLexicon.CATEGORY_ALFHOMANCY));
		BotaniaAPI.addCategory(categoryMisc = new LexiconCategory(LibLexicon.CATEGORY_MISC));

		// BASICS ENTRIES
		flowers = new BLexiconEntry(LibLexicon.BASICS_FLOWERS, categoryBasics);
		flowers.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_FLOWERS), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipesPetals), new PageCraftingRecipe("4", ModCrafingRecipes.recipePestleAndMortar), new PageCraftingRecipe("5", ModCrafingRecipes.recipesDyes),
				new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipeFertilizerPowder), new PageCraftingRecipe("8", ModCrafingRecipes.recipeFerilizerDye));

		apothecary = new BLexiconEntry(LibLexicon.BASICS_APOTHECARY, categoryBasics);
		apothecary.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_APOTHECARY), new PageText("2"), new PageText("3"), new PageCraftingRecipe("4", ModCrafingRecipes.recipesApothecary));

		lexicon = new BLexiconEntry(LibLexicon.BASICS_LEXICON, categoryBasics);
		lexicon.setPriority().setLexiconPages(new PageText("0"), new PageText("3"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLexicon), new PageText("2"));

		wand = new BLexiconEntry(LibLexicon.BASICS_WAND, categoryBasics);
		wand.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipesTwigWand));

		pureDaisy = new BLexiconEntry(LibLexicon.BASICS_PURE_DAISY, categoryBasics);
		pureDaisy.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_PURE_DAISY), new PageCraftingRecipe("2", ModCrafingRecipes.recipeLivingwoodTwig), new PagePetalRecipe("3", ModPetalRecipes.pureDaisyRecipe));
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingwood), pureDaisy, 1);
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingrock), pureDaisy, 1);

		runicAltar = new BLexiconEntry(LibLexicon.BASICS_RUNE_ALTAR, categoryBasics);
		runicAltar.setPriority().setLexiconPages(new PageText("21"), new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipesRuneAltar), new PageText("3"), new PageText("20"), new PageRuneRecipe("4", ModRuneRecipes.recipeWaterRune), new PageRuneRecipe("5", ModRuneRecipes.recipesEarthRune), new PageRuneRecipe("6", ModRuneRecipes.recipesAirRune), new PageRuneRecipe("7", ModRuneRecipes.recipeFireRune),
				new PageRuneRecipe("8", ModRuneRecipes.recipeSpringRune), new PageRuneRecipe("9", ModRuneRecipes.recipeSummerRune), new PageRuneRecipe("10", ModRuneRecipes.recipeAutumnRune), new PageRuneRecipe("11", ModRuneRecipes.recipesWinterRune),  new PageRuneRecipe("12", ModRuneRecipes.recipeManaRune),
				new PageRuneRecipe("13", ModRuneRecipes.recipeLustRune), new PageRuneRecipe("14", ModRuneRecipes.recipeGluttonyRune), new PageRuneRecipe("15", ModRuneRecipes.recipeGreedRune), new PageRuneRecipe("16", ModRuneRecipes.recipeSlothRune), new PageRuneRecipe("17", ModRuneRecipes.recipeWrathRune), new PageRuneRecipe("18", ModRuneRecipes.recipeEnvyRune), new PageRuneRecipe("19", ModRuneRecipes.recipePrideRune));

		terrasteel = new BLexiconEntry(LibLexicon.BASICS_TERRASTEEL, categoryBasics);
		terrasteel.setLexiconPages(new PageText("0"), new PageTerrasteel("1"));

		// MANA ENTRIES
		manaIntro = new BLexiconEntry(LibLexicon.MANA_INTRO, categoryMana);
		manaIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		spreader = new BLexiconEntry(LibLexicon.MANA_SPREADER, categoryMana);
		spreader.setPriority().setLexiconPages(new PageText("0"), new PageText("10"), new PageImage("1", LibResources.ENTRY_SPREADER), new PageText("2"), new PageText("3"), new PageText("4"), new PageText("11"), new PageCraftingRecipe("5", ModCrafingRecipes.recipesSpreader));

		pool = new BLexiconEntry(LibLexicon.MANA_POOL, categoryMana);
		pool.setPriority().setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePool), new PageText("2"), new PageText("8"), new PageManaInfusionRecipe("3", ModManaInfusionRecipes.manasteelRecipes), new PageManaInfusionRecipe("4", ModManaInfusionRecipes.manaPearlRecipe), new PageManaInfusionRecipe("5", ModManaInfusionRecipes.manaDiamondRecipe), new PageManaInfusionRecipe("6", ModManaInfusionRecipes.manaPetalRecipes), new PageManaInfusionRecipe("7", ModManaInfusionRecipes.manaCookieRecipe));

		lenses = new BLexiconEntry(LibLexicon.MANA_LENSES, categoryMana);
		lenses.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaLens), new PageCraftingRecipe("2", ModCrafingRecipes.recipesLensDying), new PageCraftingRecipe("3", ModCrafingRecipes.recipeRainbowLens), new PageText("4"), new PageText("5"),
				new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipeLensVelocity),
				new PageText("8"), new PageCraftingRecipe("9", ModCrafingRecipes.recipeLensPotency),
				new PageText("10"), new PageCraftingRecipe("11", ModCrafingRecipes.recipeLensResistance),
				new PageText("12"), new PageCraftingRecipe("13", ModCrafingRecipes.recipeLensEfficiency),
				new PageText("14"), new PageCraftingRecipe("15", ModCrafingRecipes.recipeLensBounce),
				new PageText("16"), new PageCraftingRecipe("17", ModCrafingRecipes.recipeLensGravity),
				new PageText("18"), new PageCraftingRecipe("19", ModCrafingRecipes.recipeLensBore),
				new PageText("20"), new PageCraftingRecipe("21", ModCrafingRecipes.recipeLensDamaging),
				new PageText("22"), new PageCraftingRecipe("23", ModCrafingRecipes.recipeLensPhantom),
				new PageText("24"), new PageCraftingRecipe("25", ModCrafingRecipes.recipeLensMagnet),
				new PageText("26"), new PageCraftingRecipe("27", ModCrafingRecipes.recipeLensExplosive),
				new PageText("28"), new PageCraftingRecipe("29", ModCrafingRecipes.recipeLensInfluence),
				new PageText("30"), new PageCraftingRecipe("31", ModCrafingRecipes.recipeLensWeight),
				new PageText("32"), new PageCraftingRecipe("33", ModCrafingRecipes.recipeLensFire),
				new PageText("34"), new PageCraftingRecipe("35", ModCrafingRecipes.recipeLensPiston));

		distributor = new BLexiconEntry(LibLexicon.MANA_DISTRIBUTOR, categoryMana);
		distributor.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeDistributor));

		manaVoid = new BLexiconEntry(LibLexicon.MANA_VOID, categoryMana);
		manaVoid.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaVoid));

		manaTransport = new BLexiconEntry(LibLexicon.MANA_TRANSPORT, categoryMana);
		manaTransport.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipesManaTablet),
				new PageText("3"), new PageCraftingRecipe("4", ModCrafingRecipes.recipeManaMirror));

		manaDetector = new BLexiconEntry(LibLexicon.MANA_DETECTOR, categoryMana);
		manaDetector.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaDetector));

		redstoneSpreader = new BLexiconEntry(LibLexicon.MANA_REDSTONE_SPREADER, categoryMana);
		redstoneSpreader.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeRedstoneSpreader));

		manastar = new BLexiconEntry(LibLexicon.MANA_MANASTAR, categoryMana);
		manastar.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.manastarRecipe));

		dreamwoodSpreader = new ALexiconEntry(LibLexicon.MANA_DREAMWOOD_SPREADER, categoryMana);
		dreamwoodSpreader.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipesDreamwoodSpreader));

		paintLens = new ALexiconEntry(LibLexicon.MANA_PAINT_LENS, categoryMana);
		paintLens.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLensPaint));

		// FUNCTIONAL FLOWERS ENTRIES
		functionalIntro = new BLexiconEntry(LibLexicon.FFLOWER_INTRO, categoryFunctionalFlowers);
		functionalIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"), new PageCraftingRecipe("4", ModCrafingRecipes.recipeRedstoneRoot));

		jadedAmaranthus = new BLexiconEntry(LibLexicon.FFLOWER_JADED_AMARANTHUS, categoryFunctionalFlowers);
		jadedAmaranthus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.jadedAmaranthusRecipe));

		bellethorne = new BLexiconEntry(LibLexicon.FFLOWER_BELLETHORNE, categoryFunctionalFlowers);
		bellethorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.bellethorneRecipe));

		dreadthorne = new BLexiconEntry(LibLexicon.FFLOWER_DREADTHORNE, categoryFunctionalFlowers);
		dreadthorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.dreadthorneRecipe));

		heiseiDream = new BLexiconEntry(LibLexicon.FFLOWER_HEISEI_DREAM, categoryFunctionalFlowers);
		heiseiDream.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.heiseiDreamRecipe));

		tigerseye = new BLexiconEntry(LibLexicon.FFLOWER_TIGERSEYE, categoryFunctionalFlowers);
		tigerseye.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.tigerseyeRecipe));

		orechid = new BLexiconEntry(LibLexicon.FFLOWER_ORECHID, categoryFunctionalFlowers);
		orechid.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.orechidRecipe));

		if(ConfigHandler.fallenKanadeEnabled) {
			fallenKanade = new BLexiconEntry(LibLexicon.FFLOWER_FALLEN_KANADE, categoryFunctionalFlowers);
			fallenKanade.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.fallenKanadeRecipe));
		}

		exoflame = new BLexiconEntry(LibLexicon.FFLOWER_EXOFLAME, categoryFunctionalFlowers);
		exoflame.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.exoflameRecipe));

		agricarnation = new BLexiconEntry(LibLexicon.FFLOWER_AGRICARNATION, categoryFunctionalFlowers);
		agricarnation.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.agricarnationRecipe));

		hopperhock = new BLexiconEntry(LibLexicon.FFLOWER_HOPPERHOCK, categoryFunctionalFlowers);
		hopperhock.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModPetalRecipes.hopperhockRecipe));

		tangleberrie = new BLexiconEntry(LibLexicon.FFLOWER_TANGLEBERRIE, categoryFunctionalFlowers);
		tangleberrie.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.tangleberrieRecipe));

		jiyuulia = new BLexiconEntry(LibLexicon.FFLOWER_JIYUULIA, categoryFunctionalFlowers);
		jiyuulia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.jiyuuliaRecipe));

		rannuncarpus = new BLexiconEntry(LibLexicon.FFLOWER_RANNUNCARPUS, categoryFunctionalFlowers);
		rannuncarpus.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModPetalRecipes.rannuncarpusRecipe));

		hyacidus = new BLexiconEntry(LibLexicon.FFLOWER_HYACIDUS, categoryFunctionalFlowers);
		hyacidus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.hyacidusRecipe));

		pollidisiac = new BLexiconEntry(LibLexicon.FFLOWER_POLLIDISIAC, categoryFunctionalFlowers);
		pollidisiac.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.pollidisiacRecipe));

		clayconia = new BLexiconEntry(LibLexicon.FFLOWER_CLAYCONIA, categoryFunctionalFlowers);
		clayconia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.clayconiaRecipe));

		loonium = new BLexiconEntry(LibLexicon.FFLOWER_LOONIUM, categoryFunctionalFlowers);
		loonium.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.looniumRecipe));

		daffomill = new BLexiconEntry(LibLexicon.FFLOWER_DAFFOMILL, categoryFunctionalFlowers);
		daffomill.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.daffomillRecipe));

		vinculotus = new BLexiconEntry(LibLexicon.FFLOWER_VINCULOTUS, categoryFunctionalFlowers);
		vinculotus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.vinculotusRecipe));

		// GENERATING FLOWERS ENTRIES
		generatingIntro = new BLexiconEntry(LibLexicon.GFLOWER_INTRO, categoryGenerationFlowers);
		generatingIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		daybloom = new BLexiconEntry(LibLexicon.GFLOWER_DAYBLOOM, categoryGenerationFlowers);
		daybloom.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageImage("3", LibResources.ENTRY_DIMINISHING_RETURNS), new PagePetalRecipe("2", ModPetalRecipes.daybloomRecipe));

		nightshade = new BLexiconEntry(LibLexicon.GFLOWER_NIGHTSHADE, categoryGenerationFlowers);
		nightshade.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.nightshadeRecipe));

		endoflame = new BLexiconEntry(LibLexicon.GFLOWER_ENDOFLAME, categoryGenerationFlowers);
		endoflame.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModPetalRecipes.endoflameRecipe));

		hydroangeas = new BLexiconEntry(LibLexicon.GFLOWER_HYDROANGEAS, categoryGenerationFlowers);
		hydroangeas.setLexiconPages(new PageText("0"), new PageImage("2", LibResources.ENTRY_HYDROANGEAS), new PagePetalRecipe("1", ModPetalRecipes.hydroangeasRecipe));

		thermalily = new BLexiconEntry(LibLexicon.GFLOWER_THERMALILY, categoryGenerationFlowers);
		thermalily.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.thermalilyRecipe));

		arcaneRose = new BLexiconEntry(LibLexicon.GFLOWER_ARCANE_ROSE, categoryGenerationFlowers);
		arcaneRose.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.arcaneRoseRecipe));

		munchdew = new BLexiconEntry(LibLexicon.GFLOWER_MUNCHDEW, categoryGenerationFlowers);
		munchdew.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.munchdewRecipe));

		entropinnyum = new BLexiconEntry(LibLexicon.GFLOWER_ENTROPINNYUM, categoryGenerationFlowers);
		entropinnyum.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.entropinnyumRecipe));

		kekimurus = new BLexiconEntry(LibLexicon.GFLOWER_KEKIMURUS, categoryGenerationFlowers);
		kekimurus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModPetalRecipes.kekimurusRecipe));

		gourmaryllis = new BLexiconEntry(LibLexicon.GFLOWER_GOURMARYLLIS, categoryGenerationFlowers);
		gourmaryllis.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PagePetalRecipe("3", ModPetalRecipes.gourmaryllisRecipe));

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

		alchemy = new BLexiconEntry(LibLexicon.DEVICE_ALCHEMY, categoryDevices);
		alchemy.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeAlchemyCatalyst),
				new PageManaInfusionRecipe("2", ModManaAlchemyRecipes.leatherRecipe), new PageManaInfusionRecipe("3", ModManaAlchemyRecipes.woodRecipes), new PageManaInfusionRecipe("4", ModManaAlchemyRecipes.saplingRecipes), new PageManaInfusionRecipe("5", ModManaAlchemyRecipes.glowstoneDustRecipe),
				new PageManaInfusionRecipe("6", ModManaAlchemyRecipes.quartzRecipes), new PageManaInfusionRecipe("7", ModManaAlchemyRecipes.chiseledBrickRecipe), new PageManaInfusionRecipe("8", ModManaAlchemyRecipes.iceRecipe), new PageManaInfusionRecipe("9", ModManaAlchemyRecipes.swampFolliageRecipes),
				new PageManaInfusionRecipe("10", ModManaAlchemyRecipes.fishRecipes), new PageManaInfusionRecipe("11", ModManaAlchemyRecipes.cropRecipes), new PageManaInfusionRecipe("12", ModManaAlchemyRecipes.potatoRecipe), new PageManaInfusionRecipe("13", ModManaAlchemyRecipes.netherWartRecipe),
				new PageManaInfusionRecipe("14", ModManaAlchemyRecipes.gunpowderAndFlintRecipes), new PageManaInfusionRecipe("15", ModManaAlchemyRecipes.nameTagRecipe), new PageManaInfusionRecipe("16", ModManaAlchemyRecipes.stringRecipes), new PageManaInfusionRecipe("17", ModManaAlchemyRecipes.slimeballCactusRecipes),
				new PageManaInfusionRecipe("18", ModManaAlchemyRecipes.enderPearlRecipe), new PageManaInfusionRecipe("19", ModManaAlchemyRecipes.redstoneToGlowstoneRecipes), new PageManaInfusionRecipe("20", ModManaAlchemyRecipes.sandRecipe), new PageManaInfusionRecipe("21", ModManaAlchemyRecipes.coarseDirtRecipe));

		openCrate = new BLexiconEntry(LibLexicon.DEVICE_OPEN_CRATE, categoryDevices);
		openCrate.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeOpenCrate));

		forestEye = new BLexiconEntry(LibLexicon.DEVICE_FOREST_EYE, categoryDevices);
		forestEye.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeForestEye));

		forestDrum = new BLexiconEntry(LibLexicon.DEVICE_FOREST_DRUM, categoryDevices);
		forestDrum.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeForestDrum));

		platform = new BLexiconEntry(LibLexicon.DEVICE_PLATFORM, categoryDevices);
		platform.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePlatform));

		conjurationCatalyst = new ALexiconEntry(LibLexicon.DEVICE_MANA_CONJURATION, categoryDevices);
		conjurationCatalyst.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeConjurationCatalyst), new PageManaInfusionRecipe("2", ModManaConjurationRecipes.redstoneRecipe), new PageManaInfusionRecipe("3", ModManaConjurationRecipes.glowstoneRecipe), new PageManaInfusionRecipe("4", ModManaConjurationRecipes.quartzRecipe),
				new PageManaInfusionRecipe("5", ModManaConjurationRecipes.coalRecipe), new PageManaInfusionRecipe("6", ModManaConjurationRecipes.snowballRecipe), new PageManaInfusionRecipe("7", ModManaConjurationRecipes.netherrackRecipe), new PageManaInfusionRecipe("8", ModManaConjurationRecipes.soulSandRecipe), new PageManaInfusionRecipe("9", ModManaConjurationRecipes.gravelRecipe));

		spectralPlatform = new ALexiconEntry(LibLexicon.DEVICE_SPECTRAL_PLATFORM, categoryDevices);
		spectralPlatform.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeSpectralPlatform));

		gatherDrum = new ALexiconEntry(LibLexicon.DEVICE_GATHER_DRUM, categoryDevices);
		gatherDrum.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeGatherDrum));

		spawnerClaw = new ALexiconEntry(LibLexicon.DEVICE_SPAWNER_CLAW, categoryDevices);
		spawnerClaw.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeSpawnerClaw));

		craftCrate = new ALexiconEntry(LibLexicon.DEVICE_CRAFT_CRATE, categoryDevices);
		craftCrate.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipePlaceholder), new PageText("3"), new PageText("4"), new PageImage("5", LibResources.ENTRY_CRAFT_CRATE), new PageCraftingRecipe("6", ModCrafingRecipes.recipeCraftCrate));

		enderEyeBlock = new BLexiconEntry(LibLexicon.DEVICE_ENDER_EYE_BLOCK, categoryDevices);
		enderEyeBlock.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeEnderEyeBlock));
		
		// TOOLS ENTRIES
		manaBlaster = new BLexiconEntry(LibLexicon.TOOL_MANA_BLASTER, categoryTools);
		manaBlaster.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipeManaBlaster));

		grassSeeds = new BLexiconEntry(LibLexicon.TOOL_GRASS_SEEDS, categoryTools);
		grassSeeds.setLexiconPages(new PageText("0"), new PageManaInfusionRecipe("1", ModManaInfusionRecipes.grassSeedsRecipe), new PageManaInfusionRecipe("2", ModManaInfusionRecipes.podzolSeedsRecipe), new PageManaInfusionRecipe("3", ModManaInfusionRecipes.mycelSeedsRecipes));

		dirtRod = new BLexiconEntry(LibLexicon.TOOL_DIRT_ROD, categoryTools);
		dirtRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeDirtRod));

		terraformRod = new BLexiconEntry(LibLexicon.TOOL_TERRAFORM_ROD, categoryTools);
		terraformRod.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipeTerraformRod));

		manasteelGear = new BLexiconEntry(LibLexicon.TOOL_MANASTEEL_GEAR, categoryTools);
		manasteelGear.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManasteelPick), new PageCraftingRecipe("2", ModCrafingRecipes.recipeManasteelShovel), new PageCraftingRecipe("3", ModCrafingRecipes.recipeManasteelAxe),
				new PageCraftingRecipe("4", ModCrafingRecipes.recipeManasteelShears), new PageCraftingRecipe("5", ModCrafingRecipes.recipeManasteelSword),
				new PageCraftingRecipe("6", ModCrafingRecipes.recipeManasteelHelm), new PageCraftingRecipe("7", ModCrafingRecipes.recipeManasteelChest), new PageCraftingRecipe("8", ModCrafingRecipes.recipeManasteelLegs), new PageCraftingRecipe("9", ModCrafingRecipes.recipeManasteelBoots));

		terrasteelArmor = new BLexiconEntry(LibLexicon.TOOL_TERRASTEEL_ARMOR, categoryTools);
		terrasteelArmor.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTerrasteelHelm), new PageCraftingRecipe("2", ModCrafingRecipes.recipeTerrasteelChest), new PageCraftingRecipe("3", ModCrafingRecipes.recipeTerrasteelLegs), new PageCraftingRecipe("4", ModCrafingRecipes.recipeTerrasteelBoots));

		grassHorn = new BLexiconEntry(LibLexicon.TOOL_GRASS_HORN, categoryTools);
		grassHorn.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeGrassHorn));

		terraBlade = new BLexiconEntry(LibLexicon.TOOL_TERRA_SWORD, categoryTools);
		terraBlade.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTerraSword));

		terraPick = new BLexiconEntry(LibLexicon.TOOL_TERRA_PICK, categoryTools);
		terraPick.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"), new PageText("4"), new PageCraftingRecipe("5", ModCrafingRecipes.recipeTerraPick));

		enderDagger = new BLexiconEntry(LibLexicon.TOOL_ENDER_DAGGER, categoryTools);
		enderDagger.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeEnderDagger));

		waterRod = new BLexiconEntry(LibLexicon.TOOL_WATER_ROD, categoryTools);
		waterRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeWaterRod));

		elfGear = new ALexiconEntry(LibLexicon.TOOL_ELF_GEAR, categoryTools);
		elfGear.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageCraftingRecipe("3", ModCrafingRecipes.recipeElementiumPick), new PageText("4"), new PageCraftingRecipe("5", ModCrafingRecipes.recipeElementiumShovel),
				new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipeElementiumAxe), new PageText("8"), new PageCraftingRecipe("9", ModCrafingRecipes.recipeElementiumShears), new PageText("10"), new PageCraftingRecipe("11", ModCrafingRecipes.recipeElementiumSword),
				new PageCraftingRecipe("12", ModCrafingRecipes.recipeElementiumHelm), new PageCraftingRecipe("13", ModCrafingRecipes.recipeElementiumChest), new PageCraftingRecipe("14", ModCrafingRecipes.recipeElementiumLegs), new PageCraftingRecipe("15", ModCrafingRecipes.recipeElementiumBoots));

		openBucket = new ALexiconEntry(LibLexicon.TOOL_OPEN_BUCKET, categoryTools);
		openBucket.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeOpenBucket));

		spawnerMover = new ALexiconEntry(LibLexicon.TOOL_SPAWNER_MOVER, categoryTools);
		spawnerMover.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeSpawnerMover));

		rainbowRod = new ALexiconEntry(LibLexicon.TOOL_RAINBOW_ROD, categoryTools);
		rainbowRod.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeRainbowRod));

		tornadoRod = new BLexiconEntry(LibLexicon.TOOL_TORNADO_ROD, categoryTools);
		tornadoRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTornadoRod));

		fireRod = new BLexiconEntry(LibLexicon.TOOL_FIRE_ROD, categoryTools);
		fireRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeFireRod));

		vineBall = new BLexiconEntry(LibLexicon.TOOL_VINE_BALL, categoryTools);
		vineBall.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeVineBall), new PageCraftingRecipe("3", ModCrafingRecipes.recipeSlingshot));

		laputaShard = new ALexiconEntry(LibLexicon.TOOL_LAPUTA_SHARD, categoryTools);
		laputaShard.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipesLaputaShard));

		virus = new ALexiconEntry(LibLexicon.TOOL_VIRUS, categoryTools);
		virus.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeVirusZombie), new PageCraftingRecipe("2", ModCrafingRecipes.recipeVirusSkeleton));

		skyDirtRod = new ALexiconEntry(LibLexicon.TOOL_SKY_DIRT_ROD, categoryTools);
		skyDirtRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeSkyDirtRod));

		// BAUBLES ENTRIES
		baublesIntro = new BLexiconEntry(LibLexicon.BAUBLE_INTRO, categoryBaubles);
		baublesIntro.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_BAUBLES), new PageText("2"));

		tinyPlanet = new BLexiconEntry(LibLexicon.BAUBLE_TINY_PLANET, categoryBaubles);
		tinyPlanet.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTinyPlanet), new PageCraftingRecipe("2", ModCrafingRecipes.recipeTinyPlanetBlock));

		manaRing = new BLexiconEntry(LibLexicon.BAUBLE_MANA_RING, categoryBaubles);
		manaRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeManaRing), new PageCraftingRecipe("2", ModCrafingRecipes.recipeGreaterManaRing));

		auraRing = new BLexiconEntry(LibLexicon.BAUBLE_AURA_RING, categoryBaubles);
		auraRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeAuraRing), new PageCraftingRecipe("2", ModCrafingRecipes.recipeGreaterAuraRing));

		travelBelt = new BLexiconEntry(LibLexicon.BAUBLE_TRAVEL_BELT, categoryBaubles);
		travelBelt.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeTravelBelt));

		knockbacklBelt = new BLexiconEntry(LibLexicon.BAUBLE_KNOCKBACK_BELT, categoryBaubles);
		knockbacklBelt.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeKnocbackBelt));

		icePendant = new BLexiconEntry(LibLexicon.BAUBLE_ICE_PENDANT, categoryBaubles);
		icePendant.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeIcePendant));

		lavaPendant = new BLexiconEntry(LibLexicon.BAUBLE_LAVA_PENDANT, categoryBaubles);
		lavaPendant.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLavaPendant));

		goldLaurel = new ALexiconEntry(LibLexicon.BAUBLE_GOLDEN_LAUREL, categoryBaubles);
		goldLaurel.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeGoldenLaurel));

		waterRing = new BLexiconEntry(LibLexicon.BAUBLE_WATER_RING, categoryBaubles);
		waterRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeWaterRing));

		miningRing = new BLexiconEntry(LibLexicon.BAUBLE_MINING_RING, categoryBaubles);
		miningRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeMiningRing));

		magnetRing = new BLexiconEntry(LibLexicon.BAUBLE_MAGNET_RING, categoryBaubles);
		magnetRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeMagnetRing));

		divaCharm = new ALexiconEntry(LibLexicon.BAUBLE_DIVA_CHARM, categoryBaubles);
		divaCharm.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeDivaCharm));

		flightTiara = new ALexiconEntry(LibLexicon.BAUBLE_FLIGHT_TIARA, categoryBaubles);
		flightTiara.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeFlightTiara));

		pixieRing = new ALexiconEntry(LibLexicon.BAUBLE_PIXIE_RING, categoryBaubles);
		pixieRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipePixieRing));

		superTravelBelt = new ALexiconEntry(LibLexicon.BAUBLE_SUPER_TRAVEL_BELT, categoryBaubles);
		superTravelBelt.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeSuperTravelBelt));

		reachRing = new ALexiconEntry(LibLexicon.BAUBLE_REACH_RING, categoryBaubles);
		reachRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeReachRing));

		// ALFHOMANCY ENTRIES
		alfhomancyIntro = new BLexiconEntry(LibLexicon.ALF_INTRO, categoryAlfhomancy);
		alfhomancyIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", ModCrafingRecipes.recipeAlfPortal), new PageCraftingRecipe("3", ModCrafingRecipes.recipeNaturaPylon),
				new PageImage("4", LibResources.ENTRY_PORTAL0), new PageImage("5", LibResources.ENTRY_PORTAL1), new PageImage("6", LibResources.ENTRY_PORTAL2), new PageImage("7", LibResources.ENTRY_PORTAL3),
				new PageImage("8", LibResources.ENTRY_PORTAL4), new PageText("9"), new PageText("10"), new PageText("11"));

		elvenMessage = new ALexiconEntry(LibLexicon.ALF_MESSAGE, categoryAlfhomancy);
		elvenMessage.setPriority().setLexiconPages(new PageImage("0", LibResources.ENTRY_ELVEN_GARDE), new PageText("1"), new PageText("2"), new PageText("3"), new PageText("4"), new PageText("5"), new PageText("6"));

		elvenResources = new ALexiconEntry(LibLexicon.ALF_RESOURCES, categoryAlfhomancy);
		elvenResources.setPriority().setLexiconPages(new PageText("0"), new PageElvenRecipe("1", ModElvenTradeRecipes.dreamwoodRecipe), new PageText("2"), new PageElvenRecipe("3", ModElvenTradeRecipes.elementiumRecipes), new PageElvenRecipe("4", ModElvenTradeRecipes.pixieDustRecipe), new PageElvenRecipe("5", ModElvenTradeRecipes.dragonstoneRecipe), new PageText("6"), new PageElvenRecipe("7", ModElvenTradeRecipes.elvenQuartzRecipe));

		gaiaRitual = new ALexiconEntry(LibLexicon.ALF_GAIA_RITUAL, categoryAlfhomancy);
		gaiaRitual.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeGaiaPylon), new PageImage("2", LibResources.ENTRY_GAIA_RITUAL), new PageText("3"), new PageText("4"));
		LexiconRecipeMappings.map(new ItemStack(ModItems.manaResource, 1, 5), gaiaRitual, 0);

		// MISCLENAEOUS ENTRIES
		unstableBlocks = new BLexiconEntry(LibLexicon.MISC_UNSTABLE_BLOCKS, categoryMisc);
		unstableBlocks.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_UNSTABLE_BLOCK), new PageCraftingRecipe("2", ModCrafingRecipes.recipesUnstableBlocks), new PageText("3"), new PageImage("4", LibResources.ENTRY_UNSTABLE_BEACON), new PageCraftingRecipe("5", ModCrafingRecipes.recipesManaBeacons), new PageText("6"), new PageCraftingRecipe("7", ModCrafingRecipes.recipesSignalFlares));

		decorativeBlocks = new BLexiconEntry(LibLexicon.MISC_DECORATIVE_BLOCKS, categoryMisc);
		if(ConfigHandler.darkQuartzEnabled)
			decorativeBlocks.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLivingrockDecor1), new PageCraftingRecipe("2", ModCrafingRecipes.recipeLivingrockDecor2), new PageCraftingRecipe("3", ModCrafingRecipes.recipeLivingrockDecor3), new PageCraftingRecipe("4", ModCrafingRecipes.recipeLivingrockDecor4),
					new PageCraftingRecipe("5", ModCrafingRecipes.recipeLivingwoodDecor1), new PageCraftingRecipe("6", ModCrafingRecipes.recipeLivingwoodDecor2), new PageCraftingRecipe("7", ModCrafingRecipes.recipeLivingwoodDecor3), new PageCraftingRecipe("8", ModCrafingRecipes.recipeLivingwoodDecor4), new PageCraftingRecipe("9", ModCrafingRecipes.recipeLivingwoodDecor5),
					new PageText("10"), new PageCraftingRecipe("11", ModCrafingRecipes.recipeDarkQuartz), new PageManaInfusionRecipe("12", ModManaInfusionRecipes.manaQuartzRecipe), new PageCraftingRecipe("13", ModCrafingRecipes.recipeBlazeQuartz), new PageCraftingRecipe("14", ModCrafingRecipes.recipesLavenderQuartz), new PageCraftingRecipe("15", ModCrafingRecipes.recipeRedQuartz),
					new PageText("16"), new PageCraftingRecipe("17", ModCrafingRecipes.recipeReedBlock), new PageCraftingRecipe("18", ModCrafingRecipes.recipeThatch), new PageCraftingRecipe("19", ModCrafingRecipes.recipeRoofTile), new PageCraftingRecipe("20", ModCrafingRecipes.recipeNetherBrick), new PageCraftingRecipe("21", ModCrafingRecipes.recipeSoulBrick), new PageCraftingRecipe("22", ModCrafingRecipes.recipeSnowBrick));
		else decorativeBlocks.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipeLivingrockDecor1), new PageCraftingRecipe("2", ModCrafingRecipes.recipeLivingrockDecor2), new PageCraftingRecipe("3", ModCrafingRecipes.recipeLivingrockDecor3), new PageCraftingRecipe("4", ModCrafingRecipes.recipeLivingrockDecor4),
				new PageCraftingRecipe("5", ModCrafingRecipes.recipeLivingwoodDecor1), new PageCraftingRecipe("6", ModCrafingRecipes.recipeLivingwoodDecor2), new PageCraftingRecipe("7", ModCrafingRecipes.recipeLivingwoodDecor3), new PageCraftingRecipe("8", ModCrafingRecipes.recipeLivingwoodDecor4), new PageCraftingRecipe("9", ModCrafingRecipes.recipeLivingwoodDecor5),
				new PageText("10"), new PageManaInfusionRecipe("12", ModManaInfusionRecipes.manaQuartzRecipe), new PageCraftingRecipe("13", ModCrafingRecipes.recipeBlazeQuartz), new PageCraftingRecipe("14", ModCrafingRecipes.recipesLavenderQuartz), new PageCraftingRecipe("15", ModCrafingRecipes.recipeRedQuartz),
				new PageText("16"), new PageCraftingRecipe("17", ModCrafingRecipes.recipeReedBlock), new PageCraftingRecipe("18", ModCrafingRecipes.recipeThatch), new PageCraftingRecipe("19", ModCrafingRecipes.recipeRoofTile), new PageCraftingRecipe("20", ModCrafingRecipes.recipeNetherBrick), new PageCraftingRecipe("21", ModCrafingRecipes.recipeSoulBrick), new PageCraftingRecipe("22", ModCrafingRecipes.recipeSnowBrick));

		dispenserTweaks = new BLexiconEntry(LibLexicon.MISC_DISPENSER_TWEAKS, categoryMisc);
		dispenserTweaks.setLexiconPages(new PageText("0"));

		shinyFlowers = new BLexiconEntry(LibLexicon.MISC_SHINY_FLOWERS, categoryMisc);
		shinyFlowers.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModCrafingRecipes.recipesShinyFlowers), new PageCraftingRecipe("2", ModCrafingRecipes.recipesMiniIsland));

		prismarine = new BLexiconEntry(LibLexicon.MISC_PRISMARINE, categoryMisc);
		prismarine.setLexiconPages(new PageText("0"), new PageText("1"), new PageManaInfusionRecipe("2", ModManaAlchemyRecipes.prismarineRecipe), new PageCraftingRecipe("3", ModCrafingRecipes.recipePrismarine), new PageCraftingRecipe("4", ModCrafingRecipes.recipePrismarineBrick), new PageCraftingRecipe("5", ModCrafingRecipes.recipeDarkPrismarine), new PageCraftingRecipe("6", ModCrafingRecipes.recipeSeaLamp));

		shedding = new BLexiconEntry(LibLexicon.MISC_SHEDDING, categoryMisc);
		shedding.setLexiconPages(new PageText("0"));

		tinyPotato = new BLexiconEntry(LibLexicon.MISC_TINY_POTATO, categoryMisc);
		tinyPotato.setLexiconPages(new PageText("0"), new PageManaInfusionRecipe("1", ModManaInfusionRecipes.tinyPotatoRecipe));

		headCreating = new ALexiconEntry(LibLexicon.MISC_HEAD_CREATING, categoryMisc);
		headCreating.setLexiconPages(new PageText("0"), new PageRuneRecipe("1", ModRuneRecipes.recipeHead));

		azulejo = new BLexiconEntry(LibLexicon.MISC_AZULEJO, categoryMisc);
		azulejo.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_AZULEJOS), new PageCraftingRecipe("2", ModCrafingRecipes.recipeAzulejo), new PageCraftingRecipe("3", ModCrafingRecipes.recipesAzulejoCycling));
	}
}
