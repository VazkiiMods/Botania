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

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeUtils {
	/**
	 * Check if every ingredient in {@code inputs} is satisfied by {@code inv}.
	 * Optionally, the slots from the inventory used to fulfill the inputs are placed into {@code usedSlots}.
	 */
	public static boolean matches(List<Ingredient> inputs, Inventory inv, @Nullable IntSet usedSlots) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack input = inv.getStack(i);
			if (input.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if (ingr.test(input)) {
					stackIndex = j;
					if (usedSlots != null) {
						usedSlots.add(i);
					}
					break;
				}
			}

			if (stackIndex != -1) {
				ingredientsMissing.remove(stackIndex);
			} else {
				return false;
			}
		}

		return ingredientsMissing.isEmpty();
	}
}
