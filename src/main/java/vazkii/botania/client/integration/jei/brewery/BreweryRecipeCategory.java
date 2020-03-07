/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BreweryRecipeCategory implements IRecipeCategory<RecipeBrew> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "brewery");
	private final IDrawableStatic background;
	private final IDrawable icon;
	private final String localizedName;

	public BreweryRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("botania", "textures/gui/nei_brewery.png");
		background = guiHelper.createDrawable(location, 0, 0, 166, 65);
		localizedName = I18n.format("botania.nei.brewery");
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.brewery));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends RecipeBrew> getRecipeClass() {
		return RecipeBrew.class;
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

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(RecipeBrew recipe, IIngredients iIngredients) {
		ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> outputBuilder = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> containers = ImmutableList.builder();

		final List<ItemStack> inputs = Arrays.asList(new ItemStack(ModItems.vial),
				new ItemStack(ModItems.flask), new ItemStack(ModItems.incenseStick), new ItemStack(ModItems.bloodPendant));
		for (ItemStack stack : inputs) {
			ItemStack brewed = recipe.getOutput(stack);
			if (!brewed.isEmpty()) {
				containers.add(stack);
				outputBuilder.add(brewed);
			}
		}
		inputBuilder.add(containers.build());

		for (Ingredient i : recipe.getInputs()) {
			inputBuilder.add(Arrays.asList(i.getMatchingStacks()));
		}

		iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
		iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilder.build()));
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeBrew recipe, @Nonnull IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		IFocus<?> focus = recipeLayout.getFocus();

		recipeLayout.getItemStacks().init(0, true, 39, 41);
		recipeLayout.getItemStacks().set(0, getItemMatchingFocus(focus, IFocus.Mode.OUTPUT, outputs.get(0), inputs.get(0)));

		int index = 1, posX = 60;
		for (int i = 1; i < inputs.size(); i++) {
			List<ItemStack> o = inputs.get(i);
			recipeLayout.getItemStacks().init(index, true, posX, 6);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		recipeLayout.getItemStacks().init(7, false, 87, 41);
		recipeLayout.getItemStacks().set(7, getItemMatchingFocus(focus, IFocus.Mode.INPUT, inputs.get(0), outputs.get(0)));
	}

	/**
	 * If an item in this recipe is focused, returns the corresponding item instead of all the containers/results.
	 */
	private List<ItemStack> getItemMatchingFocus(IFocus<?> focus, IFocus.Mode mode, List<ItemStack> focused, List<ItemStack> other) {
		if (focus != null && focus.getMode() == mode) {
			ItemStack focusStack = (ItemStack) focus.getValue();
			for (int i = 0; i < focused.size(); i++) {
				if (focusStack.isItemEqual(focused.get(i))) {
					return Collections.singletonList(other.get(i));
				}
			}
		}
		return other;
	}
}
