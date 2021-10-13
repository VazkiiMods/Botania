/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import vazkii.botania.common.crafting.RecipeMarimorphosis;


import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class MarimorphosisREIDisplay extends OrechidBaseREIDisplay<RecipeMarimorphosis> {
	public MarimorphosisREIDisplay(RecipeMarimorphosis recipe) {
		super(recipe);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.MARIMORPHOSIS;
	}
}
