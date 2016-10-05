/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.elventrade;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class ElvenTradeRecipeHandler implements IRecipeHandler<RecipeElvenTrade> {

	@Nonnull
	@Override
	public Class<RecipeElvenTrade> getRecipeClass() {
		return RecipeElvenTrade.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "botania.elvenTrade";
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipeElvenTrade recipe) { // o is erased type param
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeElvenTrade recipe) {
		return new ElvenTradeRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipeElvenTrade recipe) {
		return true;
	}

}
