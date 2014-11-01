/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 1, 2014, 9:15:15 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.brew.ModBrews;

public class ModBrewRecipes {
	
	public static RecipeBrew speedBrew;
	public static RecipeBrew strengthBrew;
	
	public static void init() {
		speedBrew = BotaniaAPI.registerBrewRecipe(ModBrews.speed, new ItemStack(Items.nether_wart), new ItemStack(Items.sugar), new ItemStack(Items.redstone));
		strengthBrew = BotaniaAPI.registerBrewRecipe(ModBrews.strength, new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_powder), new ItemStack(Items.glowstone_dust));
	}

}
