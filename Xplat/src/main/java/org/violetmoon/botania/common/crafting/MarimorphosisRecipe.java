/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.function.Function;

public class MarimorphosisRecipe extends OrechidRecipe {
	public static final RecipeSerializer<MarimorphosisRecipe> SERIALIZER = new Serializer();

	public MarimorphosisRecipe(StateIngredient input, StateIngredient output, int manaCost, int weight, int weightBonus, @Nullable TagKey<Biome> biomes) {
		super(input, output, 0, manaCost, weight, null, null, weightBonus, biomes);
	}

	private MarimorphosisRecipe(OrechidRecipe orechidRecipe) {
		this(orechidRecipe.getInput(), orechidRecipe.getOutput(), orechidRecipe.getManaCost(),
				orechidRecipe.getWeight(), orechidRecipe.getWeightBonus(),
				orechidRecipe.getBiomes().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe> getType() {
		return (RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe>) BuiltInRegistries.RECIPE_TYPE.get(MARIMORPHOSIS_TYPE_ID);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<MarimorphosisRecipe> {
		public static final MapCodec<MarimorphosisRecipe> CODEC = OrechidRecipe.SERIALIZER.codec()
				.xmap(MarimorphosisRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, MarimorphosisRecipe> STREAM_CODEC = OrechidRecipe.SERIALIZER.streamCodec()
				.map(MarimorphosisRecipe::new, Function.identity());

		@Override
		public MapCodec<MarimorphosisRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, MarimorphosisRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
