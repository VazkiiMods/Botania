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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
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
	public static IRecipe recipeLifeEssence;
	public static IRecipe recipeGoldenLaurel;
	public static IRecipe recipeTinyPlanetBlock;
	public static IRecipe recipeAlchemyCatalyst;

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

		addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 9), new ItemStack(ModItems.lens), new ItemStack(ModBlocks.pistonRelay));
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
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 1, 5),
				LibOreDict.LIVING_WOOD, new ItemStack(Items.glowstone_dust));
		recipeLivingwoodDecor5 = BotaniaAPI.getLatestAddedRecipe();

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

		// Essence of Eternal Life Recipe
		addShapelessOreDictRecipe(new ItemStack(ModItems.manaResource, 12, 5), LibOreDict.TERRA_STEEL, new ItemStack(Items.nether_star), new ItemStack(Items.blaze_powder));
		recipeLifeEssence = BotaniaAPI.getLatestAddedRecipe();

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
	}

	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
