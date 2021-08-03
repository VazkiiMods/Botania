/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

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
	public @NotNull ResourceLocation getRecipeCategory() {
		return RecipeRuneAltar.TYPE_ID;
	}
}
