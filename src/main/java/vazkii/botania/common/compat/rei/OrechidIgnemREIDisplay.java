/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeOrechidIgnem;


import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class OrechidIgnemREIDisplay extends OrechidBaseREIDisplay<RecipeOrechidIgnem> {
	public OrechidIgnemREIDisplay(RecipeOrechidIgnem recipe) {
		super(recipe);
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.ORECHID_IGNEM;
	}
}
