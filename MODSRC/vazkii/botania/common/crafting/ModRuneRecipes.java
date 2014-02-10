/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 6, 2014, 5:59:28 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public final class ModRuneRecipes {

	public static RecipeRuneAltar recipeWaterRune;
	public static RecipeRuneAltar recipeFireRune;
	public static RecipeRuneAltar recipeEarthRune;
	public static RecipeRuneAltar recipeAirRune;
	public static RecipeRuneAltar recipeSpringRune;
	public static RecipeRuneAltar recipeSummerRune;
	public static RecipeRuneAltar recipeAutumnRune;
	public static RecipeRuneAltar recipeWinterRune;
	public static RecipeRuneAltar recipeManaRune;
	public static RecipeRuneAltar recipeLustRune;
	public static RecipeRuneAltar recipeGluttonyRune;
	public static RecipeRuneAltar recipeGreedRune;
	public static RecipeRuneAltar recipeSlothRune;
	public static RecipeRuneAltar recipeWrathRune;
	public static RecipeRuneAltar recipeEnvyRune;
	public static RecipeRuneAltar recipePrideRune;

	public static void init() {
		final int costTier1 = 5000;
		final int costTier2 = 10000;
		final int costTier3 = 25000;
		
		recipeWaterRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 0), costTier1, LibOreDict.MANA_STEEL, new ItemStack(Item.dyePowder, 1, 15), new ItemStack(Item.reed), new ItemStack(Item.fishRaw));
		recipeEarthRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 1), costTier1, LibOreDict.MANA_STEEL, "stone", new ItemStack(Block.dirt), new ItemStack(Block.mushroomCapBrown));
		recipeAirRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 2), costTier1, LibOreDict.MANA_STEEL, new ItemStack(Block.tallGrass), new ItemStack(Item.feather), new ItemStack(Item.carrot));
		recipeFireRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 1, 3), costTier1, LibOreDict.MANA_STEEL, new ItemStack(Item.blazeRod), new ItemStack(Item.gunpowder), new ItemStack(Item.netherStalkSeeds));

	}

}
