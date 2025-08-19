/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.mixin.IngredientAccessor;

import java.util.List;
import java.util.stream.Stream;

public class RunicAltarRecipe implements vazkii.botania.api.recipe.RunicAltarRecipe {
	public static final RecipeSerializer<RunicAltarRecipe> SERIALIZER = new Serializer();
	private final ItemStack output;
	private final Ingredient reagent;
	private final NonNullList<Ingredient> ingredients;
	private final NonNullList<Ingredient> catalysts;
	private final int mana;

	public RunicAltarRecipe(ItemStack output, Ingredient reagent, int mana, Ingredient[] ingredients, Ingredient[] catalysts) {
		int numIngredients = ingredients.length;
		int numCatalysts = catalysts.length;
		Preconditions.checkArgument(numIngredients + numCatalysts > 0, "Must have at least one ingredient or catalyst");
		Preconditions.checkArgument(numIngredients + numCatalysts <= 16, "Cannot have more than 16 ingredients and/or catalysts");
		validateIngredients(ingredients, catalysts, reagent);
		this.output = output;
		this.reagent = reagent;
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients);
		this.catalysts = NonNullList.of(Ingredient.EMPTY, catalysts);
		this.mana = mana;
	}

	private void validateIngredients(Ingredient[] ingredients, Ingredient[] catalysts, Ingredient reagent) {
		var ingredientItems = new ReferenceOpenHashSet<Item>(ingredients.length);
		var catalystItems = new ReferenceOpenHashSet<Item>(catalysts.length);
		var reagentItems = new ReferenceOpenHashSet<Item>(1);
		Stream.of(ingredients).flatMap(ingredient -> Stream.of(ingredient.getItems()))
				.map(ItemStack::getItem).forEach(ingredientItems::add);
		Stream.of(catalysts).flatMap(catalyst -> Stream.of(catalyst.getItems()))
				.map(ItemStack::getItem).forEach(catalystItems::add);
		Stream.of(reagent.getItems()).map(ItemStack::getItem).forEach(reagentItems::add);

		// HACK: Reset cached items lists of ingredients, because these might be empty right after world load.
		// (This also means the validation is not reliable on world load, only on datagen and datapack reload.)
		Stream.of(ingredients).forEach(ingr -> ((IngredientAccessor) ingr).botania_setItemStacks(null));
		Stream.of(catalysts).forEach(ingr -> ((IngredientAccessor) ingr).botania_setItemStacks(null));
		((IngredientAccessor) reagent).botania_setItemStacks(null);

		var ingredientsOverlap = ingredientItems.clone();
		ingredientsOverlap.retainAll(catalystItems);
		if (!ingredientsOverlap.isEmpty()) {
			throw new IllegalArgumentException("The following item types can match both as ingredient and as catalyst: " + ingredientsOverlap);
		}

		ingredientItems.retainAll(reagentItems);
		if (!ingredientItems.isEmpty()) {
			throw new IllegalArgumentException("The following item types can match both as ingredient and as reagent: " + ingredientItems);
		}

		catalystItems.retainAll(reagentItems);
		if (!catalystItems.isEmpty()) {
			throw new IllegalArgumentException("The following item types can match both as catalyst and as reagent: " + catalystItems);
		}

		// fallback check: test ingredient, catalyst and reagent definitions against each other directly
		List<Ingredient> ingredientList = List.of(ingredients);
		List<Ingredient> catalystsList = List.of(catalysts);
		if (ingredientList.contains(reagent) || catalystsList.contains(reagent)) {
			throw new IllegalArgumentException("Reagent definition must not occur in ingredients or catalysts: " + reagent);
		}
		if (Stream.of(catalysts).anyMatch(ingredientList::contains)) {
			throw new IllegalArgumentException("Ingredients and catalysts must not contain identical entries: %s/%s"
					.formatted(ingredientList, catalystsList));
		}

	}

	private static RunicAltarRecipe of(List<Ingredient> ingredients, List<Ingredient> catalysts, Ingredient reagent, int mana, ItemStack output) {
		return new RunicAltarRecipe(output, reagent, mana, ingredients.toArray(Ingredient[]::new), catalysts.toArray(Ingredient[]::new));
	}

	@Override
	public boolean matches(RecipeInput container, Level world) {
		return RecipeUtils.matches(ingredients, catalysts, container, null, null);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(RecipeInput container) {
		IntSet matchedCatalystSlots = new IntOpenHashSet(catalysts.size());
		RecipeUtils.matches(ingredients, catalysts, container, null, matchedCatalystSlots);
		NonNullList<ItemStack> foundCatalysts = NonNullList.createWithCapacity(matchedCatalystSlots.size());

		for (int slot : matchedCatalystSlots) {
			foundCatalysts.add(container.getItem(slot));
		}

		return foundCatalysts;
	}

	@Override
	public final ItemStack getResultItem(HolderLookup.Provider registries) {
		return output;
	}

	@Override
	public ItemStack assemble(RecipeInput inv, HolderLookup.Provider registries) {
		return getResultItem(registries).copy();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public NonNullList<Ingredient> getCatalysts() {
		return catalysts;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.runeAltar);
	}

	@Override
	public RecipeSerializer<? extends RunicAltarRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public int getMana() {
		return mana;
	}

	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Ingredient getReagent() {
		return reagent;
	}

	public static class Serializer implements RecipeSerializer<RunicAltarRecipe> {
		private static final MapCodec<RunicAltarRecipe> RAW_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(RunicAltarRecipe::getIngredients),
				Ingredient.CODEC_NONEMPTY.listOf().fieldOf("catalysts").forGetter(RunicAltarRecipe::getCatalysts),
				Ingredient.CODEC_NONEMPTY.fieldOf("reagent").forGetter(RunicAltarRecipe::getReagent),
				ExtraCodecs.POSITIVE_INT.fieldOf("mana").forGetter(RunicAltarRecipe::getMana),
				ItemStack.STRICT_CODEC.fieldOf("output").forGetter(RunicAltarRecipe::getOutput)
		).apply(instance, RunicAltarRecipe::of));
		public static final MapCodec<RunicAltarRecipe> CODEC = RAW_CODEC.validate(recipe -> {
			if (recipe.getIngredients().size() + recipe.getCatalysts().size() == 0) {
				return DataResult.error(() -> "Must have at least one ingredient or catalyst");
			}
			if (recipe.getIngredients().size() + recipe.getCatalysts().size() > 16) {
				return DataResult.error(() -> "Cannot have more than 16 ingredients and catalysts in total");
			}
			return DataResult.success(recipe);
		});
		public static final StreamCodec<RegistryFriendlyByteBuf, RunicAltarRecipe> STREAM_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RunicAltarRecipe::getIngredients,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RunicAltarRecipe::getCatalysts,
				Ingredient.CONTENTS_STREAM_CODEC, RunicAltarRecipe::getReagent,
				ByteBufCodecs.VAR_INT, RunicAltarRecipe::getMana,
				ItemStack.STREAM_CODEC, RunicAltarRecipe::getOutput,
				RunicAltarRecipe::of
		);

		@Override
		public MapCodec<RunicAltarRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RunicAltarRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
