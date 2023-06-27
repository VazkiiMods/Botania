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
import com.google.gson.JsonSyntaxException;

import net.minecraft.commands.CommandFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.StateIngredient;

public class OrechidRecipe implements vazkii.botania.api.recipe.OrechidRecipe {
	private final ResourceLocation id;
	private final StateIngredient input;
	private final StateIngredient output;
	private final int weight;
	private final CommandFunction.CacheableFunction successFunction;

	public OrechidRecipe(ResourceLocation id, StateIngredient input, StateIngredient output, int weight, CommandFunction.CacheableFunction successFunction) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.weight = weight;
		this.successFunction = successFunction;
	}

	@Override
	public StateIngredient getInput() {
		return input;
	}

	@Override
	public StateIngredient getOutput() {
		return output;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public CommandFunction.CacheableFunction getSuccessFunction() {
		return this.successFunction;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return BotaniaRecipeTypes.ORECHID_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.ORECHID_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<OrechidRecipe> {
		@Override
		public OrechidRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			var input = StateIngredientHelper.tryDeserialize(GsonHelper.getAsJsonObject(json, "input"));
			if (input == null) {
				throw new JsonSyntaxException("Unknown input: " + GsonHelper.getAsJsonObject(json, "input"));
			}
			var output = StateIngredientHelper.tryDeserialize(GsonHelper.getAsJsonObject(json, "output"));
			if (output == null) {
				throw new JsonSyntaxException("Unknown output: " + GsonHelper.getAsJsonObject(json, "output"));
			}
			var weight = GsonHelper.getAsInt(json, "weight");
			var functionIdString = GsonHelper.getAsString(json, "success_function", null);
			var functionId = functionIdString == null ? null : new ResourceLocation(functionIdString);
			var function = functionId == null
					? CommandFunction.CacheableFunction.NONE
					: new CommandFunction.CacheableFunction(functionId);

			return new OrechidRecipe(recipeId, input, output, weight, function);
		}

		@Override
		public OrechidRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			var input = StateIngredientHelper.read(buffer);
			var output = StateIngredientHelper.read(buffer);
			var weight = buffer.readVarInt();
			return new OrechidRecipe(recipeId, input, output, weight, CommandFunction.CacheableFunction.NONE);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull OrechidRecipe recipe) {
			recipe.getInput().write(buffer);
			recipe.getOutput().write(buffer);
			buffer.writeVarInt(recipe.getWeight());
		}
	}
}
