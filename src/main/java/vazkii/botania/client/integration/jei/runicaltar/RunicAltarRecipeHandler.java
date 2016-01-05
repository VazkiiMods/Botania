package vazkii.botania.client.integration.jei.runicaltar;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import javax.annotation.Nonnull;

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
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeRuneAltar recipe) {
        return new RunicAltarRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeRuneAltar recipe) {
        return recipe.getInputs().size() <= 16;
    }

}
