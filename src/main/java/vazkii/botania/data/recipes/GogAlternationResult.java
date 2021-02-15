/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonObject;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import vazkii.botania.common.crafting.recipe.GogAlternationRecipe;

import javax.annotation.Nullable;

public class GogAlternationResult implements RecipeJsonProvider {
	private final RecipeJsonProvider gogRecipe;
	private final RecipeJsonProvider baseRecipe;

	public GogAlternationResult(RecipeJsonProvider gogRecipe, RecipeJsonProvider baseRecipe) {
		this.gogRecipe = gogRecipe;
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void serialize(JsonObject json) {
		json.add("gog", gogRecipe.toJson());
		json.add("base", baseRecipe.toJson());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return GogAlternationRecipe.SERIALIZER;
	}

	// Take these from the base recipe
	@Override
	public Identifier getRecipeId() {
		return baseRecipe.getRecipeId();
	}

	@Nullable
	@Override
	public JsonObject toAdvancementJson() {
		return baseRecipe.toAdvancementJson();
	}

	@Nullable
	@Override
	public Identifier getAdvancementId() {
		return baseRecipe.getAdvancementId();
	}
}
