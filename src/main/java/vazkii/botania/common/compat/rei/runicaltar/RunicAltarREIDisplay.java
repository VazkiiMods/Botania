/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.runicaltar;

import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.compat.rei.BotaniaRecipeDisplay;
import vazkii.botania.common.crafting.RecipeRuneAltar;

public class RunicAltarREIDisplay extends BotaniaRecipeDisplay<RecipeRuneAltar> {
	public RunicAltarREIDisplay(RecipeRuneAltar recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return this.recipe.getManaUsage();
	}

	@Override
	public @NotNull Identifier getRecipeCategory() {
		return this.recipe.TYPE_ID;
	}
}
