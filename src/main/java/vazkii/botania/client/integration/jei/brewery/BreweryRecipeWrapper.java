/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

public class BreweryRecipeWrapper implements IRecipeWrapper {

    private final List input;
    private final ItemStack output;

    public BreweryRecipeWrapper(RecipeBrew recipeBrew) {
        ImmutableList.Builder builder = ImmutableList.builder();
        builder.add(new ItemStack(ModItems.vial));
        for (Object o : recipeBrew.getInputs()) {
            if (o instanceof ItemStack) {
                builder.add(o);
            }
            if (o instanceof String) {
                builder.add(OreDictionary.getOres(((String) o)));
            }
        }

        input = builder.build();
        output = recipeBrew.getOutput(new ItemStack(ModItems.vial)).copy();
    }

    @Override
    public List getInputs() {
        return input;
    }

    @Override
    public List<ItemStack> getOutputs() {
        return ImmutableList.of(output);
    }

    @Override
    public List<FluidStack> getFluidInputs() { return ImmutableList.of(); }

    @Override
    public List<FluidStack> getFluidOutputs() { return ImmutableList.of(); }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {}

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {}

}
