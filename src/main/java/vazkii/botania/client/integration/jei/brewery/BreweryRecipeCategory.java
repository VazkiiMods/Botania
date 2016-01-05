package vazkii.botania.client.integration.jei.brewery;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class BreweryRecipeCategory implements IRecipeCategory {

    private final IDrawableStatic background;
    private final String localizedName;

    private static final int slotContainerInput = 0;
    private static final int slotIngredient1 = 1;
    private static final int slotIngredient2 = 2;
    private static final int slotIngredient3 = 3;
    private static final int slotIngredient4 = 4;
    private static final int slotIngredient5 = 5;
    private static final int slotIngredient6 = 6;
    private static final int slotOutput = 7;

    public BreweryRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("botania", "textures/gui/neiBrewery.png");
        background = guiHelper.createDrawable(location, 0, 0, 166, 65, 0, 0, 0, 0);
        localizedName = StatCollector.translateToLocal("botania.nei.brewery");
    }

    @Nonnull
    @Override
    public String getUid() {
        return "botania.brewery";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {}

    @Override
    public void drawAnimations(Minecraft minecraft) {}

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        recipeLayout.getItemStacks().init(slotContainerInput, true, 39, 41);
        recipeLayout.getItemStacks().init(slotIngredient1, true, 64, 6);
        recipeLayout.getItemStacks().init(slotIngredient2, true, 80, 6);
        recipeLayout.getItemStacks().init(slotIngredient3, true, 96, 6);
        recipeLayout.getItemStacks().init(slotIngredient4, true, 112, 6);
        recipeLayout.getItemStacks().init(slotIngredient5, true, 128, 6);
        recipeLayout.getItemStacks().init(slotIngredient6, true, 144, 6);
        recipeLayout.getItemStacks().init(slotOutput, false, 87, 41);

        BreweryRecipeWrapper wrapper = ((BreweryRecipeWrapper) recipeWrapper);
        List inputs = wrapper.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            if (i > slotOutput) {
                break;
            }
            if (inputs.get(i) instanceof ItemStack) {
                recipeLayout.getItemStacks().set(i, ((ItemStack) inputs.get(i)));
            } else if (inputs.get(i) instanceof Collection) {
                recipeLayout.getItemStacks().set(i, ((Collection<ItemStack>) inputs.get(i)));
            }
        }
        recipeLayout.getItemStacks().set(slotOutput, wrapper.getOutputs().get(0));
    }
}
