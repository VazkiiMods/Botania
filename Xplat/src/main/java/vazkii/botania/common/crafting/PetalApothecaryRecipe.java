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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.List;

public class PetalApothecaryRecipe implements vazkii.botania.api.recipe.PetalApothecaryRecipe {
	public static final RecipeSerializer<PetalApothecaryRecipe> SERIALIZER = new Serializer();
	private final ItemStack output;
	private final Ingredient reagent;
	private final NonNullList<Ingredient> ingredients;
	private final boolean requireSpecialMatching;

	public PetalApothecaryRecipe(ItemStack output, Ingredient reagent, Ingredient... ingredients) {
		this.output = output;
		this.reagent = reagent;
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients);
		this.requireSpecialMatching = Arrays.stream(ingredients).anyMatch(XplatAbstractions.instance()::requiresCustomTesting);
	}

	private static PetalApothecaryRecipe of(ItemStack output, Ingredient reagent, List<Ingredient> ingredients) {
		return new PetalApothecaryRecipe(output, reagent, ingredients.toArray(Ingredient[]::new));
	}

	@Override
	public boolean matches(ProcessingRecipeInput input, Level level) {
		return RecipeUtils.matches(this, input, requireSpecialMatching);
	}

	@Override
	public final ItemStack getResultItem(HolderLookup.Provider registries) {
		return output;
	}

	@Override
	public ItemStack assemble(ProcessingRecipeInput inv, HolderLookup.Provider registries) {
		return getResultItem(registries).copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= ingredients.size();
	}

	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Ingredient getReagent() {
		return reagent;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.defaultAltar);
	}

	@Override
	public RecipeSerializer<? extends PetalApothecaryRecipe> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<PetalApothecaryRecipe> {
		public final MapCodec<PetalApothecaryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ItemStack.STRICT_CODEC.fieldOf("output").forGetter(PetalApothecaryRecipe::getOutput),
				Ingredient.CODEC_NONEMPTY.fieldOf("reagent").forGetter(PetalApothecaryRecipe::getReagent),
				Ingredient.CODEC_NONEMPTY.listOf(1, 16).fieldOf("ingredients").forGetter(PetalApothecaryRecipe::getIngredients)
		).apply(instance, PetalApothecaryRecipe::of));
		public static final StreamCodec<RegistryFriendlyByteBuf, PetalApothecaryRecipe> STREAM_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC, PetalApothecaryRecipe::getOutput,
				Ingredient.CONTENTS_STREAM_CODEC, PetalApothecaryRecipe::getReagent,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), PetalApothecaryRecipe::getIngredients,
				PetalApothecaryRecipe::of
		);

		@Override
		public MapCodec<PetalApothecaryRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PetalApothecaryRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
