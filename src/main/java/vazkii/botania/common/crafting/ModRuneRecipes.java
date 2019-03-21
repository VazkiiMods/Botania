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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
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

	// todo 1.13 re-tagify these once the community settles down on some names
	public static void init() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		Ingredient manaSteel = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/manasteel")));
		Ingredient manaDiamond = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "gems/mana_diamond")));
		Ingredient manaPowder = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "dusts/mana")));
		recipeWaterRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeWater, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.BONE_MEAL), Ingredient.fromItems(Blocks.SUGAR_CANE), Ingredient.fromItems(Items.FISHING_ROD));
		recipeFireRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeFire, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.NETHER_BRICK), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.NETHER_WART));

		Ingredient stone = Ingredient.fromTag(Tags.Items.STONE);
		Ingredient coalBlock = Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL);
		recipesEarthRune = new ArrayList<>();
		recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeEarth, 2), costTier1, manaPowder, manaSteel, stone, coalBlock, Ingredient.fromItems(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)));

		recipesAirRune = new ArrayList<>();
		recipesAirRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeAir, 2), costTier1, manaPowder, manaSteel, Ingredient.fromTag(ItemTags.CARPETS), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.STRING)));
		
		Ingredient fire = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/fire")));
		Ingredient water = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/water")));
		Ingredient earth = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/earth")));
		Ingredient air = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/air")));

		Ingredient sapling = Ingredient.fromTag(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.fromTag(ItemTags.LEAVES);
		Ingredient sand = Ingredient.fromTag(ItemTags.SAND);
		recipeSpringRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeSpring), costTier2, water, fire, sapling, sapling, sapling, Ingredient.fromItems(Items.WHEAT));
		recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeSummer), costTier2, earth, air, sand, sand, Ingredient.fromItems(Items.SLIME_BALL), Ingredient.fromItems(Items.MELON_SLICE));
		recipeAutumnRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeAutumn), costTier2, fire, air, leaves, leaves, leaves, Ingredient.fromItems(Items.SPIDER_EYE));

		recipesWinterRune = new ArrayList<>();
		recipesWinterRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeWinter), costTier2, water, earth, Ingredient.fromItems(Blocks.SNOW), Ingredient.fromItems(Blocks.SNOW), Ingredient.fromTag(ItemTags.WOOL), Ingredient.fromItems(Blocks.CAKE)));

		Ingredient spring = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/spring")));
		Ingredient summer = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/summer")));
		Ingredient autumn = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/autumn")));
		Ingredient winter = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "runes/winter")));
		
		recipeManaRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeMana), costTier2, manaSteel, manaSteel, manaSteel, manaSteel, manaSteel, Ingredient.fromItems(ModItems.manaPearl));

		recipeLustRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeLust), costTier3, manaDiamond, manaDiamond, summer, air);
		recipeGluttonyRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeGluttony), costTier3, manaDiamond, manaDiamond, winter, fire);
		recipeGreedRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeGreed), costTier3, manaDiamond, manaDiamond, spring, water);
		recipeSlothRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeSloth), costTier3, manaDiamond, manaDiamond, autumn, air);
		recipeWrathRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeWrath), costTier3, manaDiamond, manaDiamond, winter, earth);
		recipeEnvyRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runeEnvy), costTier3, manaDiamond, manaDiamond, winter, water);
		recipePrideRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.runePride), costTier3, manaDiamond, manaDiamond, summer, fire);

		recipeHead = new HeadRecipe(new ItemStack(Items.PLAYER_HEAD), 22500, Ingredient.fromItems(Items.SKELETON_SKULL), Ingredient.fromItems(ModItems.pixieDust), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.NAME_TAG), Ingredient.fromItems(Items.GOLDEN_APPLE));
		BotaniaAPI.runeAltarRecipes.add(recipeHead);
	}
}
