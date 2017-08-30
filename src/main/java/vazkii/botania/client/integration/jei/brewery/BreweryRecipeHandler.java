/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeBrew;

public class BreweryRecipeHandler implements IRecipeHandler<RecipeBrew> {

	@Nonnull
	@Override
	public Class<RecipeBrew> getRecipeClass() {
		return RecipeBrew.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipeBrew recipe) { // o is erased type param
		return "botania.brewery";
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeBrew recipe) {
		return new BreweryRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipeBrew recipe) {
		return recipe.getInputs().size() <= 6;
	}

}
