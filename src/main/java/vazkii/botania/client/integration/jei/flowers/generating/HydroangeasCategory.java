/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class HydroangeasCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	public HydroangeasCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.hydroangeas, ModSubtiles.hydroangeasFloating);
	}

	public HydroangeasCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
		super(guiHelper, flower, floatingFlower);
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.FLUID,
				Collections.singletonList(getMaterial().getAllElements()
						.stream()
						.map(fluid -> new FluidStack(fluid, 1000))
						.collect(Collectors.toList())));
	}

	protected ITag<Fluid> getMaterial() {
		return FluidTags.WATER;
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		fluids.init(0, true, 77, 5);
		fluids.set(ingredients);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new ManaGenRecipe(getMana()));
	}

	protected int getMana() {
		return 40;
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return ManaGenRecipe.class;
	}
}
