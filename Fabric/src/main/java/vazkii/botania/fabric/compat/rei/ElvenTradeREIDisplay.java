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
import me.shedaniel.rei.api.common.util.EntryIngredients;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.block.tile.TileAlfPortal;

public class ElvenTradeREIDisplay extends BotaniaRecipeDisplay<IElvenTradeRecipe> {

	public ElvenTradeREIDisplay(IElvenTradeRecipe recipe) {
		super(recipe);
		this.outputs = EntryIngredients.ofItemStacks(recipe.getOutputs());
	}

	@Override
	public int getManaCost() {
		return TileAlfPortal.MANA_COST;
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.ELVEN_TRADE;
	}
}
