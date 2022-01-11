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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeRuneAltar;

public class RunicAltarREIDisplay extends BotaniaRecipeDisplay<RecipeRuneAltar> {
	public RunicAltarREIDisplay(RecipeRuneAltar recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return this.recipe.getManaUsage();
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.RUNE_ALTAR;
	}
}
