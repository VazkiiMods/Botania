/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 3:54:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModCrafingRecipes {

	public static final void init() {
		// Petal/Dye Recipes
		for(int i = 0; i < 16; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.petal, 2, i), new ItemStack(ModBlocks.flower, 1, i)); 
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dye, 1, i), new ItemStack(ModItems.petal, 1, i)); 
		}
	}
}
