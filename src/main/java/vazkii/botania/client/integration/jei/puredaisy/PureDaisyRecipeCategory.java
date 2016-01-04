package vazkii.botania.client.integration.jei.puredaisy;

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
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Collection;

public class PureDaisyRecipeCategory implements IRecipeCategory {

    private final IDrawableStatic background;
    private final String localizedName;
    private final IDrawableStatic overlay;

    private static final int inputSlot = 0;
    private static final int pureDaisySlot = 1;
    private static final int outputSlot = 2;

    public PureDaisyRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("botania", "textures/gui/neiBlank.png");
        background = guiHelper.createDrawable(location, 0, 0, 166, 65, 0, 0, 0, 0);
        localizedName = StatCollector.translateToLocal("botania.nei.pureDaisy");
        overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/pureDaisyOverlay.png"),
                0, 0, 64, 46);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "botania.pureDaisy";
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
    public void drawExtras(Minecraft minecraft) {
        overlay.draw(minecraft, 48, 0);
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {}

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        if (!(recipeWrapper instanceof PureDaisyRecipeWrapper))
            return;

        PureDaisyRecipeWrapper wrapper = ((PureDaisyRecipeWrapper) recipeWrapper);
        boolean inputFluid = wrapper.getInputs().isEmpty();
        boolean outputFluid = wrapper.getOutputs().isEmpty();


        if (inputFluid) {
            recipeLayout.getFluidStacks().init(inputSlot, true, 40, 12, 16, 16, 1000, false, null);
            recipeLayout.getFluidStacks().set(inputSlot, wrapper.getFluidInputs().get(0));
        } else {
            recipeLayout.getItemStacks().init(inputSlot, true, 40, 12);
            if (wrapper.getInputs().get(0) instanceof Collection) {
                recipeLayout.getItemStacks().set(inputSlot, ((Collection<ItemStack>) wrapper.getInputs().get(0)));
            } else {
                recipeLayout.getItemStacks().set(inputSlot, ((ItemStack) wrapper.getInputs().get(0)));
            }
        }

        recipeLayout.getItemStacks().init(pureDaisySlot, true, 72, 12);
        recipeLayout.getItemStacks().set(pureDaisySlot, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY));

        if (outputFluid) {
            recipeLayout.getFluidStacks().init(outputSlot, false, 99, 12, 16, 16, 1000, false, null);
            recipeLayout.getFluidStacks().set(outputSlot, wrapper.getFluidOutputs().get(0));
        } else {
            recipeLayout.getItemStacks().init(outputSlot, false, 99, 12);
            if (wrapper.getOutputs().get(0) instanceof Collection) {
                recipeLayout.getItemStacks().set(outputSlot, ((Collection<ItemStack>) wrapper.getOutputs().get(0)));
            } else {
                recipeLayout.getItemStacks().set(outputSlot, ((ItemStack) wrapper.getOutputs().get(0)));
            }
        }
    }

}
