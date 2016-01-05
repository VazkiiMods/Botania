package vazkii.botania.client.integration.jei.petalapothecary;

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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Collection;

public class PetalApothecaryRecipeCategory implements IRecipeCategory {

    private final IDrawableStatic background;
    private final String localizedName;
    private final IDrawableStatic overlay;

    public PetalApothecaryRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(168, 64);
        localizedName = StatCollector.translateToLocal("botania.nei.petalApothecary");
        overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/petalOverlay.png"),
                0, 0, 64, 46);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "botania.petals";
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
        if (!(recipeWrapper instanceof PetalApothecaryRecipeWrapper))
            return;
        PetalApothecaryRecipeWrapper wrapper = ((PetalApothecaryRecipeWrapper) recipeWrapper);

        recipeLayout.getItemStacks().init(0, true, 0, 0);
        recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.altar));

        int index = 1, pos = 16;
        for (Object o : wrapper.getInputs()) {
            recipeLayout.getItemStacks().init(index, true, pos, 0);
            if (o instanceof Collection) {
                recipeLayout.getItemStacks().set(index, ((Collection<ItemStack>) o));
            }
            if (o instanceof ItemStack) {
                recipeLayout.getItemStacks().set(index, ((ItemStack) o));
            }
            pos += 16;
            index += 1;
        }

        recipeLayout.getItemStacks().init(index, false, 32, 0);
        recipeLayout.getItemStacks().set(index, wrapper.getOutputs().get(0));
    }

}
