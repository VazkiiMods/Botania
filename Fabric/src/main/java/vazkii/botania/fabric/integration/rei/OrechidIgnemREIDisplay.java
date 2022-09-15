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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.OrechidIgnemRecipe;

public class OrechidIgnemREIDisplay extends OrechidBaseREIDisplay<OrechidIgnemRecipe> {
	public OrechidIgnemREIDisplay(OrechidIgnemRecipe recipe, int totalWeight) {
		super(recipe, totalWeight);
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.ORECHID_IGNEM;
	}
}
