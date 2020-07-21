package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EndoflameCategory extends SimpleGenerationCategory {

    public EndoflameCategory(IGuiHelper guiHelper) {
        super(guiHelper, ModSubtiles.endoflame, ModSubtiles.endoflameFloating);
    }

    @Override
    protected Collection<ISimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
        List<ISimpleManaGenRecipe> recipes = new ArrayList<>();
        for(ItemStack stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM)) {
            int burnTime = SubTileEndoflame.getBurnTime(stack);
            if(burnTime > 0) {
                recipes.add(new EndoflameCategory.EndoflameRecipe(stack, burnTime));
            }
        }
        return recipes;
    }

    @Override
    public Class<? extends ISimpleManaGenRecipe> getRecipeClass() {
        return EndoflameRecipe.class;
    }

    protected static class EndoflameRecipe implements ISimpleManaGenRecipe {

        public final ItemStack fuel;
        private final int burnTime;

        public EndoflameRecipe(ItemStack fuel, int burnTime) {
            this.fuel = fuel;
            this.burnTime = burnTime;
        }

        /**
         * @see SubTileEndoflame#getValueForPassiveGeneration()
         */
        @Override
        public int getMana() {
            return burnTime * 3;
        }

        @Override
        public ItemStack getStack() {
            return fuel;
        }

    }

}
