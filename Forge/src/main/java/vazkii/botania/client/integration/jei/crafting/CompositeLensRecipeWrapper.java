/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.crafting;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class CompositeLensRecipeWrapper implements ICraftingCategoryExtension {
	private final List<Item> allLenses;

	public CompositeLensRecipeWrapper(CompositeLensRecipe recipe) {
		allLenses = StreamSupport.stream(Registry.ITEM.getTagOrEmpty(ModTags.Items.LENS).spliterator(), false)
				.map(ItemStack::new)
				.filter(s -> !((ItemLens) s.getItem()).isControlLens(s))
				.filter(s -> ((ItemLens) s.getItem()).isCombinable(s))
				.map(ItemStack::getItem)
				.toList();
	}

	@SuppressWarnings("removal") // todo 1.19 suppressing to unblock the build, address this before release
	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper helper, @NotNull IFocusGroup focusGroup) {
		var possibleFirstLenses = focusGroup.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT)
				.filter(f -> allLenses.contains(f.getTypedValue().getIngredient().getItem()))
				.map(f -> f.getTypedValue().getIngredient().getItem())
				.toList();
		if (possibleFirstLenses.isEmpty()) {
			possibleFirstLenses = allLenses;
		}

		List<ItemStack> firstInput = new ArrayList<>();
		List<ItemStack> secondInput = new ArrayList<>();
		List<ItemStack> outputs = new ArrayList<>();

		for (var firstLens : possibleFirstLenses) {
			var firstLensStack = new ItemStack(firstLens);
			for (var secondLens : allLenses) {
				if (secondLens == firstLens) {
					continue;
				}

				ItemStack secondLensStack = new ItemStack(secondLens);
				if (((ItemLens) firstLens).canCombineLenses(firstLensStack, secondLensStack)) {
					firstInput.add(firstLensStack);
					secondInput.add(secondLensStack);
					outputs.add(((ItemLens) firstLens).setCompositeLens(firstLensStack.copy(), secondLensStack));
				}
			}
		}

		helper.setInputs(builder, VanillaTypes.ITEM_STACK,
				List.of(firstInput, List.of(new ItemStack(Items.SLIME_BALL)), secondInput), 0, 0);
		helper.setOutputs(builder, VanillaTypes.ITEM_STACK, outputs);
	}
}
