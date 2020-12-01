/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.petalapothecary;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.compat.rei.BotaniaRecipeDisplay;
import vazkii.botania.common.crafting.RecipePetals;

@Environment(EnvType.CLIENT)
public class PetalApothecaryREIDisplay extends BotaniaRecipeDisplay<RecipePetals> {
	public PetalApothecaryREIDisplay(RecipePetals recipe) {
		super(recipe);
	}

	@Override
	public int getManaCost() {
		return 0;
	}

	@Override
	public @NotNull Identifier getRecipeCategory() {
		return recipe.TYPE_ID;
	}
}
