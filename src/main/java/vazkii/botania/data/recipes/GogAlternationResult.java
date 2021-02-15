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

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.crafting.recipe.GogAlternationRecipe;

import javax.annotation.Nullable;

public class GogAlternationResult implements IFinishedRecipe {
	private final IFinishedRecipe gogRecipe;
	private final IFinishedRecipe baseRecipe;

	public GogAlternationResult(IFinishedRecipe gogRecipe, IFinishedRecipe baseRecipe) {
		this.gogRecipe = gogRecipe;
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void serialize(JsonObject json) {
		json.add("gog", gogRecipe.getRecipeJson());
		json.add("base", baseRecipe.getRecipeJson());
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return GogAlternationRecipe.SERIALIZER;
	}

	// Take these from the base recipe
	@Override
	public ResourceLocation getID() {
		return baseRecipe.getID();
	}

	@Nullable
	@Override
	public JsonObject getAdvancementJson() {
		return baseRecipe.getAdvancementJson();
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementID() {
		return baseRecipe.getAdvancementID();
	}
}
