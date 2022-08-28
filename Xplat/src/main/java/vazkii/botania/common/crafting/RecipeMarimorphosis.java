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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.*;

public class RecipeMarimorphosis extends RecipeOrechid {
	private final int weightBonus;
	private final TagKey<Biome> biomes;

	public RecipeMarimorphosis(ResourceLocation id, Block input, StateIngredient output, int weight, int weightBonus, TagKey<Biome> biomes) {
		super(id, input, output, weight);
		this.weightBonus = weightBonus;
		this.biomes = biomes;
	}

	@Override
	public int getWeight(@NotNull Level level, @NotNull BlockPos pos) {
		if (level.getBiome(pos).is(this.biomes)) {
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
		public RecipeMarimorphosis fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			RecipeOrechid base = ModRecipeTypes.ORECHID_SERIALIZER.fromJson(recipeId, json);

			var biomes = TagKey.create(Registry.BIOME_REGISTRY,
					new ResourceLocation(GsonHelper.getAsString(json, "biome_bonus_tag")));
			int weightBonus = GsonHelper.getAsInt(json, "biome_bonus", 0);
			if (base.getWeight() + weightBonus <= 0) {
				throw new JsonSyntaxException("Weight combined with bonus cannot be 0 or less");
			}

			return new RecipeMarimorphosis(recipeId, base.getInput(), base.getOutput(), base.getWeight(), weightBonus, biomes);
		}

		@Override
		public RecipeMarimorphosis fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			RecipeOrechid base = ModRecipeTypes.ORECHID_SERIALIZER.fromNetwork(recipeId, buffer);

			TagKey<Biome> biomes = TagKey.create(Registry.BIOME_REGISTRY, buffer.readResourceLocation());
			int weightBonus = buffer.readVarInt();

			return new RecipeMarimorphosis(recipeId, base.getInput(), base.getOutput(), base.getWeight(), weightBonus, biomes);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RecipeMarimorphosis recipe) {
			ModRecipeTypes.ORECHID_SERIALIZER.toNetwork(buffer, recipe);

			buffer.writeResourceLocation(recipe.biomes.location());
			buffer.writeVarInt(recipe.weightBonus);
		}
	}

	public int getWeightBonus() {
		return weightBonus;
	}

	public TagKey<Biome> getBiomes() {
		return biomes;
	}
}
