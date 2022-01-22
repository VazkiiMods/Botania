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

import vazkii.botania.common.crafting.RecipePetals;

import javax.annotation.Nonnull;

public class PetalApothecaryREIDisplay extends BotaniaRecipeDisplay<RecipePetals> {
	public PetalApothecaryREIDisplay(RecipePetals recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return 0;
	}

	@Override
	public @Nonnull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.PETAL_APOTHECARY;
	}
}
