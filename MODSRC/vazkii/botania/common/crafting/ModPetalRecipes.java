/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 2:22:21 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

public final class ModPetalRecipes {

	public static final int white = 0, orange = 1, magenta = 2, lightBlue = 3, yellow = 4, lime = 5, pink = 6, gray = 7, lightGray = 8, cyan = 9, purple = 10, blue = 11, brown = 12, green = 13, red = 14, black = 15;

	public static RecipePetals testRecipe;

	public static void init() {
		testRecipe = BotaniaAPI.registerPetalRecipe(new ItemStack(Item.diamond), orange, orange, white);
	}

}
