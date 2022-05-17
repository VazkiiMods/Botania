/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;

public class RecipeOrechidIgnem extends RecipeOrechid {
	public RecipeOrechidIgnem(ResourceLocation id, Block input, StateIngredient output, int weight) {
		super(id, input, output, weight);
	}

	private RecipeOrechidIgnem(RecipeOrechid recipe) {
		this(recipe.getId(), recipe.getInput(), recipe.getOutput(), recipe.getWeight());
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.ORECHID_IGNEM_SERIALIZER;
	}

	public static class Serializer extends RecipeSerializerBase<RecipeOrechidIgnem> {
		@Override
		public RecipeOrechidIgnem fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new RecipeOrechidIgnem(ModRecipeTypes.ORECHID_SERIALIZER.fromJson(recipeId, json));
		}

		@Override
		public RecipeOrechidIgnem fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new RecipeOrechidIgnem(ModRecipeTypes.ORECHID_SERIALIZER.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RecipeOrechidIgnem recipe) {
			ModRecipeTypes.ORECHID_SERIALIZER.toNetwork(buffer, recipe);
		}
	}
}
