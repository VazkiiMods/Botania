/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import vazkii.botania.common.crafting.RecipeMarimorphosis;

public class MarimorphosisREIDisplay extends OrechidBaseREIDisplay<RecipeMarimorphosis> {
	public MarimorphosisREIDisplay(RecipeMarimorphosis recipe, int totalWeight) {
		super(recipe, totalWeight);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.MARIMORPHOSIS;
	}
}
