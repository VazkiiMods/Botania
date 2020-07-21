package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.client.integration.jei.flowers.AbstractFlowerCategory;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.client.integration.jei.mana.ManaIngredientRenderer;

public abstract class SimpleGenerationCategory extends AbstractFlowerCategory<SimpleGenerationCategory.ISimpleManaGenRecipe> {

    protected final IDrawableStatic background;

    public SimpleGenerationCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
        super(guiHelper, flower, floatingFlower);
        background = guiHelper.createBlankDrawable(168, 64);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setIngredients(ISimpleManaGenRecipe recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.getStack());
        ingredients.setOutput(ManaIngredient.Type.INSTANCE, new ManaIngredient(recipe.getMana(), false));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ISimpleManaGenRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 76, 4);
        stacks.set(ingredients);

        stacks.init(-1, true, 76, 24);
        stacks.set(-1, flower);

        IGuiIngredientGroup<IManaIngredient> manaIngredients = recipeLayout.getIngredientsGroup(ManaIngredient.Type.INSTANCE);
        manaIngredients.init(0, false,
                ManaIngredientRenderer.Bar.INSTANCE,
                33, 49,
                102, 5,
                0, 0);
        manaIngredients.set(ingredients);
    }

    public interface ISimpleManaGenRecipe {

        int getMana();

        ItemStack getStack();

    }

}
