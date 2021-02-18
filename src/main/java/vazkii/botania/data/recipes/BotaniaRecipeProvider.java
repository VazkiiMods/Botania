/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.util.Identifier;

import vazkii.botania.mixin.AccessorRecipesProvider;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class BotaniaRecipeProvider implements DataProvider {
	private final DataGenerator generator;

	protected BotaniaRecipeProvider(DataGenerator generator) {
		this.generator = generator;
	}

	// [VanillaCopy] RecipeProvider
	@Override
	public void run(DataCache cache) {
		Path path = this.generator.getOutput();
		Set<Identifier> set = Sets.newHashSet();
		registerRecipes((recipeJsonProvider) -> {
			if (!set.add(recipeJsonProvider.getRecipeId())) {
				throw new IllegalStateException("Duplicate recipe " + recipeJsonProvider.getRecipeId());
			} else {
				AccessorRecipesProvider.callSaveRecipe(cache, recipeJsonProvider.toJson(), path.resolve("data/" + recipeJsonProvider.getRecipeId().getNamespace() + "/recipes/" + recipeJsonProvider.getRecipeId().getPath() + ".json"));
				JsonObject jsonObject = recipeJsonProvider.toAdvancementJson();
				if (jsonObject != null) {
					AccessorRecipesProvider.callSaveRecipeAdvancement(cache, jsonObject, path.resolve("data/" + recipeJsonProvider.getRecipeId().getNamespace() + "/advancements/" + recipeJsonProvider.getAdvancementId().getPath() + ".json"));
				}
			}
		});
	}

	abstract void registerRecipes(Consumer<RecipeJsonProvider> consumer);
}
