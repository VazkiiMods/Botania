/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.component;

import net.minecraft.item.crafting.Ingredient;

import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Patchouli custom component that draws a rotating circle of items from a provided list.
 */
public class RotatingItemListComponent extends RotatingItemListComponentBase {
	public List<String> ingredients;

	@Override
	protected List<Ingredient> makeIngredients() {
		return ingredients.stream().map(PatchouliAPI.instance::deserializeIngredient).collect(Collectors.toList());
	}

	@Override
	public void onVariablesAvailable(Function<String, String> lookup) {
		for (int i = 0; i < ingredients.size(); i++) {
			ingredients.set(i, lookup.apply(ingredients.get(i)));
		}
	}
}
