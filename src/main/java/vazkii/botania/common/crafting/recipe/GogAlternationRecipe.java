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

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class GogAlternationRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<Recipe<?>> {
		@SuppressWarnings("unchecked")
		@Nonnull
		@Override
		public Recipe<?> fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			// just select the recipe here
			Recipe<Container> gog = (Recipe<Container>) RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "gog"));
			Recipe<Container> base = (Recipe<Container>) RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "base"));

			if (gog.getType() != base.getType()) {
				throw new IllegalArgumentException("Subrecipes must have matching types");
			}

			if (FabricLoader.getInstance().isModLoaded(LibMisc.GOG_MOD_ID)) {
				return gog;
			} else {
				return base;
			}
		}

		@Nonnull
		@Override
		public Recipe<?> fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull Recipe<?> recipe) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}
	}
}
