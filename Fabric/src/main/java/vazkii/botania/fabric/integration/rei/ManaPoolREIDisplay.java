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
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ManaInfusionRecipe;

public class ManaPoolREIDisplay extends BotaniaRecipeDisplay<ManaInfusionRecipe> {
	@Nullable
	private final StateIngredient catalyst;

	public ManaPoolREIDisplay(ManaInfusionRecipe recipe) {
		super(recipe);
		this.catalyst = recipe.getRecipeCatalyst();
	}

	@Nullable
	public StateIngredient getCatalyst() {
		return this.catalyst;
	}

	@Override
	public int getManaCost() {
		return recipe.getManaToConsume();
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.MANA_INFUSION;
	}
}
