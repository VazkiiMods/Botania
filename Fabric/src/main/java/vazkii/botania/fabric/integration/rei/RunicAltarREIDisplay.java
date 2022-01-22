/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import vazkii.botania.common.crafting.RecipeRuneAltar;

import javax.annotation.Nonnull;

public class RunicAltarREIDisplay extends BotaniaRecipeDisplay<RecipeRuneAltar> {
	public RunicAltarREIDisplay(RecipeRuneAltar recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return this.recipe.getManaUsage();
	}

	@Override
	public @Nonnull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.RUNE_ALTAR;
	}
}
