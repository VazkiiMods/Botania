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

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.item.ModItems;

public final class ModManaInfusionRecipes {

	public static RecipeManaInfusion manasteelRecipe;
	
	public static void init() {
		manasteelRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, 0), "ingotIron", 1000);
	}
	
}
