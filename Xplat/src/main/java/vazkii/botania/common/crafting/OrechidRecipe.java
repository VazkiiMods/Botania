/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Optional;

import io.netty.buffer.ByteBuf;

public class OrechidRecipe extends BlockStateRecipe implements vazkii.botania.api.recipe.OrechidRecipe {
	public static final RecipeSerializer<OrechidRecipe> SERIALIZER = new Serializer();
	private final int cooldown;
	private final int manaCost;
	private final int weight;
	private final int weightBonus;
	@Nullable
	private final TagKey<Biome> biomes;

	public OrechidRecipe(StateIngredient input, StateIngredient output, int cooldown, int manaCost, int weight) {
		this(input, output, cooldown, manaCost, weight, null, null, 0, null);
	}

	public OrechidRecipe(StateIngredient input, StateIngredient output, int cooldown, int manaCost, int weight,
			@Nullable CacheableFunction preUpdateFunction, @Nullable CacheableFunction successFunction,
			int weightBonus, @Nullable TagKey<Biome> biomes) {
		super(input, output, preUpdateFunction, successFunction);
		this.weight = weight;
		this.weightBonus = weightBonus;
		this.cooldown = cooldown;
		this.manaCost = manaCost;
		this.biomes = biomes;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static OrechidRecipe of(StateIngredient input, StateIngredient output, int delay, int manaCost, int weight,
			int biomeWeight, Optional<TagKey<Biome>> biomeTag, Optional<CacheableFunction> preUpdateFunction,
			Optional<CacheableFunction> successFunction) {
		return new OrechidRecipe(input, output, delay, manaCost, weight, preUpdateFunction.orElse(null),
				successFunction.orElse(null), biomeWeight, biomeTag.orElse(null));
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public int getManaCost() {
		return manaCost;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getWeight(Level level, BlockPos pos) {
		return this.biomes != null && level.getBiome(pos).is(this.biomes)
				? getWeight() + getWeightBonus()
				: getWeight();
	}

	public int getWeightBonus() {
		return weightBonus;
	}

	public Optional<TagKey<Biome>> getBiomes() {
		return Optional.ofNullable(biomes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe> getType() {
		return (RecipeType<? extends vazkii.botania.api.recipe.OrechidRecipe>) BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<OrechidRecipe> {
		private static final MapCodec<OrechidRecipe> RAW_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredients.TYPED_CODEC.fieldOf("input").forGetter(OrechidRecipe::getInput),
				StateIngredients.TYPED_CODEC.fieldOf("output").forGetter(OrechidRecipe::getOutput),
				ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("cooldown", 0).forGetter(OrechidRecipe::getCooldown),
				ExtraCodecs.POSITIVE_INT.fieldOf("mana").forGetter(OrechidRecipe::getManaCost),
				ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 0).forGetter(OrechidRecipe::getWeight),
				Codec.INT.optionalFieldOf("biome_bonus_weight", 0).forGetter(OrechidRecipe::getWeightBonus),
				TagKey.codec(Registries.BIOME).optionalFieldOf("biome_bonus_tag").forGetter(OrechidRecipe::getBiomes),
				CacheableFunction.CODEC.optionalFieldOf("pre_update_function").forGetter(OrechidRecipe::getPreUpdateFunction),
				CacheableFunction.CODEC.optionalFieldOf("success_function").forGetter(OrechidRecipe::getSuccessFunction)
		).apply(instance, OrechidRecipe::of));
		public static final MapCodec<OrechidRecipe> CODEC = RAW_CODEC.validate(orechidRecipe -> {
			if (orechidRecipe.getWeight() == 0 && orechidRecipe.getWeightBonus() == 0) {
				return DataResult.error(() -> "Weight and bonus weight cannot both be 0");
			}
			if (orechidRecipe.getWeight() + orechidRecipe.getWeightBonus() < 0) {
				return DataResult.error(() -> "Weight combined with bonus cannot be less than 0");
			}
			return DataResult.success(orechidRecipe);
		});
		public static final StreamCodec<RegistryFriendlyByteBuf, OrechidRecipe> STREAM_CODEC = new StreamCodec<>() {

			private static final StreamCodec<ByteBuf, Optional<TagKey<Biome>>> BIOMES_CODEC = ByteBufCodecs.optional(
					ResourceLocation.STREAM_CODEC.map(
							id -> TagKey.create(Registries.BIOME, id),
							TagKey::location
					));

			@Override
			public OrechidRecipe decode(RegistryFriendlyByteBuf buffer) {
				return new OrechidRecipe(
						StateIngredients.TYPED_STREAM_CODEC.decode(buffer),
						StateIngredients.TYPED_STREAM_CODEC.decode(buffer),
						ByteBufCodecs.VAR_INT.decode(buffer),
						ByteBufCodecs.VAR_INT.decode(buffer),
						ByteBufCodecs.VAR_INT.decode(buffer),
						null,
						null,
						ByteBufCodecs.VAR_INT.decode(buffer),
						BIOMES_CODEC.decode(buffer).orElse(null)
				);
			}

			@Override
			public void encode(RegistryFriendlyByteBuf buffer, OrechidRecipe recipe) {
				StateIngredients.TYPED_STREAM_CODEC.encode(buffer, recipe.getInput());
				StateIngredients.TYPED_STREAM_CODEC.encode(buffer, recipe.getOutput());
				ByteBufCodecs.VAR_INT.encode(buffer, recipe.getCooldown());
				ByteBufCodecs.VAR_INT.encode(buffer, recipe.getManaCost());
				ByteBufCodecs.VAR_INT.encode(buffer, recipe.getWeight());
				ByteBufCodecs.VAR_INT.encode(buffer, recipe.getWeightBonus());
				BIOMES_CODEC.encode(buffer, recipe.getBiomes());
			}
		};

		@Override
		public MapCodec<OrechidRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, OrechidRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
