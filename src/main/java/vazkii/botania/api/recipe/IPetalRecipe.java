/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface IPetalRecipe extends Recipe<Inventory> {
	Identifier TYPE_ID = new Identifier(BotaniaAPI.MODID, "petal_apothecary");

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	@Override
	default boolean fits(int width, int height) {
		return false;
	}
}
