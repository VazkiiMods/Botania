/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 6, 2014, 5:59:28 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

import java.util.ArrayList;
import java.util.List;

public final class ModRuneRecipes {

	public static RecipeRuneAltar recipeWaterRune;
	public static RecipeRuneAltar recipeFireRune;
	public static List<RecipeRuneAltar> recipesEarthRune;
	public static List<RecipeRuneAltar> recipesAirRune;
	public static RecipeRuneAltar recipeSpringRune;
	public static RecipeRuneAltar recipeSummerRune;
	public static RecipeRuneAltar recipeAutumnRune;
	public static List<RecipeRuneAltar> recipesWinterRune;
	public static RecipeRuneAltar recipeManaRune;
	public static RecipeRuneAltar recipeLustRune;
	public static RecipeRuneAltar recipeGluttonyRune;
	public static RecipeRuneAltar recipeGreedRune;
	public static RecipeRuneAltar recipeSlothRune;
	public static RecipeRuneAltar recipeWrathRune;
	public static RecipeRuneAltar recipeEnvyRune;
	public static RecipeRuneAltar recipePrideRune;

	public static RecipeRuneAltar recipeHead;

	public static void init() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		recipeWaterRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 0), costTier1, LibOreDict.MANA_POWDER, LibOreDict.MANA_STEEL, new ItemStack(Items.DYE, 1, 15), "sugarcane", new ItemStack(Items.FISHING_ROD));
		recipeFireRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 1), costTier1, LibOreDict.MANA_POWDER, LibOreDict.MANA_STEEL, "ingotBrickNether", "gunpowder", "cropNetherWart");

		recipesEarthRune = new ArrayList<>();
		recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 2), costTier1, LibOreDict.MANA_POWDER, LibOreDict.MANA_STEEL, "stone", "blockCoal", new ItemStack(Blocks.BROWN_MUSHROOM)));
		recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 2), costTier1, LibOreDict.MANA_POWDER, LibOreDict.MANA_STEEL, "stone", "blockCoal", new ItemStack(Blocks.RED_MUSHROOM)));

		recipesAirRune = new ArrayList<>();
		for(int i = 0; i < 16; i++)
			recipesAirRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 3), costTier1, LibOreDict.MANA_POWDER, LibOreDict.MANA_STEEL, new ItemStack(Blocks.CARPET, 1, i), "feather", "string"));

		recipeSpringRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 4), costTier2, LibOreDict.RUNE[0], LibOreDict.RUNE[1], "treeSapling", "treeSapling", "treeSapling", "cropWheat");
		recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 5), costTier2, LibOreDict.RUNE[2], LibOreDict.RUNE[3], new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND), "slimeball", new ItemStack(Items.MELON));
		recipeAutumnRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 6), costTier2, LibOreDict.RUNE[1], LibOreDict.RUNE[3], "treeLeaves", "treeLeaves", "treeLeaves", new ItemStack(Items.SPIDER_EYE));

		recipesWinterRune = new ArrayList<>();
		for(int i = 0; i < 16; i++)
			recipesWinterRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 7), costTier2, LibOreDict.RUNE[0], LibOreDict.RUNE[2], new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.WOOL, 1, i), new ItemStack(Items.CAKE)));

		recipeManaRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 8), costTier2, LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL, LibOreDict.MANA_STEEL, LibOreDict.MANA_PEARL);

		recipeLustRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 9), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[5], LibOreDict.RUNE[3]);
		recipeGluttonyRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 10), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[7], LibOreDict.RUNE[1]);
		recipeGreedRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 11), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[4], LibOreDict.RUNE[0]);
		recipeSlothRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 12), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[6], LibOreDict.RUNE[3]);
		recipeWrathRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 13), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[7], LibOreDict.RUNE[2]);
		recipeEnvyRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 14), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[7], LibOreDict.RUNE[0]);
		recipePrideRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 15), costTier3, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, LibOreDict.RUNE[5], LibOreDict.RUNE[1]);

		recipeHead = new HeadRecipe(new ItemStack(Items.SKULL, 1, 3), 22500, new ItemStack(Items.SKULL), LibOreDict.PIXIE_DUST, "gemPrismarine", new ItemStack(Items.NAME_TAG), new ItemStack(Items.GOLDEN_APPLE));
		BotaniaAPI.runeAltarRecipes.add(recipeHead);
	}
}
