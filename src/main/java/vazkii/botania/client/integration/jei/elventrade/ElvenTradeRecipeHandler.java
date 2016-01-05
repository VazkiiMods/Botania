package vazkii.botania.client.integration.jei.elventrade;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import vazkii.botania.api.recipe.RecipeElvenTrade;

import javax.annotation.Nonnull;

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
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeElvenTrade recipe) {
        return new ElvenTradeRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeElvenTrade recipe) {
        return true;
    }

}
