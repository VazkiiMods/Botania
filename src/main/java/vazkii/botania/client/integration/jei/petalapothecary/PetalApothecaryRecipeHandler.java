/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.petalapothecary;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipePetals;

import javax.annotation.Nonnull;

public class PetalApothecaryRecipeHandler implements IRecipeHandler<RecipePetals> {

	@Nonnull
	@Override
	public Class<RecipePetals> getRecipeClass() {
		return RecipePetals.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "botania.petals";
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipePetals recipe) { // o is erased type param
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipePetals recipe) {
		return new PetalApothecaryRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipePetals recipe) {
		return recipe.getInputs().size() <= 16;
	}

}
