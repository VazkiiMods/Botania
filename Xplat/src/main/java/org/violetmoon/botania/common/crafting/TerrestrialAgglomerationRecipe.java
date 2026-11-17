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
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.List;

public class TerrestrialAgglomerationRecipe implements vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe {
	public static final RecipeSerializer<TerrestrialAgglomerationRecipe> SERIALIZER = new Serializer();
	private final int mana;
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final boolean requireSpecialMatching;

	public TerrestrialAgglomerationRecipe(int mana, ItemStack output, Ingredient... ingredients) {
		this.mana = mana;
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients);
		this.output = output;
		this.requireSpecialMatching = Arrays.stream(ingredients).anyMatch(XplatAbstractions.instance()::requiresCustomTesting);
	}

	private static TerrestrialAgglomerationRecipe of(List<Ingredient> ingredients, int mana, ItemStack output) {
		return new TerrestrialAgglomerationRecipe(mana, output, ingredients.toArray(Ingredient[]::new));
	}

	@Override
	public int getMana() {
		return mana;
	}

	public ItemStack getOutput() {
		return output;
	}

	@Override
	public boolean matches(ProcessingRecipeInput input, Level world) {
		return RecipeUtils.matches(this, input, requireSpecialMatching);
	}

	@Override
	public ItemStack assemble(ProcessingRecipeInput inv, HolderLookup.Provider registries) {
		return output.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public RecipeSerializer<? extends TerrestrialAgglomerationRecipe> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<TerrestrialAgglomerationRecipe> {
		public static final MapCodec<TerrestrialAgglomerationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ExtraCodecs.nonEmptyList(Ingredient.CODEC_NONEMPTY.listOf()).fieldOf("ingredients")
						.forGetter(TerrestrialAgglomerationRecipe::getIngredients),
				ExtraCodecs.POSITIVE_INT.fieldOf("mana").forGetter(TerrestrialAgglomerationRecipe::getMana),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(TerrestrialAgglomerationRecipe::getOutput)
		).apply(instance, TerrestrialAgglomerationRecipe::of));
		public static final StreamCodec<RegistryFriendlyByteBuf, TerrestrialAgglomerationRecipe> STREAM_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), TerrestrialAgglomerationRecipe::getIngredients,
				ByteBufCodecs.VAR_INT, TerrestrialAgglomerationRecipe::getMana,
				ItemStack.STREAM_CODEC, TerrestrialAgglomerationRecipe::getOutput,
				TerrestrialAgglomerationRecipe::of
		);

		@Override
		public MapCodec<TerrestrialAgglomerationRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TerrestrialAgglomerationRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
