/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public interface BotaniaRecipeProvider {
	void registerRecipes(Consumer<RecipeJsonProvider> consumer);
}
