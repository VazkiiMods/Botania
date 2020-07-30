/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class CompositeLensRecipeWrapper implements ICustomCraftingCategoryExtension {
	private final List<List<ItemStack>> inputs;
	private final List<Item> lenses;

	public CompositeLensRecipeWrapper(CompositeLensRecipe recipe) {
		List<ItemStack> lensStacks = ItemTags.getContainer().getOrCreate(prefix("lens"))
				.values().stream()
				.map(ItemStack::new)
				.filter(s -> !((ItemLens) s.getItem()).isControlLens(s))
				.filter(s -> ((ItemLens) s.getItem()).isCombinable(s))
				.collect(Collectors.toList());
		lenses = lensStacks.stream().map(ItemStack::getItem).collect(Collectors.toList());
		inputs = ImmutableList.of(lensStacks, ImmutableList.of(new ItemStack(Items.SLIME_BALL)), lensStacks);
	}

	@Override
	public void setIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().set(ingredients);
		recipeLayout.setShapeless();
		if (recipeLayout.getFocus() != null) {
			ItemStack stack = (ItemStack) recipeLayout.getFocus().getValue();
			int idx = lenses.indexOf(stack.getItem());
			if (idx != -1) {
				setLenses(recipeLayout, idx, idx);
				return;
			}
		}
		setLenses(recipeLayout, 1, lenses.size() - 1);
	}

	private void setLenses(IRecipeLayout recipeLayout, int start, int end) {
		List<ItemStack> firstInput = new ArrayList<>();
		List<ItemStack> secondInput = new ArrayList<>();
		List<ItemStack> outputs = new ArrayList<>();

		if (end >= lenses.size()) {
			end = lenses.size() - 1;
		}

		for (int i = start; i <= end; i++) {
			ItemStack firstLens = new ItemStack(lenses.get(i));
			for (Item secondLens : lenses) {
				if (secondLens == firstLens.getItem()) {
					continue;
				}

				ItemStack secondLensStack = new ItemStack(secondLens);
				if (((ItemLens) firstLens.getItem()).canCombineLenses(firstLens, secondLensStack)) {
					firstInput.add(firstLens);
					secondInput.add(secondLensStack);
					outputs.add(((ItemLens) firstLens.getItem()).setCompositeLens(firstLens.copy(), secondLensStack));
				}
			}

		}
		recipeLayout.getItemStacks().set(1, firstInput);
		recipeLayout.getItemStacks().set(3, secondInput);
		recipeLayout.getItemStacks().set(0, outputs);
	}

}
