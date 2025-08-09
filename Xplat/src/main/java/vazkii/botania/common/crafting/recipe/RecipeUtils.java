/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class RecipeUtils {
	/**
	 * Check if every ingredient in {@code inputs} and optionally {@code catalysts} is satisfied by {@code inv}.
	 * Optionally, the slots from the inventory used to fulfill the inputs are placed into {@code matchedInputSlots}.
	 * Similarly, the slots from the inventory used to fulfill the catalysts are placed into
	 * {@code matchedCatalystSlots}.
	 */
	public static boolean matches(List<Ingredient> inputs, @Nullable List<Ingredient> catalysts, RecipeInput inv,
			@Nullable IntSet matchedInputSlots, @Nullable IntSet matchedCatalystSlots) {
		// TODO: Can we reuse the ShapelessRecipe matching logic here? It seems to be optimized in some way.
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);
		int catalystsStart = ingredientsMissing.size();
		if (catalysts != null) {
			ingredientsMissing.addAll(catalysts);
		}

		for (int i = 0; i < inv.size(); i++) {
			ItemStack input = inv.getItem(i);
			if (input.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if (ingr.test(input)) {
					stackIndex = j;
					IntSet slotsTracker = stackIndex >= catalystsStart ? matchedCatalystSlots : matchedInputSlots;
					if (slotsTracker != null) {
						slotsTracker.add(i);
					}
					break;
				}
			}

			if (stackIndex != -1) {
				ingredientsMissing.remove(stackIndex);
				if (stackIndex < catalystsStart) {
					catalystsStart--;
				}
			} else {
				return false;
			}
		}

		return ingredientsMissing.isEmpty();
	}

	/**
	 * Like the vanilla method on recipe interface, but specialHandler is called first, and if it returns
	 * nonnull, that result is used instead of vanilla's
	 */
	public static NonNullList<ItemStack> getRemainingItemsSub(RecipeInput inv, Function<ItemStack, @Nullable ItemStack> specialHandler) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < ret.size(); ++i) {
			ItemStack item = inv.getItem(i);
			ItemStack special = specialHandler.apply(item);
			if (special != null) {
				ret.set(i, special);
			} else if (item.getItem().hasCraftingRemainingItem()) {
				ret.set(i, new ItemStack(Objects.requireNonNull(item.getItem().getCraftingRemainingItem())));
			}
		}

		return ret;
	}
}
