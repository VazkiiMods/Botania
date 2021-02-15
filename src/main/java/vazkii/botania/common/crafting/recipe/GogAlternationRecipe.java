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
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class GogAlternationRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<Recipe<?>> {
		@SuppressWarnings("unchecked")
		@Nonnull
		@Override
		public Recipe<?> read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			// just select the recipe here
			Recipe<Inventory> gog = (Recipe<Inventory>) RecipeManager.deserialize(recipeId, JsonHelper.getObject(json, "gog"));
			Recipe<Inventory> base = (Recipe<Inventory>) RecipeManager.deserialize(recipeId, JsonHelper.getObject(json, "base"));

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
		public Recipe<?> read(@Nonnull Identifier recipeId, @Nonnull PacketByteBuf buffer) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}

		@Override
		public void write(@Nonnull PacketByteBuf buffer, @Nonnull Recipe<?> recipe) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}
	}
}
