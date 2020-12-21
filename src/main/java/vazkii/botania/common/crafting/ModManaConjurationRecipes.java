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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.core.handler.ConfigHandler;

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
		if (!ConfigHandler.enableDefaultRecipes) return;

		redstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.redstone, 2), new ItemStack(Items.redstone), 5000);
		glowstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.glowstone_dust, 2), new ItemStack(Items.glowstone_dust), 5000);
		quartzRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.quartz, 2), new ItemStack(Items.quartz), 2500);
		coalRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.coal, 2), new ItemStack(Items.coal), 2100);
		snowballRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.snowball, 2), new ItemStack(Items.snowball), 200);
		netherrackRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.netherrack, 2), new ItemStack(Blocks.netherrack), 200);
		soulSandRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.soul_sand, 2), new ItemStack(Blocks.soul_sand), 1500);
		gravelRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Block.getBlockFromName("gravel"), 2), new ItemStack(Block.getBlockFromName("gravel")), 720);

		leavesRecipes = new ArrayList();
		for(int i = 0; i < 4; i++)
			leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.leaves, 2, i), new ItemStack(Blocks.leaves, 1, i), 2000));
		for(int i = 0; i < 2; i++)
			leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.leaves2, 2, i), new ItemStack(Blocks.leaves2, 1, i), 2000));

		grassRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.tallgrass, 2, 1), new ItemStack(Blocks.tallgrass, 1, 1), 800);
	}

}
