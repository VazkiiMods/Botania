/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.manapool;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeManaInfusion;

public class ManaPoolRecipeHandler implements IRecipeHandler<RecipeManaInfusion> {

	@Nonnull
	@Override
	public Class<RecipeManaInfusion> getRecipeClass() {
		return RecipeManaInfusion.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "botania.manaPool";
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RecipeManaInfusion recipe) { // o is erased type param
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeManaInfusion recipe) {
		return new ManaPoolRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecipeManaInfusion recipe) {
		return recipe.getManaToConsume() <= 100000;
	}

}
