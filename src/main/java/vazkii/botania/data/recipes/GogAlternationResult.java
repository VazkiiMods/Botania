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

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.common.crafting.recipe.GogAlternationRecipe;

import javax.annotation.Nullable;

public class GogAlternationResult implements FinishedRecipe {
	private final FinishedRecipe gogRecipe;
	private final FinishedRecipe baseRecipe;

	public GogAlternationResult(FinishedRecipe gogRecipe, FinishedRecipe baseRecipe) {
		this.gogRecipe = gogRecipe;
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void serializeRecipeData(JsonObject json) {
		json.add("gog", gogRecipe.serializeRecipe());
		json.add("base", baseRecipe.serializeRecipe());
	}

	@Override
	public RecipeSerializer<?> getType() {
		return GogAlternationRecipe.SERIALIZER;
	}

	// Take these from the base recipe
	@Override
	public ResourceLocation getId() {
		return baseRecipe.getId();
	}

	@Nullable
	@Override
	public JsonObject serializeAdvancement() {
		return baseRecipe.serializeAdvancement();
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementId() {
		return baseRecipe.getAdvancementId();
	}
}
