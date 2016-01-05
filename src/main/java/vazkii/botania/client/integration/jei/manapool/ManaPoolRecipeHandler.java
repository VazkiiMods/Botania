package vazkii.botania.client.integration.jei.manapool;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import javax.annotation.Nonnull;

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
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeManaInfusion recipe) {
        return new ManaPoolRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeManaInfusion recipe) {
        return recipe.getManaToConsume() <= 100000;
    }

}
