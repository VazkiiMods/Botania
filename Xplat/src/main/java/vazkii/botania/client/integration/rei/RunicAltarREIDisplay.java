/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.botania.api.recipe.RunicAltarRecipe;

import java.util.List;

public class RunicAltarREIDisplay extends BotaniaRecipeDisplay<RunicAltarRecipe> {
	private final List<EntryIngredient> catalysts;
	private final EntryIngredient reagent;

	public RunicAltarREIDisplay(RecipeHolder<? extends RunicAltarRecipe> recipe) {
		super(recipe);
		this.catalysts = EntryIngredients.ofIngredients(recipe.value().getCatalysts());
		this.reagent = EntryIngredients.ofIngredient(recipe.value().getReagent());
	}

	public List<EntryIngredient> getCatalysts() {
		return catalysts;
	}

	public EntryIngredient getReagent() {
		return reagent;
	}

	@Override
	public int getManaCost() {
		return this.recipe.value().getMana();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.RUNE_ALTAR;
	}
}
