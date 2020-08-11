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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BreweryRecipeCategory implements IRecipeCategory<IBrewRecipe> {

	public static final Identifier UID = prefix("brewery");
	private final IDrawableStatic background;
	private final IDrawable icon;
	private final String localizedName;

	public BreweryRecipeCategory(IGuiHelper guiHelper) {
		Identifier location = prefix("textures/gui/nei_brewery.png");
		background = guiHelper.createDrawable(location, 28, 6, 131, 55);
		background = guiHelper.createDrawable(location, 0, 0, 166, 65);
		localizedName = I18n.translate("botania.nei.brewery");
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.brewery));
	}

	@Nonnull
	@Override
	public Identifier getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IBrewRecipe> getRecipeClass() {
		return IBrewRecipe.class;
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
	public void setIngredients(IBrewRecipe recipe, IIngredients iIngredients) {
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

		for (Ingredient i : recipe.getPreviewInputs()) {
			inputBuilder.add(Arrays.asList(i.getMatchingStacksClient()));
		}

		iIngredients.setInputLists(VanillaTypes.ITEM, inputBuilder.build());
		iIngredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(outputBuilder.build()));
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IBrewRecipe recipe, @Nonnull IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		IFocus<?> focus = recipeLayout.getFocus();

		recipeLayout.getItemStacks().init(0, true, 10, 35);
		recipeLayout.getItemStacks().set(0, getItemMatchingFocus(focus, IFocus.Mode.OUTPUT, outputs.get(0), inputs.get(0)));

		int index = 1, posX = 76 - (inputs.size() * 9);
		for (int i = 1; i < inputs.size(); i++) {
			List<ItemStack> o = inputs.get(i);
			recipeLayout.getItemStacks().init(index, true, posX, 0);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		recipeLayout.getItemStacks().init(7, false, 58, 35);
		recipeLayout.getItemStacks().set(7, getItemMatchingFocus(focus, IFocus.Mode.INPUT, inputs.get(0), outputs.get(0)));

		JEIBotaniaPlugin.addDefaultRecipeIdTooltip(recipeLayout.getItemStacks(), 7, recipe.getId());
	}

	/**
	 * If an item in this recipe is focused, returns the corresponding item instead of all the containers/results.
	 */
	private List<ItemStack> getItemMatchingFocus(IFocus<?> focus, IFocus.Mode mode, List<ItemStack> focused, List<ItemStack> other) {
		if (focus != null && focus.getMode() == mode) {
			ItemStack focusStack = (ItemStack) focus.getValue();
			for (int i = 0; i < focused.size(); i++) {
				if (focusStack.isItemEqualIgnoreDamage(focused.get(i))) {
					return Collections.singletonList(other.get(i));
				}
			}
		}
		return other;
	}
}
