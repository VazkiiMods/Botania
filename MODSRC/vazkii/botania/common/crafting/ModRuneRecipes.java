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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public final class ModRuneRecipes {

	public static RecipeRuneAltar recipeDebug;
	
	public static void init() {
		recipeDebug = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(Item.diamond), 1000, "ingotIron", "cobblestone", "plankWood", "ingotGold");
	}
	
}
