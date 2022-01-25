/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;

import java.util.*;

public class RecipeMarimorphosis extends RecipeOrechid {
	private final int weightBonus;
	private final Set<BiomeCategory> biomes;

	public RecipeMarimorphosis(ResourceLocation id, Block input, StateIngredient output, int weight, int weightBonus, Collection<BiomeCategory> biomes) {
		super(id, input, output, weight);
		this.weightBonus = weightBonus;
		this.biomes = Set.copyOf(biomes);
	}

	@Override
	public int getWeight(@Nonnull Level level, @Nonnull BlockPos pos) {
		Biome biome = level.getBiome(pos);
		if (biomes.contains(biome.getBiomeCategory())) {
			return getWeight() + weightBonus;
		}
		return getWeight();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.MARIMORPHOSIS_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.MARIMORPHOSIS_SERIALIZER;
	}

	public static class Serializer extends RecipeSerializerBase<RecipeMarimorphosis> {
		@Override
		public RecipeMarimorphosis fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			RecipeOrechid base = ModRecipeTypes.ORECHID_SERIALIZER.fromJson(recipeId, json);

			Set<BiomeCategory> biomes = new HashSet<>();
			var array = GsonHelper.getAsJsonArray(json, "biomes", new JsonArray());
			for (JsonElement element : array) {
				biomes.add(BiomeCategory.byName(GsonHelper.convertToString(element, "biome entry")));
			}

			int weightBonus = GsonHelper.getAsInt(json, "biome_bonus", 0);
			if (base.getWeight() + weightBonus <= 0) {
				throw new JsonSyntaxException("Weight combined with bonus cannot be 0 or less");
			}

			return new RecipeMarimorphosis(recipeId, base.getInput(), base.getOutput(), base.getWeight(), weightBonus, biomes);
		}

		@Override
		public RecipeMarimorphosis fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			RecipeOrechid base = ModRecipeTypes.ORECHID_SERIALIZER.fromNetwork(recipeId, buffer);

			Set<BiomeCategory> biomes = new HashSet<>();
			int size = buffer.readVarInt();
			for (int i = 0; i < size; i++) {
				biomes.add(BiomeCategory.byName(buffer.readUtf()));
			}
			int weightBonus = buffer.readVarInt();

			return new RecipeMarimorphosis(recipeId, base.getInput(), base.getOutput(), base.getWeight(), weightBonus, biomes);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RecipeMarimorphosis recipe) {
			ModRecipeTypes.ORECHID_SERIALIZER.toNetwork(buffer, recipe);

			buffer.writeVarInt(recipe.biomes.size());
			for (BiomeCategory biomeCategory : recipe.biomes) {
				buffer.writeUtf(biomeCategory.getSerializedName());
			}
			buffer.writeVarInt(recipe.weightBonus);
		}
	}

}
