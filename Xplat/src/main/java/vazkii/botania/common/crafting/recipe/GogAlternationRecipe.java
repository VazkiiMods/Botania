/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeSerializerBase;
import vazkii.botania.xplat.XplatAbstractions;

public class GogAlternationRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();

	private static class Serializer extends RecipeSerializerBase<Recipe<?>> {
		@NotNull
		@Override
		public Recipe<?> fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			// just select the recipe here
			Recipe<?> gog = RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "gog"));
			Recipe<?> base = RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "base"));

			if (gog.getType() != base.getType()) {
				throw new IllegalArgumentException("Subrecipes must have matching types");
			}

			if (XplatAbstractions.INSTANCE.gogLoaded()) {
				return gog;
			} else {
				return base;
			}
		}

		@NotNull
		@Override
		public Recipe<?> fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull Recipe<?> recipe) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}
	}
}
