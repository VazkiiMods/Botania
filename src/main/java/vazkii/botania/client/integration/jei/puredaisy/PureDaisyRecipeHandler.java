/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.puredaisy;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipePureDaisy;

import javax.annotation.Nonnull;

public class PureDaisyRecipeHandler implements IRecipeHandler<RecipePureDaisy> {

	@Nonnull
	@Override
	public Class<RecipePureDaisy> getRecipeClass() {
		return RecipePureDaisy.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "botania.pureDaisy";
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipePureDaisy recipe) { // o is erased type param
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipePureDaisy recipe) {
		return new PureDaisyRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipePureDaisy recipe) {
		return true;
	}

}
