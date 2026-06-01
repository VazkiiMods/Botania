/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * A recipe with a separate set of input items that are returned unchanged, but are required to be present for producing
 * the output item.
 */
public interface RecipeWithCatalysts<T extends RecipeInput> extends Recipe<T> {
	/**
	 * @return Ingredients that are returned unchanged after the output is crafted.
	 */
	NonNullList<Ingredient> getCatalysts();

	@Override
	NonNullList<ItemStack> getRemainingItems(T input);
}
