/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.runicaltar;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public class RunicAltarRecipeHandler implements IRecipeHandler<RecipeRuneAltar> {

	@Nonnull
	@Override
	public Class<RecipeRuneAltar> getRecipeClass() {
		return RecipeRuneAltar.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "botania.runicAltar";
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipeRuneAltar recipe) { // o is erased type param
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeRuneAltar recipe) {
		return new RunicAltarRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipeRuneAltar recipe) {
		return recipe.getInputs().size() <= 16;
	}

}
