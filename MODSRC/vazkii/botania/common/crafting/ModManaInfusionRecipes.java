/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 30, 2014, 6:10:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public final class ModManaInfusionRecipes {

	public static List<RecipeManaInfusion> manaPetalRecipes;
	public static List<RecipeManaInfusion> manasteelRecipes;
	public static RecipeManaInfusion manaPearlRecipe;
	public static RecipeManaInfusion manaDiamondRecipe;
	public static RecipeManaInfusion pistonRelayRecipe;
	public static RecipeManaInfusion manaCookieRecipe;
	public static RecipeManaInfusion grassSeedsRecipe;
	public static RecipeManaInfusion podzolSeedsRecipe;
	public static List<RecipeManaInfusion> mycelSeedsRecipes;

	public static void init() {
		manaPetalRecipes = new ArrayList();
		for(int i = 0; i < 16; i++)
			manaPetalRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaPetal, 1, i), LibOreDict.PETAL[i], 1000));

		manasteelRecipes = new ArrayList();
		manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, 0), "ingotIron", 3000));
		manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.storage, 1, 0), new ItemStack(Blocks.iron_block), 30000));

		manaPearlRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, 1), new ItemStack(Items.ender_pearl), 6000);
		manaDiamondRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, 2), "gemDiamond", 10000);
		pistonRelayRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.pistonRelay), new ItemStack(Blocks.piston), 15000);
		manaCookieRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaCookie), new ItemStack(Items.cookie), 20000);
		grassSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds), new ItemStack(Blocks.tallgrass, 1, 1), 2500);
		podzolSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, 1), new ItemStack(Blocks.tallgrass), 2500);

		mycelSeedsRecipes = new ArrayList();
		mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, 2), new ItemStack(Blocks.red_mushroom), 6500));
		mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, 2), new ItemStack(Blocks.brown_mushroom), 6500));
	}

}
