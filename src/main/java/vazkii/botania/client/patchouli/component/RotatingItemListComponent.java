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

import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Patchouli custom component that draws a rotating circle of items from a provided list.
 */
public class RotatingItemListComponent extends RotatingItemListComponentBase {
	public List<IVariable> ingredients;

	private transient List<Ingredient> theIngredients = new ArrayList<>();

	@Override
	protected List<Ingredient> makeIngredients() {
		return theIngredients;
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		theIngredients.clear();
		for (int i = 0; i < ingredients.size(); i++) {
			theIngredients.add(lookup.apply(ingredients.get(i)).as(Ingredient.class));
		}
	}
}
