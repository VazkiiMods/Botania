/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 3:54:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemLens;
import vazkii.botania.common.item.ItemSignalFlare;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModCrafingRecipes {

	public static IRecipe recipeLexicon;
	public static List<IRecipe> recipesPetals;
	public static List<IRecipe> recipesDyes;
	public static IRecipe recipePestleAndMortar;
	public static List<IRecipe> recipesTwigWand;
	public static List<IRecipe> recipesApothecary;
	public static List<IRecipe> recipesSpreader;
	public static IRecipe recipeManaLens;
	public static List<IRecipe> recipesLensDying;
	public static IRecipe recipeRainbowLens;
	public static IRecipe recipePool;
	public static List<IRecipe> recipesRuneAltar;
	public static IRecipe recipeLensVelocity;
	public static IRecipe recipeLensPotency;
	public static IRecipe recipeLensResistance;
	public static IRecipe recipeLensEfficiency;
	public static IRecipe recipeLensBounce;
	public static IRecipe recipeLensGravity;
	public static IRecipe recipeLensBore;
	public static IRecipe recipeLensDamaging;
	public static IRecipe recipeLensPhantom;
	public static IRecipe recipeLensMagnet;
	public static IRecipe recipeLensExplosive;
	public static List<IRecipe> recipesUnstableBlocks;
	public static IRecipe recipePylon;
	public static IRecipe recipeDistributor;
	public static IRecipe recipeLivingrockDecor1;
	public static IRecipe recipeLivingrockDecor2;
	public static IRecipe recipeLivingrockDecor3;
	public static IRecipe recipeLivingrockDecor4;
	public static IRecipe recipeLivingwoodDecor1;
	public static IRecipe recipeLivingwoodDecor2;
	public static IRecipe recipeLivingwoodDecor3;
	public static IRecipe recipeLivingwoodDecor4;
	public static IRecipe recipeLivingwoodDecor5;
	public static List<IRecipe> recipesManaBeacons;
	public static List<IRecipe> recipesSignalFlares;
	public static IRecipe recipeManaVoid;
	public static List<IRecipe> recipesManaTablet;
	public static IRecipe recipeManaDetector;
	public static IRecipe recipeManaBlaster;
	public static IRecipe recipeTurntable;
	public static IRecipe recipeFertilizerPowder;
	public static IRecipe recipeFerilizerDye;
	public static IRecipe recipeLivingwoodTwig;
	public static IRecipe recipeDirtRod;
	public static IRecipe recipeTerraformRod;
	public static IRecipe recipeRedstoneSpreader;
	public static IRecipe recipeManaMirror;
	public static IRecipe recipeManasteelHelm;
	public static IRecipe recipeManasteelChest;
	public static IRecipe recipeManasteelLegs;
	public static IRecipe recipeManasteelBoots;
	public static IRecipe recipeManasteelPick;
	public static IRecipe recipeManasteelShovel;
	public static IRecipe recipeManasteelAxe;
	public static IRecipe recipeManasteelShears;
	public static IRecipe recipeManasteelSword;
	public static IRecipe recipeGrassHorn;
	public static IRecipe recipeTerrasteelHelm;
	public static IRecipe recipeTerrasteelChest;
	public static IRecipe recipeTerrasteelLegs;
	public static IRecipe recipeTerrasteelBoots;
	public static IRecipe recipeTerraSword;
	public static IRecipe recipeTinyPlanet;
	public static IRecipe recipeManaRing;
	public static IRecipe recipeAuraRing;
	public static IRecipe recipeGreaterManaRing;
	public static IRecipe recipeGreaterAuraRing;
	public static IRecipe recipeTravelBelt;
	public static IRecipe recipeKnocbackBelt;
	public static IRecipe recipeIcePendant;
	public static IRecipe recipeLavaPendant;
	public static IRecipe recipeGoldenLaurel;
	public static IRecipe recipeTinyPlanetBlock;
	public static IRecipe recipeAlchemyCatalyst;
	public static IRecipe recipeOpenCrate;
	public static IRecipe recipeForestEye;
	public static IRecipe recipeRedstoneRoot;
	public static IRecipe recipeForestDrum;
	public static IRecipe recipeWaterRing;
	public static IRecipe recipeMiningRing;
	public static IRecipe recipeMagnetRing;
	public static IRecipe recipeTerraPick;
	public static IRecipe recipeDivaCharm;
	public static IRecipe recipeFlightTiara;
	public static List<IRecipe> recipesShinyFlowers;
	public static IRecipe recipePlatform;
	public static IRecipe recipeEnderDagger;
	public static IRecipe recipeDarkQuartz;
	public static IRecipe recipeBlazeQuartz;
	public static List<IRecipe> recipesLavenderQuartz;
	public static IRecipe recipeRedQuartz;
	public static IRecipe recipeAlfPortal;
	public static IRecipe recipeNaturaPylon;
	public static IRecipe recipeWaterRod;
	public static IRecipe recipeElementiumHelm;
	public static IRecipe recipeElementiumChest;
	public static IRecipe recipeElementiumLegs;
	public static IRecipe recipeElementiumBoots;
	public static IRecipe recipeElementiumPick;
	public static IRecipe recipeElementiumShovel;
	public static IRecipe recipeElementiumAxe;
	public static IRecipe recipeElementiumShears;
	public static IRecipe recipeElementiumSword;
	public static IRecipe recipeOpenBucket;
	public static IRecipe recipeConjurationCatalyst;
	public static IRecipe recipeSpawnerMover;
	public static IRecipe recipePixieRing;
	public static IRecipe recipeSuperTravelBelt;
	public static IRecipe recipeRainbowRod;
	public static IRecipe recipeSpectralPlatform;
	public static List<IRecipe> recipesDreamwoodSpreader;
	public static IRecipe recipeTornadoRod;
	public static IRecipe recipeFireRod;
	public static IRecipe recipeVineBall;
	public static IRecipe recipeSlingshot;
	public static IRecipe recipePrismarine;
	public static IRecipe recipePrismarineBrick;
	public static IRecipe recipeDarkPrismarine;
	public static IRecipe recipeSeaLamp;
	public static IRecipe recipeLensInfluence;
	public static IRecipe recipeLensWeight;
	public static IRecipe recipeLensPaint;
	public static List<IRecipe> recipesMiniIsland;
	public static IRecipe recipeGaiaPylon;
	public static IRecipe recipeGatherDrum;
	public static IRecipe recipeLensFire;
	public static IRecipe recipeLensPiston;
	public static List<IRecipe> recipesLaputaShard;
	public static IRecipe recipeVirusZombie;
	public static IRecipe recipeVirusSkeleton;
	public static IRecipe recipeReachRing;
	public static IRecipe recipeSkyDirtRod;
	public static IRecipe recipeSpawnerClaw;
	public static IRecipe recipeCraftCrate;
	public static IRecipe recipePlaceholder;
	public static IRecipe recipeReedBlock;
	public static IRecipe recipeThatch;
	public static IRecipe recipeNetherBrick;
	public static IRecipe recipeSoulBrick;
	public static IRecipe recipeSnowBrick;
	public static IRecipe recipeRoofTile;
	public static IRecipe recipeAzulejo;
	public static List<IRecipe> recipesAzulejoCycling;
	public static IRecipe recipeEnderEyeBlock;

	public static void init() {
		// Lexicon Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.lexicon), "treeSapling", Items.book);
		recipeLexicon = BotaniaAPI.getLatestAddedRecipe();

		// Petal/Dye Recipes
		for(int i = 0; i < 16; i++)
			addShapelessOreDictRecipe(new ItemStack(ModItems.petal, 2, i), LibOreDict.FLOWER[i]);
		recipesPetals = BotaniaAPI.getLatestAddedRecipes(16);

		for(int i = 0; i < 16; i++)
			addShapelessOreDictRecipe(new ItemStack(ModItems.dye, 1, i), LibOreDict.PETAL[i], LibOreDict.PESTLE_AND_MORTAR);
		recipesDyes = BotaniaAPI.getLatestAddedRecipes(16);

		// Pestle and Mortar Recipe
		addOreDictRecipe(new ItemStack(ModItems.pestleAndMortar),
				" S", "W ", "B ",
				'S', "stickWood",
				'W', "plankWood",
				'B', Items.bowl);
		recipePestleAndMortar = BotaniaAPI.getLatestAddedRecipe();

		// Wand of the Forest Recipes
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 16; j++) {
				addOreDictRecipe(ItemTwigWand.forColors(i, j),
						" AS", " SB", "S  ",
						'A', LibOreDict.PETAL[i],
						'B', LibOreDict.PETAL[j],
						'S', LibOreDict.LIVINGWOOD_TWIG);
			}
		recipesTwigWand = BotaniaAPI.getLatestAddedRecipes(256);

		// Petal Apothecary Recipes
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.altar),
					"SPS", " C ", "CCC",
					'S', new ItemStack(Blocks.stone_slab, 1, 3),
					'P', LibOreDict.PETAL[i],
					'C', "cobblestone");
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(16);

		// Mana Spreader Recipes
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.spreader),
					"WWW", "GP ", "WWW",
					'W', LibOreDict.LIVING_WOOD,
					'P', LibOreDict.PETAL[i],
					'G', "ingotGold");
		recipesSpreader = BotaniaAPI.getLatestAddedRecipes(16);

		// Mana Lens Recipe
		addOreDictRecipe(new ItemStack(ModItems.lens),
				" S ", "SGS", " S ",
				'S', LibOreDict.MANA_STEEL,
				'G', new ItemStack(Blocks.glass_pane));
		recipeManaLens = BotaniaAPI.getLatestAddedRecipe();

		// Mana Lens Dying Recipes
		for(int j = 0; j < 16; j++)
			addShapelessOreDictRecipe(ItemLens.setLensColor(new ItemStack(ModItems.lens), j), new ItemStack(ModItems.lens), LibOreDict.DYE[j]);
		recipesLensDying = BotaniaAPI.getLatestAddedRecipes(16);

		for(int i = 1; i < ItemLens.SUBTYPES; i++)
			for(int j = 0; j < 16; j++)
				addShapelessOreDictRecipe(ItemLens.setLensColor(new ItemStack(ModItems.lens, 1, i), j), new ItemStack(ModItems.lens, 1, i), LibOreDict.DYE[j]);

		// Rainbow Lens Recipe
		addShapelessOreDictRecipe(ItemLens.setLensColor(new ItemStack(ModItems.lens), 16), new ItemStack(ModItems.lens), LibOreDict.MANA_PEARL);
		recipeRainbowLens = BotaniaAPI.getLatestAddedRecipe();
		for(int i = 1; i < ItemLens.SUBTYPES; i++)
			addShapelessOreDictRecipe(ItemLens.setLensColor(new ItemStack(ModItems.lens, 1, i), 16), new ItemStack(ModItems.lens, 1, i), LibOreDict.MANA_PEARL);

		// Mana Pool Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.pool),
				"R R", "RRR",
				'R', LibOreDict.LIVING_ROCK);
		recipePool = BotaniaAPI.getLatestAddedRecipe();

		// Runic Altar Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.runeAltar),
				"SSS", "SPS",
				'S', LibOreDict.LIVING_ROCK,
				'P', LibOreDict.MANA_PEARL);
		addOreDictRecipe(new ItemStack(ModBlocks.runeAltar),
				"SSS", "SDS",
				'S', LibOreDict.LIVING_ROCK,
				'D', LibOreDict.MANA_DIAMOND);
		recipesRuneAltar = BotaniaAPI.getLatestAddedRecipes(2);

		// Lens Recipes
		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 1), new ItemStack(ModItems.lens), LibOreDict.RUNE[3]);
		recipeLensVelocity = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 2), new ItemStack(ModItems.lens), LibOreDict.RUNE[1]);
		recipeLensPotency = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 3), new ItemStack(ModItems.lens), LibOreDict.RUNE[2]);
		recipeLensResistance = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 4), new ItemStack(ModItems.lens), LibOreDict.RUNE[0]);
		recipeLensEfficiency = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 5), new ItemStack(ModItems.lens), LibOreDict.RUNE[5]);
		recipeLensBounce = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 6), new ItemStack(ModItems.lens), LibOreDict.RUNE[7]);
		recipeLensGravity = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 7), new ItemStack(ModItems.lens), LibOreDict.RUNE[11]);
		recipeLensBore = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 8), new ItemStack(ModItems.lens), LibOreDict.RUNE[13]);
		recipeLensDamaging = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 9), new ItemStack(ModItems.lens), new ItemStack(ModBlocks.platform));
		recipeLensPhantom = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 10), new ItemStack(ModItems.lens), "ingotIron", "ingotGold");
		recipeLensMagnet = BotaniaAPI.getLatestAddedRecipe();

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 11), new ItemStack(ModItems.lens), LibOreDict.RUNE[14]);
		recipeLensExplosive = BotaniaAPI.getLatestAddedRecipe();

		// Unstable Block Recipes
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.unstableBlock, 2, i),
					"OPO", "PMP", "OPO",
					'O', new ItemStack(Blocks.obsidian),
					'P', LibOreDict.PETAL[i],
					'M', new ItemStack(Items.ender_pearl));
		recipesUnstableBlocks = BotaniaAPI.getLatestAddedRecipes(16);

		// Mana Pylon Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.pylon),
				" G ", "MDM", " G ",
				'G', "ingotGold",
				'M', LibOreDict.MANA_STEEL,
				'D', LibOreDict.MANA_DIAMOND);
		recipePylon = BotaniaAPI.getLatestAddedRecipe();

		// Mana Distributor
		addOreDictRecipe(new ItemStack(ModBlocks.distributor),
				"RRR", "S S", "RRR",
				'R', LibOreDict.LIVING_ROCK,
				'S', LibOreDict.MANA_STEEL);
		recipeDistributor = BotaniaAPI.getLatestAddedRecipe();

		// Livingrock Decorative Blocks
		addOreDictRecipe(new ItemStack(ModBlocks.livingrock, 4, 1),
				"RR", "RR",
				'R', LibOreDict.LIVING_ROCK);
		recipeLivingrockDecor1 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingrock, 1, 2), new ItemStack(ModBlocks.livingrock, 1, 1), new ItemStack(Items.wheat_seeds));
		recipeLivingrockDecor2 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingrock, 2, 3), new ItemStack(ModBlocks.livingrock, 1, 1), "cobblestone");
		recipeLivingrockDecor3 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingrock, 4, 4),
				"RR", "RR",
				'R', new ItemStack(ModBlocks.livingrock, 1, 1));
		recipeLivingrockDecor4 = BotaniaAPI.getLatestAddedRecipe();

		// Livingwood Decorative Blocks
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 1), LibOreDict.LIVING_WOOD);
		recipeLivingwoodDecor1 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 1, 2), new ItemStack(ModBlocks.livingwood, 1, 1), new ItemStack(Items.wheat_seeds));
		recipeLivingwoodDecor2 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 3),
				"WW", "WW",
				'W', new ItemStack(ModBlocks.livingwood, 1, 1));
		recipeLivingwoodDecor3 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 4),
				" W ", "W W", " W ",
				'W', new ItemStack(ModBlocks.livingwood, 1, 1));
		recipeLivingwoodDecor4 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 1, 5), LibOreDict.LIVING_WOOD, new ItemStack(Items.glowstone_dust));
		recipeLivingwoodDecor5 = BotaniaAPI.getLatestAddedRecipe();

		// Dreamwood Decorative Blocks
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.dreamwood, 4, 1), LibOreDict.DREAM_WOOD);
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.dreamwood, 1, 2), new ItemStack(ModBlocks.dreamwood, 1, 1), new ItemStack(Items.wheat_seeds));
		addOreDictRecipe(new ItemStack(ModBlocks.dreamwood, 4, 3),
				"WW", "WW",
				'W', new ItemStack(ModBlocks.dreamwood, 1, 1));
		addOreDictRecipe(new ItemStack(ModBlocks.dreamwood, 4, 4),
				" W ", "W W", " W ",
				'W', new ItemStack(ModBlocks.dreamwood, 1, 1));
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.dreamwood, 1, 5), LibOreDict.DREAM_WOOD, new ItemStack(Items.glowstone_dust));

		// Mana Beacon Recipe
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.manaBeacon, 1, i),
					" B ", "BPB", " B ",
					'B', new ItemStack(ModBlocks.unstableBlock, 1, i),
					'P', LibOreDict.MANA_PEARL);
		recipesManaBeacons = BotaniaAPI.getLatestAddedRecipes(16);

		// Signal Flare Recipe
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(ItemSignalFlare.forColor(i),
					"I ", " B", "W ",
					'B', new ItemStack(ModBlocks.manaBeacon, 1, i),
					'I', "ingotIron",
					'W', LibOreDict.LIVING_WOOD);
		recipesSignalFlares = BotaniaAPI.getLatestAddedRecipes(16);

		// Mana Void Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.manaVoid),
				"SSS", "O O", "SSS",
				'S', LibOreDict.LIVING_ROCK,
				'O', new ItemStack(Blocks.obsidian));
		recipeManaVoid = BotaniaAPI.getLatestAddedRecipe();

		// Mana Tablet Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaTablet, 1, 10000),
				"SSS", "SPS", "SSS",
				'S', LibOreDict.LIVING_ROCK,
				'P', LibOreDict.MANA_PEARL);
		addOreDictRecipe(new ItemStack(ModItems.manaTablet, 1, 10000),
				"SSS", "SDS", "SSS",
				'S', LibOreDict.LIVING_ROCK,
				'D', LibOreDict.MANA_DIAMOND);
		recipesManaTablet = BotaniaAPI.getLatestAddedRecipes(2);

		// Mana Detector Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.manaDetector),
				"RSR", "SCS", "RSR",
				'R', new ItemStack(Items.redstone),
				'C', new ItemStack(Items.comparator),
				'S', LibOreDict.LIVING_ROCK);
		recipeManaDetector = BotaniaAPI.getLatestAddedRecipe();

		// Mana Blaster Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaGun),
				"SMD", " WT", "  W",
				'S', new ItemStack(ModBlocks.spreader, 1, 1),
				'M', LibOreDict.RUNE[8],
				'D', LibOreDict.MANA_DIAMOND,
				'T', new ItemStack(Blocks.tnt),
				'W', LibOreDict.LIVING_WOOD);
		recipeManaBlaster = BotaniaAPI.getLatestAddedRecipe();

		// Spreader Turntable Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.turntable),
				"WWW", "WPW", "WWW",
				'W', LibOreDict.LIVING_WOOD,
				'P', Blocks.sticky_piston);
		recipeTurntable = BotaniaAPI.getLatestAddedRecipe();

		// Fertilizer Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fertilizer), new ItemStack(Items.dye, 1, 15), new ItemStack(ModItems.dye, 1, Short.MAX_VALUE), new ItemStack(ModItems.dye, 1, Short.MAX_VALUE), new ItemStack(ModItems.dye, 1, Short.MAX_VALUE), new ItemStack(ModItems.dye, 1, Short.MAX_VALUE));
		recipeFertilizerPowder = BotaniaAPI.getLatestAddedRecipe();
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fertilizer), new ItemStack(Items.dye, 1, 15), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 1));
		recipeFerilizerDye = BotaniaAPI.getLatestAddedRecipe();

		// Livingwood Twig Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaResource, 1, 3),
				"W", "W",
				'W', LibOreDict.LIVING_WOOD);
		recipeLivingwoodTwig = BotaniaAPI.getLatestAddedRecipe();

		// Rod of the Lands Recipe
		addOreDictRecipe(new ItemStack(ModItems.dirtRod),
				"  D", " T ", "E  ",
				'D', new ItemStack(Blocks.dirt),
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'E', LibOreDict.RUNE[2]);
		recipeDirtRod = BotaniaAPI.getLatestAddedRecipe();

		// Terra Firma Rod Recipe
		addOreDictRecipe(new ItemStack(ModItems.terraformRod),
				" WT", "ARS", "GM ",
				'T', LibOreDict.TERRA_STEEL,
				'R', new ItemStack(ModItems.dirtRod),
				'G', new ItemStack(ModItems.grassSeeds),
				'W', LibOreDict.RUNE[7],
				'S', LibOreDict.RUNE[4],
				'M', LibOreDict.RUNE[5],
				'A', LibOreDict.RUNE[6]);
		recipeTerraformRod = BotaniaAPI.getLatestAddedRecipe();

		// Redstone Mana Spreader Recipe
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.spreader, 1, 1),
				new ItemStack(ModBlocks.spreader), new ItemStack(Items.redstone));
		recipeRedstoneSpreader = BotaniaAPI.getLatestAddedRecipe();

		// Mana Miror Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaMirror),
				" PR", " SI", "T  ",
				'P', LibOreDict.MANA_PEARL,
				'R', LibOreDict.LIVING_ROCK,
				'S', LibOreDict.LIVINGWOOD_TWIG,
				'I', LibOreDict.TERRA_STEEL,
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE));
		recipeManaMirror = BotaniaAPI.getLatestAddedRecipe();

		// Mana Armor & Tools Recipes
		addOreDictRecipe(new ItemStack(ModItems.manasteelHelm),
				"SSS", "S S",
				'S', LibOreDict.MANA_STEEL);
		recipeManasteelHelm = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelChest),
				"S S", "SSS", "SSS",
				'S', LibOreDict.MANA_STEEL);
		recipeManasteelChest = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelLegs),
				"SSS", "S S", "S S",
				'S', LibOreDict.MANA_STEEL);
		recipeManasteelLegs = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelBoots),
				"S S", "S S",
				'S', LibOreDict.MANA_STEEL);
		recipeManasteelBoots = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelPick),
				"SSS", " T ", " T ",
				'S', LibOreDict.MANA_STEEL,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeManasteelPick = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelShovel),
				"S", "T", "T",
				'S', LibOreDict.MANA_STEEL,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeManasteelShovel = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelAxe),
				"SS", "TS", "T ",
				'S', LibOreDict.MANA_STEEL,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeManasteelAxe = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelSword),
				"S", "S", "T",
				'S', LibOreDict.MANA_STEEL,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeManasteelSword = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.manasteelShears),
				"S ", " S",
				'S', LibOreDict.MANA_STEEL);
		recipeManasteelShears = BotaniaAPI.getLatestAddedRecipe();

		// Horn of the Wild Recipe
		addOreDictRecipe(new ItemStack(ModItems.grassHorn),
				" W ", "WSW", "WW ",
				'W', LibOreDict.LIVING_WOOD,
				'S', new ItemStack(ModItems.grassSeeds));
		recipeGrassHorn = BotaniaAPI.getLatestAddedRecipe();

		// Terrasteel Armor Recipes
		addOreDictRecipe(new ItemStack(ModItems.terrasteelHelm),
				"SSS", "S S",
				'S', LibOreDict.TERRA_STEEL);
		recipeTerrasteelHelm = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.terrasteelChest),
				"S S", "SSS", "SSS",
				'S', LibOreDict.TERRA_STEEL);
		recipeTerrasteelChest = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.terrasteelLegs),
				"SSS", "S S", "S S",
				'S', LibOreDict.TERRA_STEEL);
		recipeTerrasteelLegs = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.terrasteelBoots),
				"S S", "S S",
				'S', LibOreDict.TERRA_STEEL);
		recipeTerrasteelBoots = BotaniaAPI.getLatestAddedRecipe();

		// Terra Blade Recipe
		addOreDictRecipe(new ItemStack(ModItems.terraSword),
				"I", "I", "S",
				'I', LibOreDict.TERRA_STEEL,
				'S', LibOreDict.LIVINGWOOD_TWIG);
		recipeTerraSword = BotaniaAPI.getLatestAddedRecipe();

		// Tiny Planet Recipe
		addOreDictRecipe(new ItemStack(ModItems.tinyPlanet),
				"LSL", "SPS", "LSL",
				'S', "stone",
				'L', LibOreDict.LIVING_ROCK,
				'P', LibOreDict.MANA_PEARL);
		recipeTinyPlanet = BotaniaAPI.getLatestAddedRecipe();

		// Mana Band Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaRing),
				"TI ", "I I", " I ",
				'T', new ItemStack(ModItems.manaTablet),
				'I', LibOreDict.MANA_STEEL);
		recipeManaRing = BotaniaAPI.getLatestAddedRecipe();

		// Aura Band Recipe
		addOreDictRecipe(new ItemStack(ModItems.auraRing),
				"RI ", "I I", " I ",
				'R', LibOreDict.RUNE[8],
				'I', LibOreDict.MANA_STEEL);
		recipeAuraRing = BotaniaAPI.getLatestAddedRecipe();

		// Greater Mana Band Recipe
		addOreDictRecipe(new ItemStack(ModItems.manaRingGreater),
				"IRI",
				'I', LibOreDict.TERRA_STEEL,
				'R', new ItemStack(ModItems.manaRing));
		recipeGreaterManaRing = BotaniaAPI.getLatestAddedRecipe();

		// Greater Aura Band Recipe
		addOreDictRecipe(new ItemStack(ModItems.auraRingGreater),
				"IRI",
				'I', LibOreDict.TERRA_STEEL,
				'R', new ItemStack(ModItems.auraRing));
		recipeGreaterAuraRing = BotaniaAPI.getLatestAddedRecipe();

		// Soujourner's Belt Recipe
		addOreDictRecipe(new ItemStack(ModItems.travelBelt),
				"EL ", "L L", "SLA",
				'E', LibOreDict.RUNE[2],
				'A', LibOreDict.RUNE[3],
				'S', LibOreDict.MANA_STEEL,
				'L', new ItemStack(Items.leather));
		recipeTravelBelt = BotaniaAPI.getLatestAddedRecipe();

		// Tectonic Girdle Recipe
		addOreDictRecipe(new ItemStack(ModItems.knockbackBelt),
				"AL ", "L L", "SLE",
				'E', LibOreDict.RUNE[2],
				'A', LibOreDict.RUNE[1],
				'S', LibOreDict.MANA_STEEL,
				'L', new ItemStack(Items.leather));
		recipeKnocbackBelt = BotaniaAPI.getLatestAddedRecipe();

		// Snowflake Pendant Recipe
		addOreDictRecipe(new ItemStack(ModItems.icePendant),
				"WS ", "S S", "MSR",
				'S', new ItemStack(Items.string),
				'M', LibOreDict.MANA_STEEL,
				'R', LibOreDict.RUNE[0],
				'W', LibOreDict.RUNE[7]);
		recipeIcePendant = BotaniaAPI.getLatestAddedRecipe();

		// Pyroclast Pendant Recipe
		addOreDictRecipe(new ItemStack(ModItems.lavaPendant),
				"MS ", "S S", "DSF",
				'S', new ItemStack(Items.string),
				'D', LibOreDict.MANA_DIAMOND,
				'M', LibOreDict.RUNE[5],
				'F', LibOreDict.RUNE[1]);
		recipeLavaPendant = BotaniaAPI.getLatestAddedRecipe();

		// Golden Laurel Crown Recipe
		addOreDictRecipe(new ItemStack(ModItems.goldLaurel),
				"G G", "LEL", "LLL",
				'G', "ingotGold",
				'L', "treeLeaves",
				'E', LibOreDict.LIFE_ESSENCE);
		recipeGoldenLaurel = BotaniaAPI.getLatestAddedRecipe();

		// Tiny Planet Block Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.tinyPlanet),
				"SSS", "SPS", "SSS",
				'S', "stone",
				'P', ModItems.tinyPlanet);
		recipeTinyPlanetBlock = BotaniaAPI.getLatestAddedRecipe();

		// Alchemy Catalyst Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.alchemyCatalyst),
				"SGS", "BPB", "SGS",
				'S', LibOreDict.LIVING_ROCK,
				'G', "ingotGold",
				'B', new ItemStack(Items.brewing_stand),
				'P', LibOreDict.MANA_PEARL);
		recipeAlchemyCatalyst = BotaniaAPI.getLatestAddedRecipe();

		// Open Crate Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.openCrate),
				"WWW", "W W", "W W",
				'W', new ItemStack(ModBlocks.livingwood, 1, 1));
		recipeOpenCrate = BotaniaAPI.getLatestAddedRecipe();

		// Eye of the Ancients Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.forestEye),
				"MSM", "SES", "MSM",
				'M', LibOreDict.MANA_STEEL,
				'S', LibOreDict.LIVING_ROCK,
				'E', new ItemStack(Items.ender_eye));
		recipeForestEye = BotaniaAPI.getLatestAddedRecipe();

		// Redstone Root Recipe
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manaResource, 1, 6), new ItemStack(Items.redstone), new ItemStack(Blocks.tallgrass, 1, 1));
		recipeRedstoneRoot = BotaniaAPI.getLatestAddedRecipe();

		// Drum of the Wild Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.forestDrum),
				"WLW", "WHW", "WLW",
				'W', LibOreDict.LIVING_WOOD,
				'L', new ItemStack(Items.leather),
				'H', new ItemStack(ModItems.grassHorn));
		recipeForestDrum = BotaniaAPI.getLatestAddedRecipe();

		// Ring of Chordata Recipe
		addOreDictRecipe(new ItemStack(ModItems.waterRing),
				"WMP", "M M", "SM ",
				'W', LibOreDict.RUNE[0],
				'M', LibOreDict.MANA_STEEL,
				'P', new ItemStack(Items.fish, 1, 3),
				'S', new ItemStack(Items.fish, 1, 1));
		recipeWaterRing = BotaniaAPI.getLatestAddedRecipe();

		// Ring of the Mantle Recipe
		addOreDictRecipe(new ItemStack(ModItems.miningRing),
				"EMP", "M M", " M ",
				'E', LibOreDict.RUNE[2],
				'M', LibOreDict.MANA_STEEL,
				'P', new ItemStack(Items.golden_pickaxe));
		recipeMiningRing = BotaniaAPI.getLatestAddedRecipe();

		// Ring of Magnetization Recipe
		addOreDictRecipe(new ItemStack(ModItems.magnetRing),
				"LM ", "M M", " M ",
				'L', new ItemStack(ModItems.lens, 1, 10),
				'M', LibOreDict.MANA_STEEL);
		recipeMagnetRing = BotaniaAPI.getLatestAddedRecipe();

		// Terra Shatterer Recipe
		addOreDictRecipe(new ItemStack(ModItems.terraPick),
				"ITI", "ILI", " L ",
				'T', new ItemStack(ModItems.manaTablet, 1, -1),
				'I', LibOreDict.TERRA_STEEL,
				'L', LibOreDict.LIVINGWOOD_TWIG);
		recipeTerraPick = BotaniaAPI.getLatestAddedRecipe();

		// Charm of the Diva Recipe
		addOreDictRecipe(new ItemStack(ModItems.divaCharm),
				"LGP", " HG", " GL",
				'L', LibOreDict.LIFE_ESSENCE,
				'G', "ingotGold",
				'H', LibOreDict.RUNE[15],
				'P', new ItemStack(ModItems.tinyPlanet));
		recipeDivaCharm = BotaniaAPI.getLatestAddedRecipe();

		// Flugel Tiara Recipe
		addOreDictRecipe(new ItemStack(ModItems.flightTiara),
				"LLL", "ILI", "FIF",
				'L', LibOreDict.LIFE_ESSENCE,
				'I', LibOreDict.ELEMENTIUM,
				'F', new ItemStack(Items.feather));
		recipeFlightTiara = BotaniaAPI.getLatestAddedRecipe();

		// Glimmering Flowers Recipes
		for(int i = 0; i < 16; i++)
			addShapelessOreDictRecipe(new ItemStack(ModBlocks.shinyFlower, 1, i), new ItemStack(Items.glowstone_dust), new ItemStack(Items.glowstone_dust), LibOreDict.FLOWER[i]);
		recipesShinyFlowers = BotaniaAPI.getLatestAddedRecipes(16);

		// Abstruse Platform Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.platform),
				"343", "0 0",
				'0', new ItemStack(ModBlocks.livingwood, 0, 0),
				'3', new ItemStack(ModBlocks.livingwood, 0, 3),
				'4', new ItemStack(ModBlocks.livingwood, 0, 4));
		recipePlatform = BotaniaAPI.getLatestAddedRecipe();

		// Soulscribe Recipe
		addOreDictRecipe(new ItemStack(ModItems.enderDagger),
				"P", "S", "T",
				'P', LibOreDict.MANA_PEARL,
				'S', LibOreDict.MANA_STEEL,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeEnderDagger = BotaniaAPI.getLatestAddedRecipe();

		// Quartz Recipes
		if(ConfigHandler.darkQuartzEnabled)
			recipeDarkQuartz = addQuartzRecipes(0, Items.coal, ModBlocks.darkQuartz, ModBlocks.darkQuartzStairs, ModBlocks.darkQuartzSlab);
		addQuartzRecipes(1, null, ModBlocks.manaQuartz, ModBlocks.manaQuartzStairs, ModBlocks.manaQuartzSlab);
		recipeBlazeQuartz = addQuartzRecipes(2, Items.blaze_powder, ModBlocks.blazeQuartz, ModBlocks.blazeQuartzStairs, ModBlocks.blazeQuartzSlab);

		GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, 3),
				"QQQ", "QCQ", "QQQ",
				'Q', Items.quartz,
				'C', new ItemStack(Blocks.red_flower, 1, 2));
		GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, 3),
				"QQQ", "QCQ", "QQQ",
				'Q', Items.quartz,
				'C', new ItemStack(Blocks.red_flower, 1, 7));
		GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, 3),
				"QQQ", "QCQ", "QQQ",
				'Q', Items.quartz,
				'C', new ItemStack(Blocks.double_plant, 1, 1));
		GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, 3),
				"QQQ", "QCQ", "QQQ",
				'Q', Items.quartz,
				'C', new ItemStack(Blocks.double_plant, 1, 5));
		recipesLavenderQuartz = BotaniaAPI.getLatestAddedRecipes(4);
		addQuartzRecipes(3, null, ModBlocks.lavenderQuartz, ModBlocks.lavenderQuartzStairs, ModBlocks.lavenderQuartzSlab);

		recipeRedQuartz = addQuartzRecipes(4, Items.redstone, ModBlocks.redQuartz, ModBlocks.redQuartzStairs, ModBlocks.redQuartzSlab);
		addQuartzRecipes(5, null, ModBlocks.elfQuartz, ModBlocks.elfQuartzStairs, ModBlocks.elfQuartzSlab);

		// Alfheim Portal Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.alfPortal),
				"WWW", "WTW", "WWW",
				'W', LibOreDict.LIVING_WOOD,
				'T', LibOreDict.TERRA_STEEL);
		recipeAlfPortal = BotaniaAPI.getLatestAddedRecipe();

		// Natura Pylon Recipe
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.pylon, 2, 1), ModBlocks.pylon, LibOreDict.TERRA_STEEL, ModBlocks.pylon);
		recipeNaturaPylon = BotaniaAPI.getLatestAddedRecipe();

		// Rod of the Seas Recipe
		addOreDictRecipe(new ItemStack(ModItems.waterRod),
				"  B", " T ", "R  ",
				'B', new ItemStack(Items.potionitem),
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'R', LibOreDict.RUNE[0]);
		recipeWaterRod = BotaniaAPI.getLatestAddedRecipe();

		// Elementium Armor & Tools Recipes
		addOreDictRecipe(new ItemStack(ModItems.elementiumHelm),
				"SSS", "S S",
				'S', LibOreDict.ELEMENTIUM);
		recipeElementiumHelm = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumChest),
				"S S", "SSS", "SSS",
				'S', LibOreDict.ELEMENTIUM);
		recipeElementiumChest = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumLegs),
				"SSS", "S S", "S S",
				'S', LibOreDict.ELEMENTIUM);
		recipeElementiumLegs = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumBoots),
				"S S", "S S",
				'S', LibOreDict.ELEMENTIUM);
		recipeElementiumBoots = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumPick),
				"SSS", " T ", " T ",
				'S', LibOreDict.ELEMENTIUM,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeElementiumPick = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumShovel),
				"S", "T", "T",
				'S', LibOreDict.ELEMENTIUM,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeElementiumShovel = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumAxe),
				"SS", "TS", "T ",
				'S', LibOreDict.ELEMENTIUM,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeElementiumAxe = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumSword),
				"S", "S", "T",
				'S', LibOreDict.ELEMENTIUM,
				'T', LibOreDict.LIVINGWOOD_TWIG);
		recipeElementiumSword = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModItems.elementiumShears),
				"S ", " S",
				'S', LibOreDict.ELEMENTIUM);
		recipeElementiumShears = BotaniaAPI.getLatestAddedRecipe();

		// Extrapolated Bucket Recipe
		addOreDictRecipe(new ItemStack(ModItems.openBucket),
				"E E", " E ",
				'E', LibOreDict.ELEMENTIUM);
		recipeOpenBucket = BotaniaAPI.getLatestAddedRecipe();

		// Conjuration Catalyst Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.conjurationCatalyst),
				"SBS", "GPG", "SGS",
				'S', LibOreDict.LIVING_ROCK,
				'G', LibOreDict.ELEMENTIUM,
				'B',LibOreDict.PIXIE_DUST,
				'P', new ItemStack(ModBlocks.alchemyCatalyst));
		recipeConjurationCatalyst = BotaniaAPI.getLatestAddedRecipe();

		// Life Aggregator Recipe
		addOreDictRecipe(new ItemStack(ModItems.spawnerMover),
				"EIE", "IDI", "EIE",
				'E', LibOreDict.LIFE_ESSENCE,
				'I', LibOreDict.ELEMENTIUM,
				'D', LibOreDict.DRAGONSTONE);
		recipeSpawnerMover = BotaniaAPI.getLatestAddedRecipe();

		// Great Fairy Ring Recipe
		addOreDictRecipe(new ItemStack(ModItems.pixieRing),
				"DE ", "E E", " E ",
				'D', LibOreDict.PIXIE_DUST,
				'E', LibOreDict.ELEMENTIUM);
		recipePixieRing = BotaniaAPI.getLatestAddedRecipe();

		// Globetrotter's Sash Recipe
		addOreDictRecipe(new ItemStack(ModItems.superTravelBelt),
				"E  ", " S ", "L E",
				'E', LibOreDict.ELEMENTIUM,
				'L', LibOreDict.LIFE_ESSENCE,
				'S', new ItemStack(ModItems.travelBelt));
		recipeSuperTravelBelt = BotaniaAPI.getLatestAddedRecipe();

		// Rod of Bifrost Recipe
		addOreDictRecipe(new ItemStack(ModItems.rainbowRod),
				" PD", " EP", "E  ",
				'P', LibOreDict.PIXIE_DUST,
				'E', LibOreDict.ELEMENTIUM,
				'D', LibOreDict.DRAGONSTONE);
		recipeRainbowRod = BotaniaAPI.getLatestAddedRecipe();

		// Spectral Platform Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.platform, 1, 1),
				"343", "0 0",
				'0', new ItemStack(ModBlocks.dreamwood, 0, 0),
				'3', new ItemStack(ModBlocks.dreamwood, 0, 3),
				'4', new ItemStack(ModBlocks.dreamwood, 0, 4));
		recipeSpectralPlatform = BotaniaAPI.getLatestAddedRecipe();

		// Elven Mana Spreader Recipes
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.spreader, 1, 2),
					"WWW", "EP ", "WWW",
					'W', LibOreDict.DREAM_WOOD,
					'P', LibOreDict.MANA_PETAL[i],
					'E', LibOreDict.ELEMENTIUM);
		recipesDreamwoodSpreader = BotaniaAPI.getLatestAddedRecipes(16);

		// Rod of the Skies Recipe
		addOreDictRecipe(new ItemStack(ModItems.tornadoRod),
				"  F", " T ", "R  ",
				'F', new ItemStack(Items.feather),
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'R', LibOreDict.RUNE[3]);
		recipeTornadoRod = BotaniaAPI.getLatestAddedRecipe();

		// Rod of the Hells Recipe
		addOreDictRecipe(new ItemStack(ModItems.fireRod),
				"  F", " T ", "R  ",
				'F', new ItemStack(Items.blaze_powder),
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'R', LibOreDict.RUNE[1]);
		recipeFireRod = BotaniaAPI.getLatestAddedRecipe();

		// Vine Ball Recipe
		addOreDictRecipe(new ItemStack(ModItems.vineBall),
				"VVV", "VVV", "VVV",
				'V', new ItemStack(Blocks.vine));
		recipeVineBall = BotaniaAPI.getLatestAddedRecipe();

		// Livingwood Slingshot Recipe
		addOreDictRecipe(new ItemStack(ModItems.slingshot),
				" TA", " TT", "T  ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'A', LibOreDict.RUNE[3]);
		recipeSlingshot = BotaniaAPI.getLatestAddedRecipe();

		// Prismarine Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.prismarine, 1, 0),
				" S ", "SBS", " S ",
				'S', LibOreDict.PRISMARINE_SHARD,
				'B', "cobblestone");
		recipePrismarine = BotaniaAPI.getLatestAddedRecipe();

		// Prismarine Brick Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.prismarine, 1, 1),
				" S ", "SBS", " S ",
				'S', LibOreDict.PRISMARINE_SHARD,
				'B', new ItemStack(Blocks.stonebrick));
		recipePrismarineBrick = BotaniaAPI.getLatestAddedRecipe();

		// Dark Prismarine Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.prismarine, 1, 2),
				" S ", "SBS", " S ",
				'S', LibOreDict.PRISMARINE_SHARD,
				'B', new ItemStack(Blocks.nether_brick));
		recipeDarkPrismarine = BotaniaAPI.getLatestAddedRecipe();

		// Sea Lantern Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.seaLamp),
				" S ", "SBS", " S ",
				'S', LibOreDict.PRISMARINE_SHARD,
				'B', new ItemStack(Blocks.glowstone));
		recipeSeaLamp = BotaniaAPI.getLatestAddedRecipe();

		// Influence Lens Recipe
		addOreDictRecipe(new ItemStack(ModItems.lens, 1, 12),
				"PRP", "PLP", "PPP",
				'P', LibOreDict.PRISMARINE_SHARD,
				'R', LibOreDict.RUNE[3],
				'L', new ItemStack(ModItems.lens));
		recipeLensInfluence = BotaniaAPI.getLatestAddedRecipe();

		// Weight Lens Recipe
		addOreDictRecipe(new ItemStack(ModItems.lens, 1, 13),
				"PPP", "PLP", "PRP",
				'P', LibOreDict.PRISMARINE_SHARD,
				'R', LibOreDict.RUNE[0],
				'L', new ItemStack(ModItems.lens));
		recipeLensWeight = BotaniaAPI.getLatestAddedRecipe();

		// Paint Lens Recipe
		addOreDictRecipe(new ItemStack(ModItems.lens, 1, 14),
				" E ", "WLW", " E ",
				'E', LibOreDict.ELEMENTIUM,
				'W', new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
				'L', new ItemStack(ModItems.lens));
		recipeLensPaint = BotaniaAPI.getLatestAddedRecipe();

		// Mini Island Recipes
		for(int i = 0; i < 16; i++)
			GameRegistry.addRecipe(new ItemStack(ModBlocks.miniIsland, 1, i),
					"F", "S", "D",
					'F', new ItemStack(ModBlocks.shinyFlower, 1, i),
					'S', new ItemStack(ModItems.grassSeeds),
					'D', new ItemStack(Blocks.dirt));
		recipesMiniIsland = BotaniaAPI.getLatestAddedRecipes(16);

		// Gaia Pylon Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.pylon, 1, 2),
				" D ", "EPE", " D ",
				'D', LibOreDict.PIXIE_DUST,
				'E', LibOreDict.ELEMENTIUM,
				'P', new ItemStack(ModBlocks.pylon));
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe();

		// Drum of the Gathering Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.forestDrum, 1, 1),
				"WLW", "WEW", "WLW",
				'W', LibOreDict.DREAM_WOOD,
				'L', new ItemStack(Items.leather),
				'E', LibOreDict.ELEMENTIUM);
		recipeGatherDrum = BotaniaAPI.getLatestAddedRecipe();

		// Mana Lens: Kindle Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 15), new ItemStack(ModItems.lens), new ItemStack(Items.fire_charge));
		recipeLensFire = BotaniaAPI.getLatestAddedRecipe();

		// Mana Lens: Piston Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 16), new ItemStack(ModItems.lens), new ItemStack(ModBlocks.pistonRelay));
		recipeLensPiston = BotaniaAPI.getLatestAddedRecipe();

		// Shard of Laputa Recipe
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModItems.laputaShard),
					"SFS", "PDP", "ASE",
					'S', LibOreDict.LIFE_ESSENCE,
					'D', LibOreDict.DRAGONSTONE,
					'F', new ItemStack(ModBlocks.miniIsland, 1, i),
					'P', LibOreDict.PRISMARINE_SHARD,
					'A', LibOreDict.RUNE[3],
					'E', LibOreDict.RUNE[2]);
		recipesLaputaShard = BotaniaAPI.getLatestAddedRecipes(16);

		// Necrodermal Virus Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.virus), LibOreDict.PIXIE_DUST, new ItemStack(ModItems.vineBall), new ItemStack(Items.magma_cream), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.ender_eye), new ItemStack(Items.skull, 1, 2));
		recipeVirusZombie = BotaniaAPI.getLatestAddedRecipe();

		// Nullodermal Virus Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.virus, 1, 1), LibOreDict.PIXIE_DUST, new ItemStack(ModItems.vineBall), new ItemStack(Items.magma_cream), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.ender_eye), new ItemStack(Items.skull));
		recipeVirusSkeleton = BotaniaAPI.getLatestAddedRecipe();

		// Ring of Far Reach Recipe
		addOreDictRecipe(new ItemStack(ModItems.reachRing),
				"RE ", "E E", " E ",
				'R', LibOreDict.RUNE[15],
				'E', LibOreDict.ELEMENTIUM);
		recipeReachRing = BotaniaAPI.getLatestAddedRecipe();

		// Rod of the Highlands Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.skyDirtRod), new ItemStack(ModItems.dirtRod), LibOreDict.PIXIE_DUST, LibOreDict.RUNE[3]);
		recipeSkyDirtRod = BotaniaAPI.getLatestAddedRecipe();

		// Life Imbuer Recipe
		addOreDictRecipe(new ItemStack(ModBlocks.spawnerClaw),
				"BSB", "PMP", "P P",
				'B', new ItemStack(Items.blaze_rod),
				'S', LibOreDict.ELEMENTIUM,
				'P', new ItemStack(ModBlocks.prismarine, 1, 2),
				'M', new ItemStack(ModBlocks.storage));
		recipeSpawnerClaw = BotaniaAPI.getLatestAddedRecipe();

		// Crafty Crate Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.openCrate, 1, 1),
				"WCW", "W W", "W W",
				'C', new ItemStack(Blocks.crafting_table),
				'W', new ItemStack(ModBlocks.dreamwood, 1, 1));
		recipeCraftCrate = BotaniaAPI.getLatestAddedRecipe();

		// Crafting Placeholder Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.manaResource, 32, 11), new ItemStack(Blocks.crafting_table), LibOreDict.LIVING_ROCK);
		recipePlaceholder = BotaniaAPI.getLatestAddedRecipe();

		// Reed Block Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.reedBlock),
				"rrr", "rrr", "rrr",
				'r', new ItemStack(Items.reeds));
		recipeReedBlock = BotaniaAPI.getLatestAddedRecipe();

		// Thatch Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.thatch),
				"ww", "ww",
				'w', new ItemStack(Items.wheat));
		recipeThatch = BotaniaAPI.getLatestAddedRecipe();

		// Nether Brick Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.customBrick, 4, 0),
				" B ", "BSB", " B ",
				'B', new ItemStack(Blocks.netherrack),
				'S', new ItemStack(Blocks.stonebrick));
		recipeNetherBrick = BotaniaAPI.getLatestAddedRecipe();

		// Soul Brick Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.customBrick, 4, 1),
				" B ", "BSB", " B ",
				'B', new ItemStack(Blocks.soul_sand),
				'S', new ItemStack(Blocks.stonebrick));
		recipeSoulBrick = BotaniaAPI.getLatestAddedRecipe();

		// Snow Brick Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.customBrick, 4, 2),
				" B ", "BSB", " B ",
				'B', new ItemStack(Blocks.snow),
				'S', new ItemStack(Blocks.stonebrick));
		recipeSnowBrick = BotaniaAPI.getLatestAddedRecipe();

		// Roof Tile Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.customBrick, 4, 3),
				"BB", "BB", "BB",
				'B', new ItemStack(Items.brick));
		recipeRoofTile = BotaniaAPI.getLatestAddedRecipe();

		// Azulejo Recipe
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.customBrick, 1, 4), new ItemStack(Items.dye, 1, 4), new ItemStack(Blocks.quartz_block));
		recipeAzulejo = BotaniaAPI.getLatestAddedRecipe();

		// Azulejo Cycling Recipes
		for(int i = 0; i < 12; i++)
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.customBrick, 1, 4 + (i == 11 ? 0 : i + 1)), new ItemStack(ModBlocks.customBrick, 1, 4 + i));
		recipesAzulejoCycling = BotaniaAPI.getLatestAddedRecipes(12);

		// Ender Overseer Recipe
		GameRegistry.addRecipe(new ItemStack(ModBlocks.enderEye),
				"RER", "EOE", "RER",
				'R', new ItemStack(Items.redstone),
				'E', new ItemStack(Items.ender_eye),
				'O', new ItemStack(Blocks.obsidian));
		recipeEnderEyeBlock = BotaniaAPI.getLatestAddedRecipe();
		
		// Mana and Terrasteel Block Recipes
		addOreDictRecipe(new ItemStack(ModBlocks.storage, 1, 0),
				"III", "III", "III",
				'I', LibOreDict.MANA_STEEL);
		addOreDictRecipe(new ItemStack(ModBlocks.storage, 1, 1),
				"III", "III", "III",
				'I', LibOreDict.TERRA_STEEL);
		addOreDictRecipe(new ItemStack(ModBlocks.storage, 1, 2),
				"III", "III", "III",
				'I', LibOreDict.ELEMENTIUM);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manaResource, 9, 0), new ItemStack(ModBlocks.storage, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manaResource, 9, 4), new ItemStack(ModBlocks.storage, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manaResource, 9, 7), new ItemStack(ModBlocks.storage, 1, 2));

		// Revealing Helmet Recipes
		if(Botania.thaumcraftLoaded) {
			Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manasteelHelmRevealing), new ItemStack(ModItems.manasteelHelm), new ItemStack(goggles));
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.terrasteelHelmRevealing), new ItemStack(ModItems.terrasteelHelm), new ItemStack(goggles));
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.elementiumHelmRevealing), new ItemStack(ModItems.elementiumHelm), new ItemStack(goggles));
		}

		// Slab & Stair Recipes
		addStairsAndSlabs(ModBlocks.livingwood, 0, ModBlocks.livingwoodStairs, ModBlocks.livingwoodSlab);
		addStairsAndSlabs(ModBlocks.livingwood, 1, ModBlocks.livingwoodPlankStairs, ModBlocks.livingwoodPlankSlab);
		addStairsAndSlabs(ModBlocks.livingrock, 0, ModBlocks.livingrockStairs, ModBlocks.livingrockSlab);
		addStairsAndSlabs(ModBlocks.livingrock, 1, ModBlocks.livingrockBrickStairs, ModBlocks.livingrockBrickSlab);
		addStairsAndSlabs(ModBlocks.dreamwood, 0, ModBlocks.dreamwoodStairs, ModBlocks.dreamwoodSlab);
		addStairsAndSlabs(ModBlocks.dreamwood, 1, ModBlocks.dreamwoodPlankStairs, ModBlocks.dreamwoodPlankSlab);
		addStairsAndSlabs(ModBlocks.prismarine, 0, ModBlocks.prismarineStairs, ModBlocks.prismarineSlab);
		addStairsAndSlabs(ModBlocks.prismarine, 1, ModBlocks.prismarineBrickStairs, ModBlocks.prismarineBrickSlab);
		addStairsAndSlabs(ModBlocks.prismarine, 2, ModBlocks.darkPrismarineStairs, ModBlocks.darkPrismarineSlab);
		addStairsAndSlabs(ModBlocks.reedBlock, 0, ModBlocks.reedStairs, ModBlocks.reedSlab);
		addStairsAndSlabs(ModBlocks.thatch, 0, ModBlocks.thatchStairs, ModBlocks.thatchSlab);
		addStairsAndSlabs(ModBlocks.customBrick, 0, ModBlocks.netherBrickStairs, ModBlocks.netherBrickSlab);
		addStairsAndSlabs(ModBlocks.customBrick, 1, ModBlocks.soulBrickStairs, ModBlocks.soulBrickSlab);
		addStairsAndSlabs(ModBlocks.customBrick, 2, ModBlocks.snowBrickStairs, ModBlocks.snowBrickSlab);
		addStairsAndSlabs(ModBlocks.customBrick, 3, ModBlocks.tileStairs, ModBlocks.tileSlab);
	}

	private static void addStairsAndSlabs(Block block, int meta, Block stairs, Block slab) {
		GameRegistry.addRecipe(new ItemStack(slab, 6),
				"QQQ",
				'Q', new ItemStack(block, 1, meta));
		GameRegistry.addRecipe(new ItemStack(stairs, 4),
				"  Q", " QQ", "QQQ",
				'Q', new ItemStack(block, 1, meta));
		GameRegistry.addRecipe(new ItemStack(stairs, 4),
				"Q  ", "QQ ", "QQQ",
				'Q', new ItemStack(block, 1, meta));
	}

	private static IRecipe addQuartzRecipes(int meta, Item req, Block block, Block stairs, Block slab) {
		GameRegistry.addRecipe(new ItemStack(block),
				"QQ", "QQ",
				'Q', new ItemStack(ModItems.quartz, 1, meta));
		GameRegistry.addRecipe(new ItemStack(block, 2, 2),
				"Q", "Q",
				'Q', block);
		GameRegistry.addRecipe(new ItemStack(block, 1, 1),
				"Q", "Q",
				'Q', slab);
		addStairsAndSlabs(block, 0, stairs, slab);

		if(req != null) {
			if(req == Items.coal)
				GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, meta),
						"QQQ", "QCQ", "QQQ",
						'Q', Items.quartz,
						'C', new ItemStack(req, 1, 1));
			GameRegistry.addRecipe(new ItemStack(ModItems.quartz, 8, meta),
					"QQQ", "QCQ", "QQQ",
					'Q', Items.quartz,
					'C', req);
			return BotaniaAPI.getLatestAddedRecipe();
		}
		return null;
	}

	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
