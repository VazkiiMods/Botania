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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemLens;
import vazkii.botania.common.item.ItemSignalFlare;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

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
	public static IRecipe livingrockDecorRecipe1;
	public static IRecipe livingrockDecorRecipe2;
	public static IRecipe livingrockDecorRecipe3;
	public static IRecipe livingrockDecorRecipe4;
	public static IRecipe livingwoodDecorRecipe1;
	public static IRecipe livingwoodDecorRecipe2;
	public static IRecipe livingwoodDecorRecipe3;
	public static IRecipe livingwoodDecorRecipe4;
	public static IRecipe livingwoodDecorRecipe5;
	public static List<IRecipe> recipesManaBeacons;
	public static List<IRecipe> recipesSignalFlares;
	public static IRecipe recipeManaVoid;
	public static List<IRecipe> recipesManaTablet;
	public static IRecipe recipeManaDetector;
	public static IRecipe recipeManaBlaster;
	public static IRecipe recipeTurntable;

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
						'S', "stickWood");
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
		livingrockDecorRecipe1 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingrock, 1, 2), new ItemStack(ModBlocks.livingrock, 1, 1), new ItemStack(Items.wheat_seeds));
		livingrockDecorRecipe2 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingrock, 2, 3), new ItemStack(ModBlocks.livingrock, 1, 1), "cobblestone");
		livingrockDecorRecipe3 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingrock, 4, 4),
				"RR", "RR",
				'R', new ItemStack(ModBlocks.livingrock, 1, 1));
		livingrockDecorRecipe4 = BotaniaAPI.getLatestAddedRecipe();

		// Livingwood Decorative Blocks
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 1), LibOreDict.LIVING_WOOD);
		livingwoodDecorRecipe1 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 1, 2), new ItemStack(ModBlocks.livingwood, 1, 1), new ItemStack(Items.wheat_seeds));
		livingwoodDecorRecipe2 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 3),
				"WW", "WW",
				'W', new ItemStack(ModBlocks.livingwood, 1, 1));
		livingwoodDecorRecipe3 = BotaniaAPI.getLatestAddedRecipe();
		addOreDictRecipe(new ItemStack(ModBlocks.livingwood, 4, 4),
				" W ", "W W", " W ",
				'W', new ItemStack(ModBlocks.livingwood, 1, 1));
		livingwoodDecorRecipe4 = BotaniaAPI.getLatestAddedRecipe();
		addShapelessOreDictRecipe(new ItemStack(ModBlocks.livingwood, 1, 5),
				LibOreDict.LIVING_WOOD, new ItemStack(Items.glowstone_dust));
		livingwoodDecorRecipe5 = BotaniaAPI.getLatestAddedRecipe();

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
				'S', new ItemStack(ModBlocks.spreader),
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
	}

	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}
}
