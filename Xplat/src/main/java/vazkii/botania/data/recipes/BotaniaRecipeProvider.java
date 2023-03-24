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

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class BotaniaRecipeProvider implements DataProvider {
	private final PackOutput.PathProvider recipePathProvider;
	private final PackOutput.PathProvider advancementPathProvider;

	public BotaniaRecipeProvider(PackOutput packOutput) {
		this.recipePathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
		this.advancementPathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
	}

	// [VanillaCopy] RecipeProvider
	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		Set<ResourceLocation> checkDuplicates = Sets.newHashSet();
		List<CompletableFuture<?>> output = new ArrayList<>();
		buildRecipes((recipe) -> {
			if (!checkDuplicates.add(recipe.getId())) {
				throw new IllegalStateException("Duplicate recipe " + recipe.getId());
			} else {
				output.add(DataProvider.saveStable(cache, recipe.serializeRecipe(), recipePathProvider.json(recipe.getId())));
				JsonObject advancement = recipe.serializeAdvancement();
				if (advancement != null) {
					output.add(DataProvider.saveStable(cache, advancement, advancementPathProvider.json(recipe.getId())));
				}
			}
		});
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	protected abstract void buildRecipes(Consumer<FinishedRecipe> consumer);
}
