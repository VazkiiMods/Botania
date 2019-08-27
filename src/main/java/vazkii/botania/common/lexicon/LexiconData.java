/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 9:12:15 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import com.google.common.base.Stopwatch;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModMultiblocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemBaubleCosmetic;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageLoreText;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageTerrasteel;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibLexicon;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class LexiconData {

	public static LexiconEntry welcome;
	public static LexiconEntry tutorial;

	public static LexiconEntry flowers;
	public static LexiconEntry apothecary;
	public static LexiconEntry lexicon;
	public static LexiconEntry wand;
	public static LexiconEntry pureDaisy;
	public static LexiconEntry runicAltar;
	public static LexiconEntry terrasteel;
	public static LexiconEntry blackLotus;
	public static LexiconEntry flowerBag;
	public static LexiconEntry gardenOfGlass;

	public static LexiconEntry manaIntro;
	public static LexiconEntry spreader;
	public static LexiconEntry pool;
	public static LexiconEntry lenses;
	public static LexiconEntry distributor;
	public static LexiconEntry manaVoid;
	public static LexiconEntry manaTablet;
	public static LexiconEntry manaMirror;
	public static LexiconEntry manaDetector;
	public static LexiconEntry redstoneSpreader;
	public static LexiconEntry manastar;
	public static LexiconEntry dreamwoodSpreader;
	public static LexiconEntry elvenLenses;
	public static LexiconEntry sparks;
	public static LexiconEntry sparkUpgrades;
	public static LexiconEntry rfGenerator;
	public static LexiconEntry prism;
	public static LexiconEntry poolCart;
	public static LexiconEntry sparkChanger;
	public static LexiconEntry bellows;

	public static LexiconEntry functionalIntro;
	public static LexiconEntry flowerShrinking;
	public static LexiconEntry flowerSpeed;
	public static LexiconEntry jadedAmaranthus;
	public static LexiconEntry bellethorne;
	public static LexiconEntry dreadthorne;
	public static LexiconEntry heiseiDream;
	public static LexiconEntry tigerseye;
	public static LexiconEntry orechid;
	public static LexiconEntry orechidIgnem;
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
	public static LexiconEntry spectranthemum;
	public static LexiconEntry medumone;
	public static LexiconEntry marimorphosis;
	public static LexiconEntry bubbell;
	public static LexiconEntry solegnolia;
	public static LexiconEntry bergamute;

	public static LexiconEntry generatingIntro;
	public static LexiconEntry endoflame;
	public static LexiconEntry hydroangeas;
	public static LexiconEntry thermalily;
	public static LexiconEntry arcaneRose;
	public static LexiconEntry munchdew;
	public static LexiconEntry entropinnyum;
	public static LexiconEntry kekimurus;
	public static LexiconEntry gourmaryllis;
	public static LexiconEntry narslimmus;
	public static LexiconEntry spectrolus;
	public static LexiconEntry rafflowsia;
	public static LexiconEntry shulkMeNot;
	public static LexiconEntry dandelifeon;

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
	public static LexiconEntry craftCrate;
	public static LexiconEntry brewery;
	public static LexiconEntry flasks;
	public static LexiconEntry complexBrews;
	public static LexiconEntry incense;
	public static LexiconEntry hourglass;
	public static LexiconEntry ghostRail;
	public static LexiconEntry canopyDrum;
	public static LexiconEntry cocoon;
	public static LexiconEntry manaBomb;
	public static LexiconEntry teruTeruBozu;
	public static LexiconEntry avatar;
	public static LexiconEntry felPumpkin;
	public static LexiconEntry animatedTorch;

	public static LexiconEntry manaBlaster;
	public static LexiconEntry grassSeeds;
	public static LexiconEntry dirtRod;
	public static LexiconEntry terraformRod;
	public static LexiconEntry manasteelGear;
	public static LexiconEntry terrasteelArmor;
	public static LexiconEntry grassHorn;
	public static LexiconEntry terraBlade;
	public static LexiconEntry terraPick;
	public static LexiconEntry waterRod;
	public static LexiconEntry elfGear;
	public static LexiconEntry openBucket;
	public static LexiconEntry rainbowRod;
	public static LexiconEntry tornadoRod;
	public static LexiconEntry fireRod;
	public static LexiconEntry vineBall;
	public static LexiconEntry laputaShard;
	public static LexiconEntry virus;
	public static LexiconEntry skyDirtRod;
	public static LexiconEntry glassPick;
	public static LexiconEntry diviningRod;
	public static LexiconEntry gravityRod;
	public static LexiconEntry missileRod;
	public static LexiconEntry craftingHalo;
	public static LexiconEntry clip;
	public static LexiconEntry cobbleRod;
	public static LexiconEntry smeltRod;
	public static LexiconEntry worldSeed;
	public static LexiconEntry spellCloth;
	public static LexiconEntry thornChakram;
	public static LexiconEntry fireChakram;
	public static LexiconEntry overgrowthSeed;
	public static LexiconEntry livingwoodBow;
	public static LexiconEntry crystalBow;
	public static LexiconEntry temperanceStone;
	public static LexiconEntry terraAxe;
	public static LexiconEntry obedienceStick;
	public static LexiconEntry slimeBottle;
	public static LexiconEntry exchangeRod;
	public static LexiconEntry manaweave;
	public static LexiconEntry autocraftingHalo;
	public static LexiconEntry sextant;
	public static LexiconEntry astrolabe;

	public static LexiconEntry enderAir;
	public static LexiconEntry enderEyeBlock;
	public static LexiconEntry pistonRelay;
	public static LexiconEntry enderHand;
	public static LexiconEntry enderDagger;
	public static LexiconEntry spawnerClaw;
	public static LexiconEntry redString;
	public static LexiconEntry flightTiara;
	public static LexiconEntry corporea;
	public static LexiconEntry corporeaIndex;
	public static LexiconEntry corporeaFunnel;
	public static LexiconEntry corporeaInterceptor;
	public static LexiconEntry endStoneDecor;
	public static LexiconEntry spawnerMover;
	public static LexiconEntry keepIvy;
	public static LexiconEntry blackHoleTalisman;
	public static LexiconEntry corporeaCrystalCube;
	public static LexiconEntry luminizerTransport;
	public static LexiconEntry starSword;
	public static LexiconEntry thunderSword;
	public static LexiconEntry corporeaRetainer;

	public static LexiconEntry baublesIntro;
	public static LexiconEntry cosmeticBaubles;
	public static LexiconEntry tinyPlanet;
	public static LexiconEntry manaRing;
	public static LexiconEntry auraRing;
	public static LexiconEntry travelBelt;
	public static LexiconEntry knockbacklBelt;
	public static LexiconEntry icePendant;
	public static LexiconEntry lavaPendant;
	public static LexiconEntry waterRing;
	public static LexiconEntry miningRing;
	public static LexiconEntry magnetRing;
	public static LexiconEntry divaCharm;
	public static LexiconEntry pixieRing;
	public static LexiconEntry superTravelBelt;
	public static LexiconEntry reachRing;
	public static LexiconEntry itemFinder;
	public static LexiconEntry superLavaPendant;
	public static LexiconEntry bloodPendant;
	public static LexiconEntry judgementCloaks;
	public static LexiconEntry monocle;
	public static LexiconEntry swapRing;
	public static LexiconEntry speedUpBelt;
	public static LexiconEntry baubleBox;
	public static LexiconEntry dodgeRing;
	public static LexiconEntry invisibilityCloak;
	public static LexiconEntry cloudPendant;
	public static LexiconEntry superCloudPendant;
	public static LexiconEntry thirdEye;
	public static LexiconEntry goddessCharm;

	public static LexiconEntry alfhomancyIntro;
	public static LexiconEntry elvenMessage;
	public static LexiconEntry elvenResources;
	public static LexiconEntry gaiaRitual;
	public static LexiconEntry gaiaRitualHardmode;
	public static LexiconEntry elvenLore;
	public static LexiconEntry relics;
	public static LexiconEntry relicInfo;
	public static LexiconEntry infiniteFruit;
	public static LexiconEntry kingKey;
	public static LexiconEntry flugelEye;
	public static LexiconEntry thorRing;
	public static LexiconEntry lokiRing;
	public static LexiconEntry odinRing;

	public static LexiconEntry unstableBlocks;
	public static LexiconEntry decorativeBlocks;
	public static LexiconEntry dispenserTweaks;
	public static LexiconEntry shinyFlowers;
	public static LexiconEntry tinyPotato;
	public static LexiconEntry headCreating;
	public static LexiconEntry azulejo;
	public static LexiconEntry starfield;
	public static LexiconEntry mushrooms;
	public static LexiconEntry phantomInk;
	public static LexiconEntry blazeBlock;
	public static LexiconEntry challenges;
	public static LexiconEntry cacophonium;
	public static LexiconEntry pavement;
	public static LexiconEntry preventingDecay;

	public static LexiconEntry tcIntegration;
	public static LexiconEntry bcIntegration;
	public static LexiconEntry banners;

	public static void reload() {
		Stopwatch stopwatch = Stopwatch.createStarted();

		BotaniaAPI.getAllCategories().clear();
		BotaniaAPI.getAllEntries().clear();

		LexiconRecipeMappings.clear();

		BotaniaAPI.addCategory(BotaniaAPI.categoryBasics = new BLexiconCategory(LibLexicon.CATEGORY_BASICS, 9));
		BotaniaAPI.addCategory(BotaniaAPI.categoryMana = new BLexiconCategory(LibLexicon.CATEGORY_MANA, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryGenerationFlowers = new BLexiconCategory(LibLexicon.CATEGORY_GENERATION_FLOWERS, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryFunctionalFlowers = new BLexiconCategory(LibLexicon.CATEGORY_FUNCTIONAL_FLOWERS, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryDevices = new BLexiconCategory(LibLexicon.CATEGORY_DEVICES, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryTools = new BLexiconCategory(LibLexicon.CATEGORY_TOOLS, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryBaubles = new BLexiconCategory(LibLexicon.CATEGORY_BAUBLES, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryEnder = new BLexiconCategory(LibLexicon.CATEGORY_ENDER, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryAlfhomancy = new BLexiconCategory(LibLexicon.CATEGORY_ALFHOMANCY, 5));
		BotaniaAPI.addCategory(BotaniaAPI.categoryMisc = new BLexiconCategory(LibLexicon.CATEGORY_MISC, 0));

		LexiconCategory categoryBasics = BotaniaAPI.categoryBasics;
		LexiconCategory categoryMana = BotaniaAPI.categoryMana;
		LexiconCategory categoryGenerationFlowers = BotaniaAPI.categoryGenerationFlowers;
		LexiconCategory categoryFunctionalFlowers = BotaniaAPI.categoryFunctionalFlowers;
		LexiconCategory categoryDevices = BotaniaAPI.categoryDevices;
		LexiconCategory categoryTools = BotaniaAPI.categoryTools;
		LexiconCategory categoryBaubles = BotaniaAPI.categoryBaubles;
		LexiconCategory categoryEnder = BotaniaAPI.categoryEnder;
		LexiconCategory categoryAlfhomancy = BotaniaAPI.categoryAlfhomancy;
		LexiconCategory categoryMisc = BotaniaAPI.categoryMisc;

		// BASICS ENTRIES
		welcome = new WelcomeLexiconEntry();
		tutorial = new TutLexiconEntry();

		Item[] petals = Arrays.stream(DyeColor.values()).map(ModItems::getPetal).toArray(Item[]::new);
		Item[] dyes = Arrays.stream(DyeColor.values()).map(ModItems::getDye).toArray(Item[]::new);
		Item[] petalBlocks = Arrays.stream(DyeColor.values()).map(ModBlocks::getPetalBlock).map(Block::asItem).toArray(Item[]::new);
		Item[] shinyFlowerItems = Arrays.stream(DyeColor.values()).map(ModBlocks::getShinyFlower).map(Block::asItem).toArray(Item[]::new);
		Item[] floatingFlowerItems = Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).map(Block::asItem).toArray(Item[]::new);
		Item[] mushroomItems = Arrays.stream(DyeColor.values()).map(ModBlocks::getMushroom).map(Block::asItem).toArray(Item[]::new);

		flowers = new BasicLexiconEntry(LibLexicon.BASICS_FLOWERS, categoryBasics);
		flowers.setPriority()
				.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_FLOWERS), new PageText("2"),
						new PageCraftingRecipe("3", inGroup(LibItemNames.PETAL), petals),
						new PageCraftingRecipe("4", ModItems.pestleAndMortar),
						new PageCraftingRecipe("5", inGroup(LibItemNames.DYE), dyes),
						// todo 1.12 new PageCraftingRecipe("5.5", ModCraftingRecipes.recipesDyesVanilla),
						new PageText("6"),
						new PageCraftingRecipe("7", idEndsWith("powder"), ModItems.fertilizer),
						new PageCraftingRecipe("8", idEndsWith("dye"), ModItems.fertilizer), new PageText("10"),
						new PageText("12"), new PageCraftingRecipe("11", inGroup("petal_double"), petals),
						new PageCraftingRecipe("9", petalBlocks),
						new PageCraftingRecipe("13", inGroup("petal_block_deconstruct"), petals))
				.setIcon(new ItemStack(ModBlocks.pinkFlower));

		apothecary = new BasicLexiconEntry(LibLexicon.BASICS_APOTHECARY, categoryBasics);
		apothecary.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_APOTHECARY),
				new PageText("2"), new PageText("3"), new PageText("4"), new PageText("7"), new PageText("6"),
				new PageCraftingRecipe("5", ModBlocks.defaultAltar.asItem()));

		lexicon = new BasicLexiconEntry(LibLexicon.BASICS_LEXICON, categoryBasics);
		lexicon.setPriority().setLexiconPages(new PageText("0"), new PageText("3"),
				new PageCraftingRecipe("1", ModItems.lexicon), new PageText("2"));

		wand = new BasicLexiconEntry(LibLexicon.BASICS_WAND, categoryBasics);
		wand.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.twigWand));

		pureDaisy = new BasicLexiconEntry(LibLexicon.BASICS_PURE_DAISY, categoryBasics);
		pureDaisy.setPriority()
				.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_PURE_DAISY),
						new PageCraftingRecipe("2", ModItems.livingwoodTwig), new PageText("4"),
						new PagePetalRecipe("3", ModSubtiles.pureDaisy.asItem()))
				.setIcon(new ItemStack(ModSubtiles.pureDaisy));
		pureDaisy.addExtraDisplayedRecipe(new ItemStack(ModBlocks.livingwood));
		pureDaisy.addExtraDisplayedRecipe(new ItemStack(ModBlocks.livingrock));
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingwood), pureDaisy, 1);
		LexiconRecipeMappings.map(new ItemStack(ModBlocks.livingrock), pureDaisy, 1);

		runicAltar = new BasicLexiconEntry(LibLexicon.BASICS_RUNE_ALTAR, categoryBasics);
		runicAltar.setPriority().setLexiconPages(new PageText("21"), new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.runeAltar.asItem()), new PageText("3"), new PageText("20"),
				new PageText("22"), new PageRuneRecipe("4", ModItems.runeWater),
				new PageRuneRecipe("5", ModItems.runeEarth),
				new PageRuneRecipe("6", ModItems.runeAir),
				new PageRuneRecipe("7", ModItems.runeFire),
				new PageRuneRecipe("8", ModItems.runeSpring),
				new PageRuneRecipe("9", ModItems.runeSummer),
				new PageRuneRecipe("10", ModItems.runeAutumn),
				new PageRuneRecipe("11", ModItems.runeWinter),
				new PageRuneRecipe("12", ModItems.runeMana),
				new PageRuneRecipe("13", ModItems.runeLust),
				new PageRuneRecipe("14", ModItems.runeGluttony),
				new PageRuneRecipe("15", ModItems.runeGreed),
				new PageRuneRecipe("16", ModItems.runeSloth),
				new PageRuneRecipe("17", ModItems.runeWrath),
				new PageRuneRecipe("18", ModItems.runeEnvy),
				new PageRuneRecipe("19", ModItems.runePride));

		terrasteel = new BasicLexiconEntry(LibLexicon.BASICS_TERRASTEEL, categoryBasics);
		terrasteel.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.terraPlate.asItem()),
				new PageText("2"), new PageMultiblock("4", ModMultiblocks.terrasteelPlate), new PageTerrasteel("3"))
				.setIcon(new ItemStack(ModItems.terrasteel));

		blackLotus = new BasicLexiconEntry(LibLexicon.BASICS_BLACK_LOTUS, categoryBasics);
		blackLotus.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.blackLotus));
		blackLotus.addExtraDisplayedRecipe(new ItemStack(ModItems.blackLotus));

		flowerBag = new BasicLexiconEntry(LibLexicon.BASICS_FLOWER_BAG, categoryBasics);
		flowerBag.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.flowerBag));

		if(Botania.gardenOfGlassLoaded) {
			gardenOfGlass = new BasicLexiconEntry(LibLexicon.BASICS_GARDEN_OF_GLASS, categoryBasics);
			gardenOfGlass.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
					new PageCraftingRecipe("3", GOG_RECIPE, Items.OAK_SAPLING),
					new PageCraftingRecipe("4", GOG_RECIPE, ModItems.fertilizer),
					new PageCraftingRecipe("5", GOG_RECIPE, Items.COBBLESTONE), new PageText("6"),
					new PageManaInfusionRecipe("7", Items.SUGAR_CANE),
					new PageCraftingRecipe("8", GOG_RECIPE, Items.SLIME_BALL),
					new PageManaInfusionRecipe("13", Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS), new PageText("9"),
					new PageText("11"), new PageCraftingRecipe("12", Items.END_PORTAL_FRAME));
			gardenOfGlass.setPriority().setIcon(new ItemStack(ModItems.livingroot));
		}

		if(Botania.thaumcraftLoaded)
			new CompatLexiconEntry("wrap", categoryBasics, "Thaumcraft").setLexiconPages(new PageText("0")); // lel

		// MANA ENTRIES
		manaIntro = new BasicLexiconEntry(LibLexicon.MANA_INTRO, categoryMana);
		manaIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));

		spreader = new BasicLexiconEntry(LibLexicon.MANA_SPREADER, categoryMana);
		spreader.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_SPREADER),
				new PageText("2"), new PageText("3"), new PageText("4"), new PageText("11"),
				new PageCraftingRecipe("5", ModBlocks.manaSpreader.asItem()), new PageText("10"));

		pool = new BasicLexiconEntry(LibLexicon.MANA_POOL, categoryMana);
		pool.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("9"),
						new PageCraftingRecipe("1", ModBlocks.manaPool.asItem()),
						new PageCraftingRecipe("10", ModBlocks.dilutedPool.asItem()), new PageText("14"),
						new PageText("2"), new PageText("8"),
						new PageManaInfusionRecipe("3", ModItems.manaSteel),
						new PageManaInfusionRecipe("4", ModItems.manaPearl),
						new PageManaInfusionRecipe("5", ModItems.manaDiamond),
						new PageManaInfusionRecipe("6", ModItems.manaPowder),
						new PageManaInfusionRecipe("11", ModBlocks.manaGlass.asItem()),
						new PageManaInfusionRecipe("12", ModItems.manaString),
						new PageCraftingRecipe("13", Items.COBWEB),
						new PageManaInfusionRecipe("7", ModItems.manaCookie))
				.setIcon(new ItemStack(ModBlocks.manaPool));

		sparks = new BasicLexiconEntry(LibLexicon.MANA_SPARKS, categoryMana);
		sparks.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("3"),
				new PageCraftingRecipe("2", ModItems.spark));

		sparkUpgrades = new AlfheimLexiconEntry(LibLexicon.MANA_SPARK_UPGRADES, categoryMana);
		sparkUpgrades.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
				new PageText("4"), new PageCraftingRecipe("5", ModItems.sparkUpgradeDispersive, ModItems.sparkUpgradeDominant, ModItems.sparkUpgradeRecessive, ModItems.sparkUpgradeIsolated));

		if(ConfigHandler.COMMON.fluxfieldEnabled.get()) {
			rfGenerator = new BasicLexiconEntry(LibLexicon.MANA_RF_GENERATOR, categoryMana);
			rfGenerator.setLexiconPages(new PageText("0"),
					new PageCraftingRecipe("1", ModBlocks.rfGenerator.asItem()));
		}

		lenses = new BasicLexiconEntry(LibLexicon.MANA_LENSES, categoryMana);
		lenses.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.lensNormal),
				new PageText("4"), new PageText("5"), new PageText("6"),
				new PageCraftingRecipe("7", ModItems.lensSpeed), new PageText("8"),
				new PageCraftingRecipe("9", ModItems.lensPower), new PageText("10"),
				new PageCraftingRecipe("11", ModItems.lensTime), new PageText("12"),
				new PageCraftingRecipe("13", ModItems.lensEfficiency), new PageText("38"),
				new PageCraftingRecipe("39", ModItems.lensMessenger), new PageText("14"),
				new PageCraftingRecipe("15", ModItems.lensBounce), new PageText("16"),
				new PageCraftingRecipe("17", ModItems.lensGravity), new PageText("18"),
				new PageCraftingRecipe("19", ModItems.lensMine), new PageText("20"),
				new PageCraftingRecipe("21", ModItems.lensDamage), new PageText("22"),
				new PageCraftingRecipe("23", ModItems.lensPhantom), new PageText("24"),
				new PageCraftingRecipe("25", ModItems.lensMagnet), new PageText("26"),
				new PageCraftingRecipe("27", ModItems.lensExplosive), new PageText("28"),
				new PageCraftingRecipe("29", ModItems.lensInfluence), new PageText("30"),
				new PageCraftingRecipe("31", ModItems.lensWeight), new PageText("32"),
				new PageCraftingRecipe("33", ModItems.lensFire), new PageText("34"),
				new PageCraftingRecipe("35", ModItems.lensPiston), new PageText("36"),
				new PageCraftingRecipe("37", ModItems.lensLight));

		distributor = new BasicLexiconEntry(LibLexicon.MANA_DISTRIBUTOR, categoryMana);
		distributor.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.distributor.asItem()));

		manaVoid = new BasicLexiconEntry(LibLexicon.MANA_VOID, categoryMana);
		manaVoid.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.manaVoid.asItem()));

		manaTablet = new BasicLexiconEntry(LibLexicon.MANA_TABLET, categoryMana);
		manaTablet.setPriority().setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.manaTablet));

		manaMirror = new BasicLexiconEntry(LibLexicon.MANA_MIRROR, categoryMana);
		manaMirror.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.manaMirror));

		manaDetector = new BasicLexiconEntry(LibLexicon.MANA_DETECTOR, categoryMana);
		manaDetector.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.manaDetector.asItem()));

		redstoneSpreader = new BasicLexiconEntry(LibLexicon.MANA_REDSTONE_SPREADER, categoryMana);
		redstoneSpreader.setPriority().setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.redstoneSpreader.asItem()));

		manastar = new BasicLexiconEntry(LibLexicon.MANA_MANASTAR, categoryMana);
		manastar.setPriority().setLexiconPages(new PageText("0"),
				new PagePetalRecipe("1", ModSubtiles.manastar.asItem()));

		dreamwoodSpreader = new AlfheimLexiconEntry(LibLexicon.MANA_DREAMWOOD_SPREADER, categoryMana);
		dreamwoodSpreader.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.elvenSpreader.asItem()), new PageText("2"),
				new PageCraftingRecipe("3", ModBlocks.gaiaSpreader.asItem()));

		elvenLenses = new AlfheimLexiconEntry(LibLexicon.MANA_ELVEN_LENSES, categoryMana);
		elvenLenses.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.lensPaint), new PageText("3"),
				new PageCraftingRecipe("4", ModItems.lensWarp), new PageText("5"),
				new PageCraftingRecipe("6", ModItems.lensRedirect), new PageText("7"),
				new PageCraftingRecipe("8", ModItems.lensFirework), new PageText("9"),
				new PageCraftingRecipe("10", ModItems.lensFlare), new PageText("11"),
				new PageCraftingRecipe("12", ModItems.lensTripwire));

		prism = new AlfheimLexiconEntry(LibLexicon.MANA_PRISM, categoryMana);
		prism.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModBlocks.prism.asItem()));

		poolCart = new BasicLexiconEntry(LibLexicon.MANA_POOL_CART, categoryMana);
		poolCart.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.poolMinecart),
				new PageCraftingRecipe("4", ModBlocks.pump.asItem()));

		sparkChanger = new AlfheimLexiconEntry(LibLexicon.MANA_SPARK_CHANGER, categoryMana);
		sparkChanger.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.sparkChanger.asItem()));

		bellows = new BasicLexiconEntry(LibLexicon.MANA_BELLOWS, categoryMana);
		bellows.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.bellows.asItem()));

		// FUNCTIONAL FLOWERS ENTRIES
		functionalIntro = new BasicLexiconEntry(LibLexicon.FFLOWER_INTRO, categoryFunctionalFlowers);
		functionalIntro
				.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageText("3"), new PageCraftingRecipe("4", ModItems.redstoneRoot))
				.setIcon(ItemStack.EMPTY);

		Item[] miniFlowers = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "mini_flowers")).getAllElements().toArray(new Item[0]);
		flowerShrinking = new BasicLexiconEntry(LibLexicon.FFLOWER_SHRINKING, categoryFunctionalFlowers);
		flowerShrinking.setPriority()
				.setLexiconPages(new PageText("0"), new PageManaInfusionRecipe("1", miniFlowers))
				.setIcon(new ItemStack(ModSubtiles.bellethornChibi));

		flowerSpeed = new BasicLexiconEntry(LibLexicon.FFLOWER_SPEED, categoryFunctionalFlowers);
		flowerSpeed.setPriority().setLexiconPages(new PageText("0"), new PageText("1"));
		flowerSpeed.setIcon(new ItemStack(Blocks.PODZOL));

		jadedAmaranthus = new BasicLexiconEntry(LibLexicon.FFLOWER_JADED_AMARANTHUS, categoryFunctionalFlowers);
		jadedAmaranthus.setLexiconPages(new PageText("0"),
				new PagePetalRecipe("1", ModSubtiles.jadedAmaranthus.asItem()), new PageText("2"));

		bellethorne = new BasicLexiconEntry(LibLexicon.FFLOWER_BELLETHORNE, categoryFunctionalFlowers);
		bellethorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.bellethorn.asItem()));

		dreadthorne = new BasicLexiconEntry(LibLexicon.FFLOWER_DREADTHORNE, categoryFunctionalFlowers);
		dreadthorne.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.dreadthorn.asItem()));

		heiseiDream = new AlfheimLexiconEntry(LibLexicon.FFLOWER_HEISEI_DREAM, categoryFunctionalFlowers);
		heiseiDream.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.heiseiDream.asItem()));

		tigerseye = new BasicLexiconEntry(LibLexicon.FFLOWER_TIGERSEYE, categoryFunctionalFlowers);
		tigerseye.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.tigerseye.asItem()));

		orechid = Botania.gardenOfGlassLoaded ? new BasicLexiconEntry(LibLexicon.FFLOWER_ORECHID, categoryFunctionalFlowers)
				: new AlfheimLexiconEntry(LibLexicon.FFLOWER_ORECHID, categoryFunctionalFlowers);
		orechid.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.orechid.asItem()));
		if(Botania.gardenOfGlassLoaded)
			orechid.setPriority();

		orechidIgnem = new AlfheimLexiconEntry(LibLexicon.FFLOWER_ORECHID_IGNEM, categoryFunctionalFlowers);
		orechidIgnem.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.orechidIgnem.asItem()));

		if(ConfigHandler.COMMON.fallenKanadeEnabled.get()) {
			fallenKanade = new BasicLexiconEntry(LibLexicon.FFLOWER_FALLEN_KANADE, categoryFunctionalFlowers);
			fallenKanade.setLexiconPages(new PageText(Botania.bloodMagicLoaded ? "0a" : "0"),
					new PagePetalRecipe("1", ModSubtiles.fallenKanade.asItem()));
		}

		exoflame = new BasicLexiconEntry(LibLexicon.FFLOWER_EXOFLAME, categoryFunctionalFlowers);
		exoflame.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.exoflame.asItem()));

		agricarnation = new BasicLexiconEntry(LibLexicon.FFLOWER_AGRICARNATION, categoryFunctionalFlowers);
		agricarnation.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.agricarnation.asItem()));

		hopperhock = new BasicLexiconEntry(LibLexicon.FFLOWER_HOPPERHOCK, categoryFunctionalFlowers);
		hopperhock.setLexiconPages(new PageText("0"), new PageText("1"),
				new PagePetalRecipe("2", ModSubtiles.hopperhock.asItem()));

		tangleberrie = new BasicLexiconEntry(LibLexicon.FFLOWER_TANGLEBERRIE, categoryFunctionalFlowers);
		tangleberrie.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.tangleberrie.asItem()));

		jiyuulia = new BasicLexiconEntry(LibLexicon.FFLOWER_JIYUULIA, categoryFunctionalFlowers);
		jiyuulia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.jiyuulia.asItem()));

		rannuncarpus = new BasicLexiconEntry(LibLexicon.FFLOWER_RANNUNCARPUS, categoryFunctionalFlowers);
		rannuncarpus.setLexiconPages(new PageText("0"), new PageText("1"),
				new PagePetalRecipe("2", ModSubtiles.rannuncarpus.asItem()));

		hyacidus = new BasicLexiconEntry(LibLexicon.FFLOWER_HYACIDUS, categoryFunctionalFlowers);
		hyacidus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.hyacidus.asItem()));

		pollidisiac = new BasicLexiconEntry(LibLexicon.FFLOWER_POLLIDISIAC, categoryFunctionalFlowers);
		pollidisiac.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.pollidisiac.asItem()));

		clayconia = new BasicLexiconEntry(LibLexicon.FFLOWER_CLAYCONIA, categoryFunctionalFlowers);
		clayconia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.clayconia.asItem()));

		loonium = new AlfheimLexiconEntry(LibLexicon.FFLOWER_LOONIUM, categoryFunctionalFlowers);
		loonium.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.loonium.asItem()));

		daffomill = new BasicLexiconEntry(LibLexicon.FFLOWER_DAFFOMILL, categoryFunctionalFlowers);
		daffomill.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.daffomill.asItem()));

		vinculotus = new BasicLexiconEntry(LibLexicon.FFLOWER_VINCULOTUS, categoryFunctionalFlowers);
		vinculotus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.vinculotus.asItem()));

		spectranthemum = new AlfheimLexiconEntry(LibLexicon.FFLOWER_SPECTRANTHEMUN, categoryFunctionalFlowers);
		spectranthemum.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PagePetalRecipe("3", ModSubtiles.spectranthemum.asItem()));

		medumone = new BasicLexiconEntry(LibLexicon.FFLOWER_MEDUMONE, categoryFunctionalFlowers);
		medumone.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.medumone.asItem()));

		marimorphosis = new BasicLexiconEntry(LibLexicon.FFLOWER_MARIMORPHOSIS, categoryFunctionalFlowers);
		marimorphosis.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_METAMORPHIC_STONES),
				new PagePetalRecipe("2", ModSubtiles.marimorphosis.asItem()),
				new PageCraftingRecipe("3", ModBlocks.forestAltar.asItem(), ModBlocks.plainsAltar.asItem(), ModBlocks.mountainAltar.asItem(), ModBlocks.fungalAltar.asItem(), 
						ModBlocks.swampAltar.asItem(), ModBlocks.desertAltar.asItem(), ModBlocks.taigaAltar.asItem(), ModBlocks.mesaAltar.asItem(), ModBlocks.mossyAltar.asItem()));
		
		bubbell = new AlfheimLexiconEntry(LibLexicon.FFLOWER_BUBBELL, categoryFunctionalFlowers);
		bubbell.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.bubbell.asItem()));

		solegnolia = new BasicLexiconEntry(LibLexicon.FFLOWER_SOLEGNOLIA, categoryFunctionalFlowers);
		solegnolia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.solegnolia.asItem()));

		bergamute = new BasicLexiconEntry(LibLexicon.FFLOWER_BERGAMUTE, categoryFunctionalFlowers);
		bergamute.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.bergamute.asItem()));

		// GENERATING FLOWERS ENTRIES
		generatingIntro = new BasicLexiconEntry(LibLexicon.GFLOWER_INTRO, categoryGenerationFlowers);
		generatingIntro.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"));

		endoflame = new BasicLexiconEntry(LibLexicon.GFLOWER_ENDOFLAME, categoryGenerationFlowers);
		endoflame.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("3"),
				new PagePetalRecipe("2", ModSubtiles.endoflame.asItem()));

		hydroangeas = new BasicLexiconEntry(LibLexicon.GFLOWER_HYDROANGEAS, categoryGenerationFlowers);
		hydroangeas.setLexiconPages(new PageText("0"), new PageImage("2", LibResources.ENTRY_HYDROANGEAS),
				new PagePetalRecipe("1", ModSubtiles.hydroangeas.asItem()));

		thermalily = new BasicLexiconEntry(LibLexicon.GFLOWER_THERMALILY, categoryGenerationFlowers);
		thermalily.setLexiconPages(new PageText("0"), new PageText("2"), new PageText("3"),
				new PagePetalRecipe("1", ModSubtiles.thermalily.asItem()));

		arcaneRose = new BasicLexiconEntry(LibLexicon.GFLOWER_ARCANE_ROSE, categoryGenerationFlowers);
		arcaneRose.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.rosaArcana.asItem()));

		munchdew = new BasicLexiconEntry(LibLexicon.GFLOWER_MUNCHDEW, categoryGenerationFlowers);
		munchdew.setLexiconPages(new PageText("0"), new PageText("1"),
				new PagePetalRecipe("2", ModSubtiles.munchdew.asItem()));

		entropinnyum = new BasicLexiconEntry(LibLexicon.GFLOWER_ENTROPINNYUM, categoryGenerationFlowers);
		entropinnyum.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.entropinnyum.asItem()));

		kekimurus = new AlfheimLexiconEntry(LibLexicon.GFLOWER_KEKIMURUS, categoryGenerationFlowers);
		kekimurus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.kekimurus.asItem()));

		gourmaryllis = new BasicLexiconEntry(LibLexicon.GFLOWER_GOURMARYLLIS, categoryGenerationFlowers);
		gourmaryllis.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PagePetalRecipe("3", ModSubtiles.gourmaryllis.asItem()));

		narslimmus = new BasicLexiconEntry(LibLexicon.GFLOWER_NARSLIMMUS, categoryGenerationFlowers);
		narslimmus.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.narslimmus.asItem()));

		spectrolus = new AlfheimLexiconEntry(LibLexicon.GFLOWER_SPECTROLUS, categoryGenerationFlowers);
		spectrolus.setLexiconPages(new PageText("0"), new PageText("1"),
				new PagePetalRecipe("2", ModSubtiles.spectrolus.asItem()));

		rafflowsia = new AlfheimLexiconEntry(LibLexicon.GFLOWER_RAFFLOWSIA, categoryGenerationFlowers);
		rafflowsia.setLexiconPages(new PageText("0"), new PagePetalRecipe("1", ModSubtiles.rafflowsia.asItem()));

		shulkMeNot = new AlfheimLexiconEntry(LibLexicon.GFLOWER_SHULK_ME_NOT, categoryGenerationFlowers);
		shulkMeNot.setLexiconPages(new PageText("0"), new PageText("1"), new PagePetalRecipe("2", ModSubtiles.shulkMeNot.asItem()));

		dandelifeon = new AlfheimLexiconEntry(LibLexicon.GFLOWER_DANDELIFEON, categoryGenerationFlowers);
		dandelifeon.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageText("3"), new PageText("4"), new PageText("5"), new PageText("6"), new PageText("10"),
				new PageText("7"), new PagePetalRecipe("8", ModSubtiles.dandelifeon.asItem()),
				new PageCraftingRecipe("9", ModBlocks.cellBlock.asItem()));

		// DEVICES ENTRIES
		pylon = new BasicLexiconEntry(LibLexicon.DEVICE_PYLON, categoryDevices);
		pylon.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.manaPylon.asItem()));

		if(ConfigHandler.COMMON.enchanterEnabled.get()) {
			manaEnchanting = new BasicLexiconEntry(LibLexicon.DEVICE_MANA_ENCHANTING, categoryDevices);
			manaEnchanting
					.setLexiconPages(new PageText("0"), new PageText("1"),
							new PageMultiblock("2", ModMultiblocks.enchanter), new PageText("5"), new PageText("6"),
							new PageText("7"), new PageText("8"), new PageText("9"))
					.setIcon(new ItemStack(ModBlocks.enchanter));
		}

		turntable = new BasicLexiconEntry(LibLexicon.DEVICE_TURNTABLE, categoryDevices);
		turntable.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.turntable.asItem()));

		Predicate<RecipeManaInfusion> filter = r -> r.getCatalyst() == ModBlocks.alchemyCatalyst.getDefaultState();

		alchemy = new BasicLexiconEntry(LibLexicon.DEVICE_ALCHEMY, categoryDevices);
		alchemy.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.alchemyCatalyst.asItem()),
				new PageManaInfusionRecipe("2", filter, Items.LEATHER),
				new PageManaInfusionRecipe("3", filter, ItemTags.LOGS.getAllElements().toArray(new Item[0])),
				new PageManaInfusionRecipe("4", filter, ItemTags.SAPLINGS.getAllElements().toArray(new Item[0])),
				new PageManaInfusionRecipe("5", filter, Items.GLOWSTONE_DUST),
				new PageManaInfusionRecipe("6", filter, Items.QUARTZ, ModItems.darkQuartz, ModItems.manaQuartz,
						ModItems.blazeQuartz, ModItems.lavenderQuartz, ModItems.redQuartz, ModItems.elfQuartz).setSkipRegistry(),
				new PageManaInfusionRecipe("7", filter, Items.CHISELED_STONE_BRICKS),
				new PageManaInfusionRecipe("8", filter, Items.ICE),
				new PageManaInfusionRecipe("9", filter, Items.LILY_PAD, Items.VINE),
				new PageManaInfusionRecipe("10", filter, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH),
				new PageManaInfusionRecipe("11", filter, Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.COCOA_BEANS),
				new PageManaInfusionRecipe("12", filter, Items.POTATO),
				new PageManaInfusionRecipe("13", filter, Items.NETHER_WART),
				new PageManaInfusionRecipe("14", filter, Items.GUNPOWDER, Items.GRAVEL),
				new PageManaInfusionRecipe("15", filter, Items.NAME_TAG),
				new PageManaInfusionRecipe("16", filter, Items.STRING),
				new PageManaInfusionRecipe("17", filter, Items.SLIME_BALL, Items.CACTUS),
				new PageManaInfusionRecipe("18", filter, Items.ENDER_PEARL),
				new PageManaInfusionRecipe("19", filter, Items.REDSTONE, Items.GLOWSTONE_DUST),
				new PageManaInfusionRecipe("20", filter, Items.SAND),
				new PageManaInfusionRecipe("21", filter, Items.RED_SAND),
				new PageManaInfusionRecipe("26", filter, Items.ANDESITE, Items.DIORITE, Items.GRANITE),
				new PageManaInfusionRecipe("22", filter, Items.CLAY_BALL, Items.BRICK),
				new PageManaInfusionRecipe("27", filter, Items.CHORUS_FLOWER),
				new PageManaInfusionRecipe("24", filter, Items.DEAD_BUSH, Items.GRASS, Items.FERN),
				new PageManaInfusionRecipe("25", filter, Items.POPPY, Items.BLUE_ORCHID, Items.ALLIUM, Items.AZURE_BLUET, Items.RED_TULIP,
						Items.ORANGE_TULIP, Items.WHITE_TULIP, Items.PINK_TULIP, Items.OXEYE_DAISY, Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.DANDELION),
				new PageManaInfusionRecipe("23", filter, Items.COARSE_DIRT));

		openCrate = new BasicLexiconEntry(LibLexicon.DEVICE_OPEN_CRATE, categoryDevices);
		openCrate.setPriority().setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.openCrate.asItem()));

		forestEye = new BasicLexiconEntry(LibLexicon.DEVICE_FOREST_EYE, categoryDevices);
		forestEye.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.forestEye.asItem()));

		forestDrum = new BasicLexiconEntry(LibLexicon.DEVICE_FOREST_DRUM, categoryDevices);
		forestDrum.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.wildDrum.asItem()));

		platform = new BasicLexiconEntry(LibLexicon.DEVICE_PLATFORM, categoryDevices);
		platform.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModBlocks.abstrusePlatform.asItem()));

		Predicate<RecipeManaInfusion> conjurationFilter = recipe -> recipe.getCatalyst() == RecipeManaInfusion.conjuration.getDefaultState();

		conjurationCatalyst = new AlfheimLexiconEntry(LibLexicon.DEVICE_MANA_CONJURATION, categoryDevices);
		conjurationCatalyst.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.conjurationCatalyst.asItem()),
				new PageManaInfusionRecipe("2", conjurationFilter, Items.REDSTONE),
				new PageManaInfusionRecipe("3", conjurationFilter, Items.GLOWSTONE_DUST),
				new PageManaInfusionRecipe("4", conjurationFilter, Items.QUARTZ),
				new PageManaInfusionRecipe("5", conjurationFilter, Items.COAL),
				new PageManaInfusionRecipe("6", conjurationFilter, Items.SNOWBALL),
				new PageManaInfusionRecipe("7", conjurationFilter, Items.NETHERRACK),
				new PageManaInfusionRecipe("8", conjurationFilter, Items.SOUL_SAND),
				new PageManaInfusionRecipe("9", conjurationFilter, Items.GRAVEL),
				new PageManaInfusionRecipe("10", conjurationFilter, ItemTags.LEAVES.getAllElements().toArray(new Item[0])),
				new PageManaInfusionRecipe("11", conjurationFilter, Items.GRASS)); //todos

		spectralPlatform = new AlfheimLexiconEntry(LibLexicon.DEVICE_SPECTRAL_PLATFORM, categoryDevices);
		spectralPlatform.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.spectralPlatform.asItem()));

		gatherDrum = new AlfheimLexiconEntry(LibLexicon.DEVICE_GATHER_DRUM, categoryDevices);
		gatherDrum.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.gatheringDrum.asItem()));

		craftCrate = new AlfheimLexiconEntry(LibLexicon.DEVICE_CRAFT_CRATE, categoryDevices);
		craftCrate
				.setLexiconPages(new PageText("0"), new PageText("1"),
						new PageCraftingRecipe("2", ModItems.placeholder), new PageText("3"),
						new PageText("4"), new PageText("7"), new PageImage("5", LibResources.ENTRY_CRAFT_CRATE),
						new PageCraftingRecipe("6", ModBlocks.craftCrate.asItem()), new PageText("8"),
						new PageCraftingRecipe("9", ModItems.craftPattern1_1, ModItems.craftPattern2_2, ModItems.craftPattern1_2, 
								ModItems.craftPattern2_1, ModItems.craftPattern1_3, ModItems.craftPattern3_1, 
								ModItems.craftPattern2_3, ModItems.craftPattern3_2, ModItems.craftPatternDonut))
				.setIcon(new ItemStack(ModBlocks.craftCrate));

		brewery = new BasicLexiconEntry(LibLexicon.DEVICE_BREWERY, categoryDevices);
		brewery.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.brewery.asItem()),
				new PageCraftingRecipe("3", ModItems.vial), new PageText("4"),
				new PageBrew(ModBrews.speed, "5a", "5b"),
				new PageBrew(ModBrews.strength, "6a", "6b"),
				new PageBrew(ModBrews.haste, "7a", "7b"),
				new PageBrew(ModBrews.healing, "8a", "8b"),
				new PageBrew(ModBrews.jumpBoost, "9a", "9b"),
				new PageBrew(ModBrews.regen, "10a", "10b"),
				new PageBrew(ModBrews.regenWeak, "17a", "17b"),
				new PageBrew(ModBrews.resistance, "11a", "11b"),
				new PageBrew(ModBrews.fireResistance, "12a", "12b"),
				new PageBrew(ModBrews.waterBreathing, "13a", "13b"),
				new PageBrew(ModBrews.invisibility, "14a", "14b"),
				new PageBrew(ModBrews.nightVision, "15a", "15b"),
				new PageBrew(ModBrews.absorption, "16a", "16b"));

		flasks = new AlfheimLexiconEntry(LibLexicon.DEVICE_FLASKS, categoryDevices);
		flasks.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.flask));

		complexBrews = new BasicLexiconEntry(LibLexicon.DEVICE_COMPLEX_BREWS, categoryDevices);
		complexBrews.setLexiconPages(new PageText("0"), new PageBrew(ModBrews.overload, "1a", "1b"),
				new PageBrew(ModBrews.soulCross, "2a", "2b"),
				new PageBrew(ModBrews.featherfeet, "3a", "3b"),
				new PageBrew(ModBrews.emptiness, "4a", "4b"),
				new PageBrew(ModBrews.bloodthirst, "5a", "5b"),
				new PageBrew(ModBrews.allure, "6a", "6b"), new PageBrew(ModBrews.clear, "7a", "7b"))
				.setIcon(((IBrewContainer) ModItems.vial).getItemForBrew(ModBrews.jumpBoost,
						new ItemStack(ModItems.vial)));

		incense = new BasicLexiconEntry(LibLexicon.DEVICE_INCENSE, categoryDevices);
		incense.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("5"),
				new PageCraftingRecipe("3", ModItems.incenseStick),
				new PageCraftingRecipe("4", ModBlocks.incensePlate.asItem()));

		hourglass = new BasicLexiconEntry(LibLexicon.DEVICE_HOURGLASS, categoryDevices);
		hourglass.setPriority().setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageText("3"), new PageText("4"), new PageText("6"), new PageCraftingRecipe("5", ModBlocks.hourglass.asItem()));

		ghostRail = new AlfheimLexiconEntry(LibLexicon.DEVICE_GHOST_RAIL, categoryDevices);
		ghostRail.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.ghostRail.asItem()));

		canopyDrum = new BasicLexiconEntry(LibLexicon.DEVICE_CANOPY_DRUM, categoryDevices);
		canopyDrum.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.canopyDrum.asItem()));

		cocoon = Botania.gardenOfGlassLoaded ? new BasicLexiconEntry(LibLexicon.DEVICE_COCOON, categoryDevices)
				: new AlfheimLexiconEntry(LibLexicon.DEVICE_COCOON, categoryDevices);
		cocoon.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.cocoon.asItem()));

		manaBomb = new AlfheimLexiconEntry(LibLexicon.DEVICE_MANA_BOMB, categoryDevices);
		manaBomb.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.manaBomb.asItem()));

		teruTeruBozu = new BasicLexiconEntry(LibLexicon.DEVICE_TERU_TERU_BOZU, categoryDevices);
		teruTeruBozu.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.teruTeruBozu.asItem()));

		avatar = new BasicLexiconEntry(LibLexicon.DEVICE_AVATAR, categoryDevices);
		avatar.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.avatar.asItem()));

		felPumpkin = new BasicLexiconEntry(LibLexicon.DEVICE_FEL_PUMPKIN, categoryDevices);
		felPumpkin.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.felPumpkin.asItem()));

		animatedTorch = new BasicLexiconEntry(LibLexicon.DEVICE_ANIMATED_TORCH, categoryDevices);
		animatedTorch.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageCraftingRecipe("3", ModBlocks.animatedTorch.asItem()));

		// TOOLS ENTRIES
		manaBlaster = new BasicLexiconEntry(LibLexicon.TOOL_MANA_BLASTER, categoryTools);
		manaBlaster.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.manaGun));

		grassSeeds = new BasicLexiconEntry(LibLexicon.TOOL_GRASS_SEEDS, categoryTools);
		grassSeeds.setLexiconPages(new PageText("0"),
				new PageManaInfusionRecipe("1", ModItems.grassSeeds),
				new PageManaInfusionRecipe("2", ModItems.podzolSeeds),
				new PageManaInfusionRecipe("3", ModItems.mycelSeeds), new PageText("4"),
				new PageCraftingRecipe("5", ModItems.drySeeds, ModItems.goldenSeeds, ModItems.vividSeeds, ModItems.scorchedSeeds, ModItems.infusedSeeds, ModItems.mutatedSeeds));

		dirtRod = new BasicLexiconEntry(LibLexicon.TOOL_DIRT_ROD, categoryTools);
		dirtRod.setLexiconPages(new PageText("0"), new PageText("2"), new PageCraftingRecipe("1", ModItems.dirtRod));

		terraformRod = new BasicLexiconEntry(LibLexicon.TOOL_TERRAFORM_ROD, categoryTools);
		terraformRod.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.terraformRod));

		manasteelGear = new BasicLexiconEntry(LibLexicon.TOOL_MANASTEEL_GEAR, categoryTools);
		manasteelGear.setPriority().setLexiconPages(new PageText("0"), new PageText("10"),
				new PageCraftingRecipe("1", ModItems.manasteelPick),
				new PageCraftingRecipe("2", ModItems.manasteelShovel),
				new PageCraftingRecipe("3", ModItems.manasteelAxe),
				new PageCraftingRecipe("4", ModItems.manasteelShears),
				new PageCraftingRecipe("5", ModItems.manasteelSword),
				new PageCraftingRecipe("6", ModItems.manasteelHelm),
				new PageCraftingRecipe("7", ModItems.manasteelChest),
				new PageCraftingRecipe("8", ModItems.manasteelLegs),
				new PageCraftingRecipe("9", ModItems.manasteelBoots));

		terrasteelArmor = new BasicLexiconEntry(LibLexicon.TOOL_TERRASTEEL_ARMOR, categoryTools);
		terrasteelArmor.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.terrasteelHelm),
				new PageCraftingRecipe("2", ModItems.terrasteelChest),
				new PageCraftingRecipe("3", ModItems.terrasteelLegs),
				new PageCraftingRecipe("4", ModItems.terrasteelBoots));

		grassHorn = new BasicLexiconEntry(LibLexicon.TOOL_GRASS_HORN, categoryTools);
		grassHorn.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.grassHorn),
				new PageCraftingRecipe("2", ModItems.leavesHorn),
				new PageCraftingRecipe("3", ModItems.snowHorn));

		terraBlade = new BasicLexiconEntry(LibLexicon.TOOL_TERRA_SWORD, categoryTools);
		terraBlade.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.terraSword));

		terraPick = new BasicLexiconEntry(LibLexicon.TOOL_TERRA_PICK, categoryTools);
		terraPick.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
				new PageText("4"), new PageCraftingRecipe("5", ModItems.terraPick));

		waterRod = new BasicLexiconEntry(LibLexicon.TOOL_WATER_ROD, categoryTools);
		waterRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.waterRod));

		elfGear = new AlfheimLexiconEntry(LibLexicon.TOOL_ELF_GEAR, categoryTools);
		elfGear.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.elementiumPick), new PageText("4"),
				new PageCraftingRecipe("5", ModItems.elementiumShovel), new PageText("6"),
				new PageCraftingRecipe("7", ModItems.elementiumAxe), new PageText("8"),
				new PageCraftingRecipe("9", ModItems.elementiumShears), new PageText("10"),
				new PageCraftingRecipe("11", ModItems.elementiumSword),
				new PageCraftingRecipe("12", ModItems.elementiumHelm),
				new PageCraftingRecipe("13", ModItems.elementiumChest),
				new PageCraftingRecipe("14", ModItems.elementiumLegs),
				new PageCraftingRecipe("15", ModItems.elementiumBoots));

		openBucket = new AlfheimLexiconEntry(LibLexicon.TOOL_OPEN_BUCKET, categoryTools);
		openBucket.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.openBucket));

		rainbowRod = new AlfheimLexiconEntry(LibLexicon.TOOL_RAINBOW_ROD, categoryTools);
		rainbowRod.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("6"),
				new PageCraftingRecipe("2", ModItems.rainbowRod),
				new PageCraftingRecipe("3", ModBlocks.bifrostPerm.asItem()),
				new PageCraftingRecipe("4", ModBlocks.shimmerrock.asItem()),
				new PageCraftingRecipe("5", ModBlocks.shimmerwoodPlanks.asItem()),
				new PageCraftingRecipe("7", ModBlocks.fabulousPool.asItem()));

		tornadoRod = new BasicLexiconEntry(LibLexicon.TOOL_TORNADO_ROD, categoryTools);
		tornadoRod.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.tornadoRod));

		fireRod = new BasicLexiconEntry(LibLexicon.TOOL_FIRE_ROD, categoryTools);
		fireRod.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.fireRod));

		vineBall = new BasicLexiconEntry(LibLexicon.TOOL_VINE_BALL, categoryTools);
		vineBall.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.vineBall),
				new PageCraftingRecipe("3", ModItems.slingshot));

		laputaShard = new AlfheimLexiconEntry(LibLexicon.TOOL_LAPUTA_SHARD, categoryTools);
		laputaShard.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", inGroup("laputashard_upgrade").negate(), ModItems.laputaShard),
				new PageCraftingRecipe("3", inGroup("laputashard_upgrade"), ModItems.laputaShard));

		virus = new AlfheimLexiconEntry(LibLexicon.TOOL_VIRUS, categoryTools);
		virus.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.necroVirus),
				new PageCraftingRecipe("2", ModItems.nullVirus));

		skyDirtRod = new AlfheimLexiconEntry(LibLexicon.TOOL_SKY_DIRT_ROD, categoryTools);
		skyDirtRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.skyDirtRod));

		glassPick = new BasicLexiconEntry(LibLexicon.TOOL_GLASS_PICK, categoryTools);
		glassPick.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.glassPick));

		diviningRod = new BasicLexiconEntry(LibLexicon.TOOL_DIVINING_ROD, categoryTools);
		diviningRod.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.diviningRod));

		gravityRod = new AlfheimLexiconEntry(LibLexicon.TOOL_GRAVITY_ROD, categoryTools);
		gravityRod.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.gravityRod));

		missileRod = new AlfheimLexiconEntry(LibLexicon.TOOL_MISSILE_ROD, categoryTools);
		missileRod.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.missileRod));

		craftingHalo = new BasicLexiconEntry(LibLexicon.TOOL_CRAFTING_HALO, categoryTools);
		craftingHalo.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.craftingHalo));

		clip = new AlfheimLexiconEntry(LibLexicon.TOOL_CLIP, categoryTools);
		clip.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.clip));

		cobbleRod = new BasicLexiconEntry(LibLexicon.TOOL_COBBLE_ROD, categoryTools);
		cobbleRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.cobbleRod));

		smeltRod = new BasicLexiconEntry(LibLexicon.TOOL_SMELT_ROD, categoryTools);
		smeltRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.smeltRod));

		worldSeed = new AlfheimLexiconEntry(LibLexicon.TOOL_WORLD_SEED, categoryTools);
		worldSeed.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.worldSeed));

		spellCloth = new BasicLexiconEntry(LibLexicon.TOOL_SPELL_CLOTH, categoryTools);
		spellCloth.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.spellCloth));

		thornChakram = new BasicLexiconEntry(LibLexicon.TOOL_THORN_CHAKRAM, categoryTools);
		thornChakram.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.thornChakram));

		fireChakram = new AlfheimLexiconEntry(LibLexicon.TOOL_FIRE_CHAKRAM, categoryTools);
		fireChakram.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.flareChakram));

		overgrowthSeed = new BasicLexiconEntry(LibLexicon.TOOL_OVERGROWTH_SEED, categoryTools);
		overgrowthSeed.setPriority().setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(ModItems.overgrowthSeed));
		overgrowthSeed.addExtraDisplayedRecipe(new ItemStack(ModItems.overgrowthSeed));
		overgrowthSeed.addExtraDisplayedRecipe(new ItemStack(ModBlocks.enchantedSoil));

		livingwoodBow = new BasicLexiconEntry(LibLexicon.TOOL_LIVINGWOOD_BOW, categoryTools);
		livingwoodBow.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.livingwoodBow));

		crystalBow = new AlfheimLexiconEntry(LibLexicon.TOOL_CRYSTAL_BOW, categoryTools);
		crystalBow.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.crystalBow));

		temperanceStone = new BasicLexiconEntry(LibLexicon.TOOL_TEMPERANCE_STONE, categoryTools);
		temperanceStone.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.temperanceStone));

		terraAxe = new BasicLexiconEntry(LibLexicon.TOOL_TERRA_AXE, categoryTools);
		terraAxe.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.terraAxe));

		obedienceStick = new BasicLexiconEntry(LibLexicon.TOOL_OBEDIENCE_STICK, categoryTools);
		obedienceStick.setPriority().setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.obedienceStick));

		slimeBottle = new AlfheimLexiconEntry(LibLexicon.TOOL_SLIME_BOTTLE, categoryTools);
		slimeBottle.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.slimeBottle));

		exchangeRod = new BasicLexiconEntry(LibLexicon.TOOL_EXCHANGE_ROD, categoryTools);
		exchangeRod.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.exchangeRod));

		manaweave = new BasicLexiconEntry(LibLexicon.TOOL_MANAWEAVE, categoryTools);
		manaweave.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.manaweaveCloth),
				new PageCraftingRecipe("3", ModItems.manaweaveHelm),
				new PageCraftingRecipe("4", ModItems.manaweaveChest),
				new PageCraftingRecipe("5", ModItems.manaweaveLegs),
				new PageCraftingRecipe("6", ModItems.manaweaveBoots));

		autocraftingHalo = new BasicLexiconEntry(LibLexicon.TOOL_AUTOCRAFTING_HALO, categoryTools);
		autocraftingHalo.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.autocraftingHalo));

		sextant = new BasicLexiconEntry(LibLexicon.TOOL_SEXTANT, categoryTools);
		sextant.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.sextant));

		astrolabe = new AlfheimLexiconEntry(LibLexicon.TOOL_ASTROLABE, categoryTools);
		astrolabe.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.astrolabe));

		// ENDER ENTRIES
		enderAir = new BasicLexiconEntry(LibLexicon.ENDER_AIR, categoryEnder);
		enderAir.setPriority().setLexiconPages(new PageText("0"));
		enderAir.addExtraDisplayedRecipe(new ItemStack(ModItems.enderAirBottle));
		LexiconRecipeMappings.map(new ItemStack(ModItems.enderAirBottle), enderAir, 0);

		enderEyeBlock = new BasicLexiconEntry(LibLexicon.ENDER_ENDER_EYE_BLOCK, categoryEnder);
		enderEyeBlock.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.enderEye.asItem()));

		pistonRelay = new BasicLexiconEntry(LibLexicon.ENDER_PISTON_RELAY, categoryEnder);
		pistonRelay.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageManaInfusionRecipe("2", ModBlocks.pistonRelay.asItem()));

		enderHand = new BasicLexiconEntry(LibLexicon.ENDER_ENDER_HAND, categoryEnder);
		enderHand.setLexiconPages(new PageText(ConfigHandler.COMMON.enderPickpocketEnabled.get() ? "0" : "0a"), new PageText("2"),
				new PageCraftingRecipe(ConfigHandler.COMMON.enderPickpocketEnabled.get() ? "1" : "1a", ModItems.enderHand));

		enderDagger = new BasicLexiconEntry(LibLexicon.ENDER_ENDER_DAGGER, categoryEnder);
		enderDagger.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.enderDagger));

		spawnerClaw = new AlfheimLexiconEntry(LibLexicon.ENDER_SPAWNER_CLAW, categoryEnder);
		spawnerClaw.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.spawnerClaw.asItem()));

		redString = new AlfheimLexiconEntry(LibLexicon.ENDER_RED_STRING, categoryEnder);
		redString.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.redString), new PageText("3"),
				new PageCraftingRecipe("4", ModBlocks.redStringContainer.asItem()), new PageText("5"),
				new PageCraftingRecipe("6", ModBlocks.redStringDispenser.asItem()), new PageText("7"),
				new PageCraftingRecipe("8", ModBlocks.redStringFertilizer.asItem()), new PageText("9"),
				new PageCraftingRecipe("10", ModBlocks.redStringComparator.asItem()), new PageText("11"),
				new PageCraftingRecipe("12", ModBlocks.redStringRelay.asItem()), new PageText("13"),
				new PageCraftingRecipe("14", ModBlocks.redStringInterceptor.asItem()));

		flightTiara = new AlfheimLexiconEntry(LibLexicon.ENDER_FLIGHT_TIARA, categoryEnder);
		flightTiara.setLexiconPages(new PageText("0"), new PageText("4"), new PageText("5"), new PageText("6"),
				new PageCraftingRecipe("1", inGroup("flighttiara_wings").negate(), ModItems.flightTiara), new PageText("2"),
				new PageCraftingRecipe("3", inGroup("flighttiara_wings"), ModItems.flightTiara));

		corporea = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA, categoryEnder);
		corporea.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
				new PageText("4"), new PageText("5"), new PageText("6"),
				new PageCraftingRecipe("7", ModItems.corporeaSpark),
				new PageCraftingRecipe("8", ModItems.corporeaSparkMaster));

		corporeaIndex = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA_INDEX, categoryEnder);
		corporeaIndex.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
				new PageText("4"), new PageText("5"), new PageText("8"), new PageText("6"),
				new PageCraftingRecipe("7", ModBlocks.corporeaIndex.asItem()));

		corporeaFunnel = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA_FUNNEL, categoryEnder);
		corporeaFunnel.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.corporeaFunnel.asItem()));

		corporeaInterceptor = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA_INTERCEPTOR, categoryEnder);
		corporeaInterceptor.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.corporeaInterceptor.asItem()));

		spawnerMover = new AlfheimLexiconEntry(LibLexicon.ENDER_SPAWNER_MOVER, categoryEnder);
		spawnerMover.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.spawnerMover));

		keepIvy = new AlfheimLexiconEntry(LibLexicon.ENDER_KEEP_IVY, categoryEnder);
		keepIvy.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.keepIvy));

		blackHoleTalisman = new AlfheimLexiconEntry(LibLexicon.ENDER_BLACK_HOLE_TALISMAN, categoryEnder);
		blackHoleTalisman.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModItems.blackHoleTalisman));

		corporeaCrystalCube = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA_CRYSTAL_CUBE, categoryEnder);
		corporeaCrystalCube.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("3"), new PageText("4"),
				new PageCraftingRecipe("2", ModBlocks.corporeaCrystalCube.asItem()));

		luminizerTransport = new AlfheimLexiconEntry(LibLexicon.ENDER_LUMINIZER_TRANSPORT, categoryEnder);
		luminizerTransport.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModBlocks.lightRelayDefault.asItem()), new PageText("3"),
				new PageCraftingRecipe("4", ModBlocks.lightRelayDetector.asItem()), new PageText("7"),
				new PageCraftingRecipe("8", ModBlocks.lightRelayFork.asItem()), new PageText("9"), new PageText("10"),
				new PageCraftingRecipe("11", ModBlocks.lightRelayToggle.asItem()), new PageText("5"),
				new PageCraftingRecipe("6", ModBlocks.lightLauncher.asItem()));

		starSword = new AlfheimLexiconEntry(LibLexicon.ENDER_STAR_SWORD, categoryEnder);
		starSword.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.starSword));

		thunderSword = new AlfheimLexiconEntry(LibLexicon.ENDER_THUNDER_SWORD, categoryEnder);
		thunderSword.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.thunderSword));

		corporeaRetainer = new AlfheimLexiconEntry(LibLexicon.ENDER_CORPOREA_RETAINER, categoryEnder);
		corporeaRetainer.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
				new PageCraftingRecipe("3", ModBlocks.corporeaRetainer.asItem()));

		// BAUBLES ENTRIES
		baublesIntro = new BasicLexiconEntry(LibLexicon.BAUBLE_INTRO, categoryBaubles);
		baublesIntro.setPriority().setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_BAUBLES),
				new PageText("2"));

		cosmeticBaubles = new BasicLexiconEntry(LibLexicon.BAUBLE_COSMETIC, categoryBaubles);
		{
			Item[] cosmetics = Arrays.stream(ItemBaubleCosmetic.Variant.values())
					.map(v -> LibItemNames.COSMETIC_PREFIX + v.name().toLowerCase(Locale.ROOT))
					.map(ResourceLocationHelper::prefix)
					.map(ForgeRegistries.ITEMS::getValue)
					.toArray(Item[]::new);
			List<LexiconPage> pages = new ArrayList<>();
			pages.add(new PageText("0"));
			pages.add(new PageText("1"));
			for(int i = 0; i < ItemBaubleCosmetic.SUBTYPES; i++)
				pages.add(new PageCraftingRecipe("" + (i + 2), cosmetics[i]));
			cosmeticBaubles.setPriority().setLexiconPages(pages.toArray(new LexiconPage[0]));
		}

		tinyPlanet = new BasicLexiconEntry(LibLexicon.BAUBLE_TINY_PLANET, categoryBaubles);
		tinyPlanet.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.tinyPlanet),
				new PageCraftingRecipe("2", ModBlocks.tinyPlanet.asItem()));

		manaRing = new BasicLexiconEntry(LibLexicon.BAUBLE_MANA_RING, categoryBaubles);
		manaRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.manaRing),
				new PageCraftingRecipe("2", ModItems.manaRingGreater));

		auraRing = new BasicLexiconEntry(LibLexicon.BAUBLE_AURA_RING, categoryBaubles);
		auraRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.auraRing),
				new PageCraftingRecipe("2", ModItems.auraRingGreater));

		travelBelt = new BasicLexiconEntry(LibLexicon.BAUBLE_TRAVEL_BELT, categoryBaubles);
		travelBelt.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.travelBelt));

		knockbacklBelt = new BasicLexiconEntry(LibLexicon.BAUBLE_KNOCKBACK_BELT, categoryBaubles);
		knockbacklBelt.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.knockbackBelt));

		icePendant = new BasicLexiconEntry(LibLexicon.BAUBLE_ICE_PENDANT, categoryBaubles);
		icePendant.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.icePendant));

		lavaPendant = new BasicLexiconEntry(LibLexicon.BAUBLE_LAVA_PENDANT, categoryBaubles);
		lavaPendant.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.lavaPendant));

		waterRing = new BasicLexiconEntry(LibLexicon.BAUBLE_WATER_RING, categoryBaubles);
		waterRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.waterRing));

		miningRing = new BasicLexiconEntry(LibLexicon.BAUBLE_MINING_RING, categoryBaubles);
		miningRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.miningRing));

		magnetRing = new BasicLexiconEntry(LibLexicon.BAUBLE_MAGNET_RING, categoryBaubles);
		magnetRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.magnetRing),
				new PageCraftingRecipe("2", ModItems.magnetRingGreater));

		divaCharm = new AlfheimLexiconEntry(LibLexicon.BAUBLE_DIVA_CHARM, categoryBaubles);
		divaCharm.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.divaCharm));

		pixieRing = new AlfheimLexiconEntry(LibLexicon.BAUBLE_PIXIE_RING, categoryBaubles);
		pixieRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.pixieRing));

		superTravelBelt = new AlfheimLexiconEntry(LibLexicon.BAUBLE_SUPER_TRAVEL_BELT, categoryBaubles);
		superTravelBelt.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.superTravelBelt));

		reachRing = new AlfheimLexiconEntry(LibLexicon.BAUBLE_REACH_RING, categoryBaubles);
		reachRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.reachRing));

		itemFinder = new BasicLexiconEntry(LibLexicon.BAUBLE_ITEM_FINDER, categoryBaubles);
		itemFinder.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.itemFinder));

		superLavaPendant = new AlfheimLexiconEntry(LibLexicon.BAUBLE_SUPER_LAVA_PENDANT, categoryBaubles);
		superLavaPendant.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.superLavaPendant));

		bloodPendant = new BasicLexiconEntry(LibLexicon.BAUBLE_BLOOD_PENDANT, categoryBaubles);
		bloodPendant.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.bloodPendant));

		judgementCloaks = new AlfheimLexiconEntry(LibLexicon.BAUBLE_JUDGEMENT_CLOAKS, categoryBaubles);
		judgementCloaks.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", ModItems.holyCloak),
				new PageCraftingRecipe("3", ModItems.unholyCloak),
				new PageCraftingRecipe("4", ModItems.balanceCloak));

		monocle = new BasicLexiconEntry(LibLexicon.BAUBLE_MONOCLE, categoryBaubles);
		monocle.setPriority().setLexiconPages(new PageText("0"), new PageText("2"),
				new PageCraftingRecipe("1", ModItems.monocle));

		swapRing = new BasicLexiconEntry(LibLexicon.BAUBLE_SWAP_RING, categoryBaubles);
		swapRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.swapRing));

		speedUpBelt = new BasicLexiconEntry(LibLexicon.BAUBLE_SPEED_UP_BELT, categoryBaubles);
		speedUpBelt.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.speedUpBelt));

		baubleBox = new BasicLexiconEntry(LibLexicon.BAUBLE_BOX, categoryBaubles);
		baubleBox.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.baubleBox))
				.setPriority();

		dodgeRing = new BasicLexiconEntry(LibLexicon.BAUBLE_DODGE_RING, categoryBaubles);
		dodgeRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.dodgeRing));

		invisibilityCloak = new BasicLexiconEntry(LibLexicon.BAUBLE_INVISIBILITY_CLOAK, categoryBaubles);
		invisibilityCloak.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.invisibilityCloak));

		cloudPendant = new BasicLexiconEntry(LibLexicon.BAUBLE_CLOUD_PENDANT, categoryBaubles);
		cloudPendant.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.cloudPendant));

		superCloudPendant = new AlfheimLexiconEntry(LibLexicon.BAUBLE_SUPER_CLOUD_PENDANT, categoryBaubles);
		superCloudPendant.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.superCloudPendant));

		thirdEye = new BasicLexiconEntry(LibLexicon.BAUBLE_THIRD_EYE, categoryBaubles);
		thirdEye.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.thirdEye));

		goddessCharm = new BasicLexiconEntry(LibLexicon.BAUBLE_GODDESS_CHARM, categoryBaubles);
		goddessCharm.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.goddessCharm));

		// ALFHOMANCY ENTRIES
		alfhomancyIntro = new BasicLexiconEntry(LibLexicon.ALF_INTRO, categoryAlfhomancy);
		alfhomancyIntro.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"),
						new PageCraftingRecipe("2", ModBlocks.alfPortal.asItem()),
						new PageCraftingRecipe("3", ModBlocks.naturaPylon.asItem()),
						new PageMultiblock("4", ModMultiblocks.alfPortal), new PageText("5"), new PageText("6"),
						new PageText("7"))
				.setIcon(new ItemStack(ModItems.lexicon));

		elvenMessage = new AlfheimLexiconEntry(LibLexicon.ALF_MESSAGE, categoryAlfhomancy);
		elvenMessage.setPriority()
				.setLexiconPages(new PageImage("0", LibResources.ENTRY_ELVEN_GARDE), new PageLoreText("1"),
						new PageLoreText("2"), new PageLoreText("3"), new PageLoreText("4"), new PageLoreText("5"),
						new PageLoreText("6"))
				.setIcon(new ItemStack(Items.WRITABLE_BOOK));

		elvenResources = new AlfheimLexiconEntry(LibLexicon.ALF_RESOURCES, categoryAlfhomancy);
		elvenResources.setPriority()
				.setLexiconPages(new PageText("0"), new PageElvenRecipe("1", ModBlocks.dreamwood.asItem()),
						new PageText("2"), new PageCraftingRecipe("10", ModItems.dreamwoodTwig),
						new PageElvenRecipe("3", ModItems.elementium, ModBlocks.elementiumBlock.asItem()),
						new PageElvenRecipe("4", ModItems.pixieDust),
						new PageElvenRecipe("5", ModItems.dragonstone, ModBlocks.dragonstoneBlock.asItem()), new PageText("6"),
						new PageElvenRecipe("7", ModItems.elfQuartz), new PageText("8"),
						new PageElvenRecipe("9", ModBlocks.elfGlass.asItem()))
				.setIcon(new ItemStack(ModItems.dragonstone));

		gaiaRitual = new AlfheimLexiconEntry(LibLexicon.ALF_GAIA_RITUAL, categoryAlfhomancy);
		gaiaRitual.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.gaiaPylon.asItem()),
				new PageMultiblock("2", ModMultiblocks.gaiaRitual), new PageText("3"), new PageText("4"),
				new PageText("5")).setIcon(new ItemStack(ModItems.lifeEssence));
		LexiconRecipeMappings.map(new ItemStack(ModItems.lifeEssence), gaiaRitual, 0);

		gaiaRitualHardmode = new AlfheimLexiconEntry(LibLexicon.ALF_GAIA_RITUAL_HARDMODE, categoryAlfhomancy);
		gaiaRitualHardmode
				.setLexiconPages(new PageText("0"), new PageText("1"),
						new PageCraftingRecipe("2", ModItems.gaiaIngot))
				.setIcon(new ItemStack(ModItems.gaiaIngot));

		elvenLore = new AlfheimLexiconEntry(LibLexicon.ALF_LORE, categoryAlfhomancy);
		elvenLore
				.setLexiconPages(new PageText("0"), new PageLoreText("1"), new PageLoreText("2"), new PageLoreText("3"),
						new PageLoreText("4"), new PageLoreText("5"), new PageLoreText("6"), new PageLoreText("7"))
				.setIcon(new ItemStack(Items.WRITABLE_BOOK));

		if(ConfigHandler.COMMON.relicsEnabled.get()) {
			relics = new AlfheimLexiconEntry(LibLexicon.ALF_RELICS, categoryAlfhomancy);
			relics.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.dice));

			relicInfo = new RelicLexiconEntry(LibLexicon.ALF_RELIC_INFO, categoryAlfhomancy, null);
			relicInfo.setLexiconPages(new PageText("0"), new PageText("1")).setIcon(new ItemStack(ModItems.dice));

			infiniteFruit = new RelicLexiconEntry(LibLexicon.ALF_INFINITE_FRUIT, categoryAlfhomancy,
					new ResourceLocation(LibMisc.MOD_ID, "challenge/infinite_fruit"));
			infiniteFruit.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.infiniteFruit));

			kingKey = new RelicLexiconEntry(LibLexicon.ALF_KING_KEY, categoryAlfhomancy, new ResourceLocation(LibMisc.MOD_ID, "challenge/king_key"));
			kingKey.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.kingKey));

			flugelEye = new RelicLexiconEntry(LibLexicon.ALF_FLUGEL_EYE, categoryAlfhomancy,
					new ResourceLocation(LibMisc.MOD_ID, "challenge/flugel_eye"));
			flugelEye.setLexiconPages(new PageText("0"), new PageText("1")).setIcon(new ItemStack(ModItems.flugelEye));

			thorRing = new RelicLexiconEntry(LibLexicon.ALF_THOR_RING, categoryAlfhomancy, new ResourceLocation(LibMisc.MOD_ID, "challenge/thor_ring"));
			thorRing.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.thorRing));

			lokiRing = new RelicLexiconEntry(LibLexicon.ALF_LOKI_RING, categoryAlfhomancy, new ResourceLocation(LibMisc.MOD_ID, "challenge/loki_ring"));
			lokiRing.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3")).setIcon(new ItemStack(ModItems.lokiRing));

			odinRing = new RelicLexiconEntry(LibLexicon.ALF_ODIN_RING, categoryAlfhomancy, new ResourceLocation(LibMisc.MOD_ID, "challenge/odin_ring"));
			odinRing.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.odinRing));
		}

		decorativeBlocks = new BasicLexiconEntry(LibLexicon.MISC_DECORATIVE_BLOCKS, categoryMisc);
		decorativeBlocks.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModBlocks.livingrockBrick.asItem()),
				new PageCraftingRecipe("2", ModBlocks.livingrockBrickMossy.asItem()),
				new PageCraftingRecipe("3", ModBlocks.livingrockBrickCracked.asItem()),
				new PageCraftingRecipe("4", ModBlocks.livingrockBrickChiseled.asItem()),
				new PageCraftingRecipe("5", ModBlocks.livingwoodPlanks.asItem()),
				new PageCraftingRecipe("6", ModBlocks.livingwoodPlanksMossy.asItem()),
				new PageCraftingRecipe("7", ModBlocks.livingwoodFramed.asItem()),
				new PageCraftingRecipe("8", ModBlocks.livingwoodPatternFramed.asItem()),
				new PageCraftingRecipe("9", ModBlocks.livingwoodGlimmering.asItem()), new PageText("10"),
				new PageCraftingRecipe("11", ModItems.darkQuartz),
				new PageManaInfusionRecipe("12", ModItems.manaQuartz),
				new PageCraftingRecipe("13", ModItems.blazeQuartz),
				new PageCraftingRecipe("14", ModItems.lavenderQuartz),
				new PageCraftingRecipe("15", ModItems.redQuartz),
				new PageCraftingRecipe("23", ModItems.sunnyQuartz), new PageText("16"));

		dispenserTweaks = new BasicLexiconEntry(LibLexicon.MISC_DISPENSER_TWEAKS, categoryMisc);
		dispenserTweaks.setLexiconPages(new PageText("0")).setPriority().setIcon(new ItemStack(Blocks.DISPENSER));

		shinyFlowers = new BasicLexiconEntry(LibLexicon.MISC_SHINY_FLOWERS, categoryMisc);
		shinyFlowers.setLexiconPages(new PageText("0"), new PageText("3"),
				new PageCraftingRecipe("1", shinyFlowerItems),
				new PageCraftingRecipe("2", floatingFlowerItems));

		tinyPotato = new BasicLexiconEntry(LibLexicon.MISC_TINY_POTATO, categoryMisc);
		tinyPotato.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageManaInfusionRecipe("1", ModBlocks.tinyPotato.asItem()));

		headCreating = new HeadLexiconEntry(LibLexicon.MISC_HEAD_CREATING, categoryMisc);
		headCreating.setLexiconPages(new PageText("0"), new PageText("2"),
				new PageRuneRecipe("1", Items.PLAYER_HEAD));

		Item[] azulejos = IntStream.range(0, 16).mapToObj(i -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(LibMisc.MOD_ID, "azulejo_" + i))).toArray(Item[]::new);
		azulejo = new BasicLexiconEntry(LibLexicon.MISC_AZULEJO, categoryMisc);
		azulejo.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_AZULEJOS),
				new PageCraftingRecipe("2", inGroup("azulejo_cycling").negate(), ModBlocks.azulejo0.asItem()),
				new PageCraftingRecipe("3", inGroup("azulejo_cycling"), azulejos));

		starfield = new AlfheimLexiconEntry(LibLexicon.MISC_STARFIELD, categoryMisc);
		starfield.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.starfield.asItem()));

		mushrooms = new BasicLexiconEntry(LibLexicon.MISC_MUSHROOMS, categoryMisc);
		mushrooms.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", mushroomItems));

		phantomInk = new BasicLexiconEntry(LibLexicon.MISC_PHANTOM_INK, categoryMisc);
		phantomInk.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModItems.phantomInk));

		blazeBlock = new BasicLexiconEntry(LibLexicon.MISC_BLAZE_BLOCK, categoryMisc);
		blazeBlock.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", ModBlocks.blazeBlock.asItem()));
		LexiconRecipeMappings.map(new ItemStack(Blocks.OBSIDIAN), blazeBlock, 0);

		challenges = new BasicLexiconEntry(LibLexicon.MISC_CHALLENGES, categoryMisc);
		challenges.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2")).setPriority()
				.setIcon(new ItemStack(ModItems.questgiverMark));

		cacophonium = new BasicLexiconEntry(LibLexicon.MISC_CACOPHONIUM, categoryMisc);
		cacophonium.setLexiconPages(new PageText("0"),
				new PageCraftingRecipe("1", ModItems.cacophonium), new PageText("2"));

		pavement = new BasicLexiconEntry(LibLexicon.MISC_PAVEMENT, categoryMisc);
		pavement.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", inGroup("pavement"), ModFluffBlocks.whitePavement.asItem(), ModFluffBlocks.blackPavement.asItem(), 
				ModFluffBlocks.bluePavement.asItem(), ModFluffBlocks.yellowPavement.asItem(), ModFluffBlocks.redPavement.asItem(), ModFluffBlocks.greenPavement.asItem()));

		preventingDecay = new DogLexiconEntry(LibLexicon.MISC_PRENTING_DECAY, categoryMisc);
		preventingDecay.setLexiconPages(new PageText("0")).setIcon(new ItemStack(ModItems.monocle));

		banners = new BasicLexiconEntry(LibLexicon.MISC_BANNERS, BotaniaAPI.categoryMisc);
		banners.setLexiconPages(new PageText("0"), new PageImage("1", LibResources.ENTRY_BANNERS))
				.setIcon(new ItemStack(ModItems.lexicon));

		if(Botania.bcApiLoaded) {
			bcIntegration = new CompatLexiconEntry(LibLexicon.MISC_BC_INTEGRATION, categoryMisc, "BuildCraft");
			bcIntegration.setLexiconPages(new PageText("0")).setIcon(new ItemStack(Items.REDSTONE));
		}

		if(true || Botania.thaumcraftLoaded) {
			tcIntegration = new CompatLexiconEntry(LibLexicon.MISC_TC_INTEGRATION, BotaniaAPI.categoryMisc, "Thaumcraft");

			if(true || ConfigHandler.COMMON.enableThaumcraftStablizers.get())
				tcIntegration.setLexiconPages(new PageText("0"), new PageText("1"),
						new PageCraftingRecipe("2", ModItems.manasteelHelmRevealing, ModItems.elementiumHelmRevealing, ModItems.terrasteelHelmRevealing), new PageText("3"),
						new PageManaInfusionRecipe("4", ModItems.manaInkwell), new PageText("5"),
						new PageBrew(ModBrews.warpWard, "6a", "6b"))
						.setIcon(new ItemStack(ModItems.manaInkwell));
			else
				tcIntegration.setLexiconPages(new PageText("0"), new PageText("1"),
						new PageCraftingRecipe("2", ModItems.manasteelHelmRevealing, ModItems.elementiumHelmRevealing, ModItems.terrasteelHelmRevealing), new PageText("3"),
						new PageManaInfusionRecipe("4", ModItems.manaInkwell),
						new PageBrew(ModBrews.warpWard, "6a", "6b"))
						.setIcon(new ItemStack(ModItems.manaInkwell));
		}
		Botania.LOGGER.info("Reloaded lexicon in {}", stopwatch.stop());

		Botania.LOGGER.info("Dumping lexicon entries");
		for (LexiconEntry e : BotaniaAPI.getAllEntries()) {
		    e.dump();
        }
	}

	private static Predicate<IRecipe<?>> GOG_RECIPE = recipe -> recipe.getId().getPath().contains("garden_of_glass");

	private static Predicate<IRecipe<?>> inGroup(String group) {
		String recipeGroup = LibMisc.MOD_ID + ":" + group;
		return recipe -> recipe.getGroup().equals(recipeGroup);
	}

	private static Predicate<IRecipe<?>> idEndsWith(String end) {
		return recipe -> recipe.getId().getPath().endsWith(end);
	}
}
