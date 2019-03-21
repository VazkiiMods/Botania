/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 6:10:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ModManaInfusionRecipes {

	public static List<RecipeManaInfusion> manasteelRecipes;
	public static RecipeManaInfusion manaPearlRecipe;
	public static List<RecipeManaInfusion> manaDiamondRecipes;
	public static List<RecipeManaInfusion> manaPowderRecipes;
	public static RecipeManaInfusion pistonRelayRecipe;
	public static RecipeManaInfusion manaCookieRecipe;
	public static RecipeManaInfusion grassSeedsRecipe;
	public static RecipeManaInfusion podzolSeedsRecipe;
	public static List<RecipeManaInfusion> mycelSeedsRecipes;
	public static RecipeManaInfusion manaQuartzRecipe;
	public static RecipeManaInfusion tinyPotatoRecipe;
	public static RecipeManaInfusion manaInkwellRecipe;
	public static RecipeManaInfusion managlassRecipe;
	public static RecipeManaInfusion manaStringRecipe;

	public static RecipeManaInfusion sugarCaneRecipe;

	public static void init() {
		manasteelRecipes = new ArrayList<>();
		manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaSteel), Ingredient.fromTag(Tags.Items.INGOTS_IRON), 3000));
		manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.manasteelBlock), Ingredient.fromItems(Blocks.IRON_BLOCK), 27000));

		manaPearlRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPearl), Ingredient.fromItems(Items.ENDER_PEARL), 6000);

		manaDiamondRecipes = new ArrayList<>();
		manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaDiamond), Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), 10000));
		manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.manaDiamondBlock), Ingredient.fromItems(Blocks.DIAMOND_BLOCK), 90000));

		manaPowderRecipes = new ArrayList<>();
		manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPowder), Ingredient.fromItems(Items.GUNPOWDER), 500));
		manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPowder), Ingredient.fromItems(Items.REDSTONE), 500));
		manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPowder), Ingredient.fromItems(Items.GLOWSTONE_DUST), 500));
		manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPowder), Ingredient.fromItems(Items.SUGAR), 500));
		List<Item> dyes = Arrays.stream(EnumDyeColor.values()).map(ModItems::getDye).collect(Collectors.toList());
		Ingredient dyeIngredient = Ingredient.fromItems(dyes.toArray(new Item[0]));
		manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPowder), dyeIngredient, 400));

		pistonRelayRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.pistonRelay), Ingredient.fromItems(Blocks.PISTON), 15000);
		manaCookieRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaCookie), Ingredient.fromItems(Items.COOKIE), 20000);
		grassSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds), Ingredient.fromItems(Blocks.GRASS), 2500);
		podzolSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.podzolSeeds), Ingredient.fromItems(Blocks.DEAD_BUSH), 2500);

		mycelSeedsRecipes = new ArrayList<>();
		mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.mycelSeeds), Ingredient.fromItems(Blocks.RED_MUSHROOM), 6500));
		mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.mycelSeeds), Ingredient.fromItems(Blocks.BROWN_MUSHROOM), 6500));

		manaQuartzRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaQuartz), Ingredient.fromItems(Items.QUARTZ), 250);
		tinyPotatoRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.tinyPotato), Ingredient.fromItems(Items.POTATO), 1337);

		if(Botania.thaumcraftLoaded) {
			Item inkwell = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "scribing_tools"));
			manaInkwellRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaInkwell), Ingredient.fromItems(inkwell), 35000);
		}

		managlassRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.manaGlass), Ingredient.fromItems(Blocks.GLASS), 150);
		manaStringRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaString), Ingredient.fromItems(Items.STRING), 5000);

		if(Botania.gardenOfGlassLoaded)
			sugarCaneRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(Blocks.SUGAR_CANE), Ingredient.fromItems(Blocks.HAY_BLOCK), 2000);

		BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaBottle), Ingredient.fromItems(Items.GLASS_BOTTLE), 5000);
	}

}
