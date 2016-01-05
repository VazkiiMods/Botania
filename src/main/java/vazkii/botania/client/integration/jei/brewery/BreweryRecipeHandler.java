package vazkii.botania.client.integration.jei.brewery;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeBrew;

import javax.annotation.Nonnull;

public class BreweryRecipeHandler implements IRecipeHandler<RecipeBrew> {

    @Nonnull
    @Override
    public Class<RecipeBrew> getRecipeClass() {
        return RecipeBrew.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
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
