/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 20, 2014, 3:57:02 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

public class ModManaConjurationRecipes {

	public static RecipeManaInfusion redstoneRecipe;
	public static RecipeManaInfusion glowstoneRecipe;
	public static RecipeManaInfusion quartzRecipe;
	public static RecipeManaInfusion coalRecipe;
	public static RecipeManaInfusion snowballRecipe;
	public static RecipeManaInfusion netherrackRecipe;
	public static RecipeManaInfusion soulSandRecipe;
	public static RecipeManaInfusion gravelRecipe;

	public static void init() {
		redstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.redstone, 2), new ItemStack(Items.redstone), 5000);
		glowstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.glowstone_dust, 2), new ItemStack(Items.glowstone_dust), 5000);
		quartzRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.quartz, 2), new ItemStack(Items.quartz), 2500);
		coalRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.coal, 2), new ItemStack(Items.coal), 2100);
		snowballRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.snowball, 2), new ItemStack(Items.snowball), 200);
		netherrackRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.netherrack, 2), new ItemStack(Blocks.netherrack), 200);
		soulSandRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.soul_sand, 2), new ItemStack(Blocks.soul_sand), 1500);
		gravelRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.gravel, 2), new ItemStack(Blocks.gravel), 720);
	}

}
