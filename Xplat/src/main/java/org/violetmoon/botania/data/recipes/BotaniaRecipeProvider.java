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

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

// [VanillaCopy] RecipeProvider's non-static implementation, except getName() is not final
public abstract class BotaniaRecipeProvider implements DataProvider {
	private final PackOutput.PathProvider recipePathProvider;
	private final PackOutput.PathProvider advancementPathProvider;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;

	public BotaniaRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this.recipePathProvider = packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
		this.advancementPathProvider = packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
		this.lookupProvider = lookupProvider;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return lookupProvider.thenCompose(registryLookup -> this.run(output, registryLookup));
	}

	private CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider registryLookup) {
		final Set<ResourceLocation> set = Sets.newHashSet();
		final List<CompletableFuture<?>> list = new ArrayList<>();
		this.buildRecipes(XplatAbstractions.INSTANCE.createRecipeOutput(
				(location, recipe, advancement) -> {
					if (!set.add(location)) {
						throw new IllegalStateException("Duplicate recipe " + location);
					} else {
						list.add(DataProvider.saveStable(output, registryLookup, Recipe.CODEC, recipe, recipePathProvider.json(location)));
						if (advancement != null) {
							list.add(DataProvider.saveStable(output, registryLookup, Advancement.CODEC, advancement.value(), advancementPathProvider.json(advancement.id())));
						}
					}
				},
				() -> {
					@SuppressWarnings("removal")
					Advancement.Builder builder =
							Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
					return builder;
				}));
		return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
	}

	public abstract void buildRecipes(RecipeOutput recipeOutput);
}
