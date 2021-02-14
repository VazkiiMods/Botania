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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class GogAlternationRecipe {
	public static final IRecipeSerializer<IRecipe<?>> SERIALIZER = new Serializer();

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IRecipe<?>> {
		@SuppressWarnings("unchecked")
		@Nonnull
		@Override
		public IRecipe<?> read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			// just select the recipe here
			IRecipe<IInventory> gog = (IRecipe<IInventory>) RecipeManager.deserializeRecipe(recipeId, JSONUtils.getJsonObject(json, "gog"));
			IRecipe<IInventory> base = (IRecipe<IInventory>) RecipeManager.deserializeRecipe(recipeId, JSONUtils.getJsonObject(json, "base"));

			if (gog.getType() != base.getType()) {
				throw new IllegalArgumentException("Subrecipes must have matching types");
			}

			if (ModList.get().isLoaded(LibMisc.GOG_MOD_ID)) {
				return gog;
			} else {
				return base;
			}
		}

		@Nonnull
		@Override
		public IRecipe<?> read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull IRecipe<?> recipe) {
			throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
		}
	}
}
