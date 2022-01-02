/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import vazkii.botania.api.recipe.ITerraPlateRecipe;

public class TerraPlateREIDisplay extends BotaniaRecipeDisplay<ITerraPlateRecipe> {
	public TerraPlateREIDisplay(ITerraPlateRecipe recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return recipe.getMana();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.TERRA_PLATE;
	}
}
