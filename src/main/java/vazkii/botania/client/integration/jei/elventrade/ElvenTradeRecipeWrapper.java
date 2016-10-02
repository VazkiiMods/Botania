/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.elventrade;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class ElvenTradeRecipeWrapper implements IRecipeWrapper {

	private final List input;
	private final List<ItemStack> outputs;

	@SuppressWarnings("unchecked")
	public ElvenTradeRecipeWrapper(RecipeElvenTrade recipe) {
		ImmutableList.Builder builder = ImmutableList.builder();
		for(Object o : recipe.getInputs()) {
			if(o instanceof ItemStack) {
				builder.add(o);
			}
			if(o instanceof String) {
				builder.add(OreDictionary.getOres((String) o));
			}
		}
		input = builder.build();
		outputs = ImmutableList.copyOf(recipe.getOutputs());
	}

	@Override
	public List getInputs() {
		return input;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return ImmutableList.of();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return ImmutableList.of();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
