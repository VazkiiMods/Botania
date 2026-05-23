/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Optional;

public class ElvenTradeRecipe implements vazkii.botania.api.recipe.ElvenTradeRecipe {
	public static final RecipeSerializer<ElvenTradeRecipe> SERIALIZER = new Serializer();
	private final ImmutableList<ItemStack> outputs;
	private final NonNullList<Ingredient> ingredients;

	public ElvenTradeRecipe(ItemStack[] outputs, Ingredient... ingredients) {
		this.outputs = ImmutableList.copyOf(outputs);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients);
		validateIngredients();
	}

	public ElvenTradeRecipe(List<ItemStack> outputs, List<Ingredient> ingredients) {
		this.outputs = ImmutableList.copyOf(outputs);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		validateIngredients();
	}

	private void validateIngredients() {
		// Fabric doesn't provide a way to access ingredient->input mappings for their custom matcher,
		// so by way of collective punishment, Neoforge doesn't get to use that type of ingredient either for now.
		// TODO (maybe, eventually): submit PR to Fabric API, then come back to look at this and RunicAltarRecipe
		var customTestIngredients = ingredients.stream()
				.filter(XplatAbstractions.instance()::requiresCustomTesting).toList();
		if (!customTestIngredients.isEmpty()) {
			throw new IllegalArgumentException(
					"Ingredients with custom test implementations are not supported: " + customTestIngredients);
		}
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.alfPortal);
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public Optional<AssemblyResult> tryAssemble(ProcessingRecipeInput input, HolderLookup.Provider registries) {
		if (ingredients.size() == 1) {
			// single ingredient, map to first matching slot
			Ingredient ingredient = ingredients.getFirst();
			for (int slot = 0; slot < input.size(); slot++) {
				if (ingredient.test(input.getItem(slot))) {
					return Optional.of(new AssemblyResult(getOutputs(), slot));
				}
			}
		} else {
			IntList matchedIngredientItemTypes = new IntArrayList(ingredients.size());
			if (input.getStackedContents().canCraft(this, matchedIngredientItemTypes)) {
				Int2IntMap matchedInputSlots = new Int2IntArrayMap(ingredients.size());
				// TODO: figure out how to further optimize this
				for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
					Item inputItem = Item.byId(matchedIngredientItemTypes.getInt(ingredientIndex));
					for (int inputIndex = 0; inputIndex < input.size(); inputIndex++) {
						if (input.getItem(inputIndex).is(inputItem)) {
							int alreadyAssigned = matchedInputSlots.get(inputIndex);
							if (alreadyAssigned < input.getItem(inputIndex).getCount()) {
								matchedInputSlots.put(inputIndex, alreadyAssigned + 1);
								break;
							}
						}
					}
				}
				return Optional.of(new AssemblyResult(getOutputs(), matchedInputSlots));
			}
		}
		return Optional.empty();
	}

	public static class Serializer implements RecipeSerializer<ElvenTradeRecipe> {
		public static final MapCodec<ElvenTradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ExtraCodecs.nonEmptyList(ItemStack.STRICT_SINGLE_ITEM_CODEC.listOf()).fieldOf("output").forGetter(ElvenTradeRecipe::getOutputs),
				ExtraCodecs.nonEmptyList(Ingredient.CODEC_NONEMPTY.listOf()).fieldOf("ingredients").forGetter(ElvenTradeRecipe::getIngredients)
		).apply(instance, ElvenTradeRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, ElvenTradeRecipe> STREAM_CODEC = StreamCodec.composite(
				ItemStack.LIST_STREAM_CODEC, ElvenTradeRecipe::getOutputs,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), ElvenTradeRecipe::getIngredients,
				ElvenTradeRecipe::new
		);

		@Override
		public MapCodec<ElvenTradeRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ElvenTradeRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
