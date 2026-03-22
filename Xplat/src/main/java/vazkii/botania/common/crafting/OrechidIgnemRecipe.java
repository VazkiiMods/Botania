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

import net.minecraft.commands.CacheableFunction;
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

public class OrechidIgnemRecipe extends OrechidRecipe {
	public static final RecipeSerializer<OrechidIgnemRecipe> SERIALIZER = new Serializer();

	public OrechidIgnemRecipe(StateIngredient input, StateIngredient output, int cooldown, int manaCost, int weight,
			@Nullable CacheableFunction preUpdateFunction, @Nullable CacheableFunction successFunction,
			int weightBonus, @Nullable TagKey<Biome> biomes) {
		super(input, output, cooldown, manaCost, weight, preUpdateFunction, successFunction, weightBonus, biomes);
	}

	public OrechidIgnemRecipe(StateIngredient input, StateIngredient output, int delay, int manaCost, int weight) {
		this(input, output, delay, manaCost, weight, null, null, 0, null);
	}

	private OrechidIgnemRecipe(OrechidRecipe orechidRecipe) {
		this(orechidRecipe.getInput(), orechidRecipe.getOutput(), orechidRecipe.getCooldown(), orechidRecipe.getManaCost(),
				orechidRecipe.getWeight(), orechidRecipe.getPreUpdateFunction().orElse(null),
				orechidRecipe.getSuccessFunction().orElse(null), orechidRecipe.getWeightBonus(),
				orechidRecipe.getBiomes().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe> getType() {
		return (RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe>) BuiltInRegistries.RECIPE_TYPE.get(IGNEM_TYPE_ID);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<OrechidIgnemRecipe> {
		public static final MapCodec<OrechidIgnemRecipe> CODEC = OrechidRecipe.SERIALIZER.codec()
				.xmap(OrechidIgnemRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, OrechidIgnemRecipe> STREAM_CODEC = OrechidRecipe.SERIALIZER.streamCodec()
				.map(OrechidIgnemRecipe::new, Function.identity());

		@Override
		public MapCodec<OrechidIgnemRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, OrechidIgnemRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
