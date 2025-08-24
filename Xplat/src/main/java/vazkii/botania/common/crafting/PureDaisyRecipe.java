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
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Optional;

public class PureDaisyRecipe extends BlockStateRecipe implements vazkii.botania.api.recipe.PureDaisyRecipe {

	public static final int DEFAULT_TIME = 150;
	public static final RecipeSerializer<PureDaisyRecipe> SERIALIZER = new Serializer();

	private final int time;
	private final boolean copyInputProperties;

	public PureDaisyRecipe(StateIngredient input, StateIngredient output, int time, boolean copyInputProperties,
			@Nullable CacheableFunction preUpdateFunction, @Nullable CacheableFunction successFunction) {
		super(input, output, preUpdateFunction, successFunction);
		this.time = time;
		this.copyInputProperties = copyInputProperties;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static PureDaisyRecipe of(StateIngredient input, StateIngredient output, int time,
			boolean copyInputProperties, Optional<CacheableFunction> preUpdateFunction, Optional<CacheableFunction> successFunction) {
		return new PureDaisyRecipe(input, output, time, copyInputProperties, preUpdateFunction.orElse(null), successFunction.orElse(null));
	}

	@Override
	public boolean matches(Level world, BlockPos pos, BlockState state) {
		return getInput().test(state);
	}

	@Override
	public boolean isCopyInputProperties() {
		return copyInputProperties;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public RecipeSerializer<? extends PureDaisyRecipe> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<PureDaisyRecipe> {
		public static final MapCodec<PureDaisyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredients.TYPED_CODEC.fieldOf("input").forGetter(PureDaisyRecipe::getInput),
				StateIngredients.TYPED_CODEC.fieldOf("output").forGetter(PureDaisyRecipe::getOutput),
				ExtraCodecs.POSITIVE_INT.optionalFieldOf("time", 0).forGetter(PureDaisyRecipe::getTime),
				Codec.BOOL.optionalFieldOf("copy_properties", false).forGetter(PureDaisyRecipe::isCopyInputProperties),
				CacheableFunction.CODEC.optionalFieldOf("pre_update_function").forGetter(PureDaisyRecipe::getPreUpdateFunction),
				CacheableFunction.CODEC.optionalFieldOf("success_function").forGetter(PureDaisyRecipe::getSuccessFunction)
		).apply(instance, PureDaisyRecipe::of));
		public static final StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> STREAM_CODEC = StreamCodec.composite(
				StateIngredients.TYPED_STREAM_CODEC, PureDaisyRecipe::getInput,
				StateIngredients.TYPED_STREAM_CODEC, PureDaisyRecipe::getOutput,
				ByteBufCodecs.VAR_INT, PureDaisyRecipe::getTime,
				ByteBufCodecs.BOOL, PureDaisyRecipe::isCopyInputProperties,
				(input, output, time, copyInputProperties) -> new PureDaisyRecipe(input, output, time, copyInputProperties, null, null)
		);

		@Override
		public MapCodec<PureDaisyRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
