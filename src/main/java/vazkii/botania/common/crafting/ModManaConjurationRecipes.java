/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 3:57:02 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import java.util.ArrayList;
import java.util.List;

public class ModManaConjurationRecipes {

	public static RecipeManaInfusion redstoneRecipe;
	public static RecipeManaInfusion glowstoneRecipe;
	public static RecipeManaInfusion quartzRecipe;
	public static RecipeManaInfusion coalRecipe;
	public static RecipeManaInfusion snowballRecipe;
	public static RecipeManaInfusion netherrackRecipe;
	public static RecipeManaInfusion soulSandRecipe;
	public static RecipeManaInfusion gravelRecipe;
	public static List<RecipeManaInfusion> leavesRecipes;
	public static RecipeManaInfusion grassRecipe;

	public static void init() {
		redstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.REDSTONE, 2), ingr(Items.REDSTONE), 5000);
		glowstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.GLOWSTONE_DUST, 2), ingr(Items.GLOWSTONE_DUST), 5000);
		quartzRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.QUARTZ, 2), ingr(Items.QUARTZ), 2500);
		coalRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.COAL, 2), ingr(Items.COAL), 2100);
		snowballRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.SNOWBALL, 2), ingr(Items.SNOWBALL), 200);
		netherrackRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.NETHERRACK, 2), ingr(Blocks.NETHERRACK), 200);
		soulSandRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.SOUL_SAND, 2), ingr(Blocks.SOUL_SAND), 1500);
		gravelRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.GRAVEL, 2), ingr(Blocks.GRAVEL), 720);

		leavesRecipes = new ArrayList<>();
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.OAK_LEAVES, 2), ingr(Blocks.OAK_LEAVES), 2000));
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.BIRCH_LEAVES, 2), ingr(Blocks.BIRCH_LEAVES), 2000));
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.SPRUCE_LEAVES, 2), ingr(Blocks.SPRUCE_LEAVES), 2000));
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.JUNGLE_LEAVES, 2), ingr(Blocks.JUNGLE_LEAVES), 2000));
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.ACACIA_LEAVES, 2), ingr(Blocks.ACACIA_LEAVES), 2000));
		leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.DARK_OAK_LEAVES, 2), ingr(Blocks.DARK_OAK_LEAVES), 2000));

		grassRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.GRASS), ingr(Blocks.GRASS), 800);
	}

	private static Ingredient ingr(IItemProvider i) {
		return Ingredient.fromItems(i);
	}

}
