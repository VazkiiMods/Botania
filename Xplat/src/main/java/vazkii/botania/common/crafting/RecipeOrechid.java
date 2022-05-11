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

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;

public class RecipeOrechid implements IOrechidRecipe {
	private final ResourceLocation id;
	private final Block input;
	private final StateIngredient output;
	private final int weight;

	public RecipeOrechid(ResourceLocation id, Block input, StateIngredient output, int weight) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.weight = weight;
	}

	@Override
	public Block getInput() {
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
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.ORECHID_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.ORECHID_SERIALIZER;
	}

	public static class Serializer extends RecipeSerializerBase<RecipeOrechid> {
		@Override
		public RecipeOrechid fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			var blockId = new ResourceLocation(GsonHelper.getAsString(json, "input"));
			var input = Registry.BLOCK.getOptional(blockId)
					.orElseThrow(() -> new JsonSyntaxException("Unknown block id: " + blockId));
			var output = StateIngredientHelper.tryDeserialize(GsonHelper.getAsJsonObject(json, "output"));
			if (output == null) {
				throw new JsonSyntaxException("Unknown output: " + GsonHelper.getAsJsonObject(json, "output"));
			}
			var weight = GsonHelper.getAsInt(json, "weight");

			return new RecipeOrechid(recipeId, input, output, weight);
		}

		@Override
		public RecipeOrechid fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			var input = Registry.BLOCK.byId(buffer.readVarInt());
			var output = StateIngredientHelper.read(buffer);
			var weight = buffer.readVarInt();
			return new RecipeOrechid(recipeId, input, output, weight);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RecipeOrechid recipe) {
			buffer.writeVarInt(Registry.BLOCK.getId(recipe.getInput()));
			recipe.getOutput().write(buffer);
			buffer.writeVarInt(recipe.getWeight());
		}
	}
}
