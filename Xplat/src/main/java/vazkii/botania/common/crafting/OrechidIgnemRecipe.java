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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.StateIngredient;

public class OrechidIgnemRecipe extends OrechidRecipe {
	public OrechidIgnemRecipe(ResourceLocation id, Block input, StateIngredient output, int weight) {
		super(id, input, output, weight);
	}

	private OrechidIgnemRecipe(OrechidRecipe recipe) {
		this(recipe.getId(), recipe.getInput(), recipe.getOutput(), recipe.getWeight());
	}

	@Override
	public RecipeType<?> getType() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_SERIALIZER;
	}

	public static class Serializer extends RecipeSerializerBase<OrechidIgnemRecipe> {
		@Override
		public OrechidIgnemRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			return new OrechidIgnemRecipe(BotaniaRecipeTypes.ORECHID_SERIALIZER.fromJson(recipeId, json));
		}

		@Override
		public OrechidIgnemRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			return new OrechidIgnemRecipe(BotaniaRecipeTypes.ORECHID_SERIALIZER.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull OrechidIgnemRecipe recipe) {
			BotaniaRecipeTypes.ORECHID_SERIALIZER.toNetwork(buffer, recipe);
		}
	}
}
