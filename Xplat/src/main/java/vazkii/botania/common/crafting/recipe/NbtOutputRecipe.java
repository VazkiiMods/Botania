/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

public class NbtOutputRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new NbtOutputRecipe.Serializer();

	private static class Serializer implements RecipeSerializer<Recipe<?>> {
		@NotNull
		@Override
		public Recipe<?> fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
			var recipe = RecipeManager.fromJson(resourceLocation, GsonHelper.getAsJsonObject(jsonObject, "recipe"));
			JsonElement nbt = jsonObject.get("nbt");

			if (nbt == null) {
				throw new JsonSyntaxException("No nbt tag");
			}
			try {
				CompoundTag tag = TagParser.parseTag(GsonHelper.convertToString(nbt, "nbt"));
				// XXX: Hack, but we only use this recipe type with vanilla recipe types which return a constant from
				// getResultItem without consulting the RegistryAccess
				recipe.getResultItem(RegistryAccess.EMPTY).setTag(tag);
			} catch (CommandSyntaxException e) {
				throw new JsonSyntaxException("Invalid nbt tag: " + e.getMessage(), e);
			}
			return recipe;
		}

		@NotNull
		@Override
		public Recipe<?> fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			throw new IllegalStateException("NbtOutputRecipe should not be sent over network");
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull Recipe<?> recipe) {
			throw new IllegalStateException("NbtOutputRecipe should not be sent over network");
		}
	}
}
