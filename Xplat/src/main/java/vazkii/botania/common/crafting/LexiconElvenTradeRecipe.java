/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.api.recipe.ElvenTradeRecipe;
import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.item.BotaniaItems;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LexiconElvenTradeRecipe implements ElvenTradeRecipe {
	public static final LexiconElvenTradeRecipe INSTANCE = new LexiconElvenTradeRecipe();
	public static final RecipeSerializer<LexiconElvenTradeRecipe> SERIALIZER = new Serializer();
	public static final Supplier<Ingredient> LEXICON_INGREDIENT = Suppliers.memoize(() -> Ingredient.of(BotaniaItems.lexicon));

	private LexiconElvenTradeRecipe() {}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, LEXICON_INGREDIENT.get());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.ELVEN_GATEWAY_CORE);
	}

	@Override
	public List<ItemStack> getOutputs() {
		return List.of(getUpgradedLexicon(new ItemStack(BotaniaItems.lexicon)));
	}

	private static ItemStack getUpgradedLexicon(ItemStack stack) {
		stack.set(BotaniaDataComponents.ELVEN_UNLOCK, Unit.INSTANCE);
		stack.set(DataComponents.RARITY, Rarity.UNCOMMON);
		return stack;
	}

	@Override
	public Optional<AssemblyResult> tryAssemble(ProcessingRecipeInput input,
			HolderLookup.Provider registries) {
		var lexiconIngredient = LEXICON_INGREDIENT.get();
		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack inputItem = input.getItem(slot);
			if (lexiconIngredient.test(inputItem)) {
				return Optional.of(new AssemblyResult(getUpgradedLexicon(inputItem.copy()), slot));
			}
		}
		return Optional.empty();
	}

	@Override
	public RecipeSerializer<LexiconElvenTradeRecipe> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<LexiconElvenTradeRecipe> {
		@Override
		public MapCodec<LexiconElvenTradeRecipe> codec() {
			return MapCodec.unit(() -> LexiconElvenTradeRecipe.INSTANCE);
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, LexiconElvenTradeRecipe> streamCodec() {
			return StreamCodec.unit(LexiconElvenTradeRecipe.INSTANCE);
		}
	}
}
