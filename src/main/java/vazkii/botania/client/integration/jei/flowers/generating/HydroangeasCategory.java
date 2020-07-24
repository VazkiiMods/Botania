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

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HydroangeasCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	private final List<FluidStack> fluids = new ArrayList<>();

	protected void doBurnParticles(ParticleDrawable drawable) {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
		drawable.addParticle(data,
				0.5 + Math.random() * 0.2 - 0.1,
				0.5 + Math.random() * 0.2 - 0.1,
				0,
				0,
				(float) Math.random() / 30,
				0);
	}

	public HydroangeasCategory(IGuiHelper guiHelper) {
		this(guiHelper, ModSubtiles.hydroangeas, ModSubtiles.hydroangeasFloating);
	}

	public HydroangeasCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
		super(guiHelper, flower, floatingFlower);
		for (Fluid fluid : getMaterial().getAllElements()) {
			fluids.add(new FluidStack(fluid, 1000));
		}
		particle = new ParticleDrawable(drawable -> {
			if (world.rand.nextInt(8) == 0) {
				doBurnParticles(drawable);
			}
		});
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(fluids));
	}

	protected ITag<Fluid> getMaterial() {
		return FluidTags.WATER;
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
		fluids.init(0, true, 77, 5);
		fluids.set(ingredients);

		recipeLayout.getIngredientsGroup(ManaIngredient.TYPE).addTooltipCallback((slotIndex, input, ingredient, tooltip) -> tooltip.add(new TranslationTextComponent("botania.nei.hydroangeas.tooltip").func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY)));
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new ManaGenRecipe(getMana()));
	}

	protected int getMana() {
		return SubTileHydroangeas.BURN_TIME;
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return ManaGenRecipe.class;
	}

}
