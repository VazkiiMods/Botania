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
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.item.BotaniaItems;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LexiconElvenTradeRecipe implements ElvenTradeRecipe {
	public static final LexiconElvenTradeRecipe INSTANCE = new LexiconElvenTradeRecipe();
	public static final RecipeSerializer<LexiconElvenTradeRecipe> SERIALIZER = new Serializer();

	private LexiconElvenTradeRecipe() {}

	@Override
	public boolean containsItem(ItemStack stack) {
		return stack.is(BotaniaItems.lexicon) && !stack.has(BotaniaDataComponents.ELVEN_UNLOCK);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.withSize(1, Ingredient.of(BotaniaItems.lexicon));
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.alfPortal);
	}

	@Override
	public List<ItemStack> getOutputs() {
		ItemStack stack = new ItemStack(BotaniaItems.lexicon);
		stack.set(BotaniaDataComponents.ELVEN_UNLOCK, Unit.INSTANCE);
		stack.set(DataComponents.RARITY, Rarity.UNCOMMON);
		return Collections.singletonList(stack);
	}

	@Override
	public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			if (containsItem(stack)) {
				return Optional.of(Collections.singletonList(stack));
			}
		}
		return Optional.empty();
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		ItemStack stack = inputs.getFirst().copy();
		stack.set(BotaniaDataComponents.ELVEN_UNLOCK, Unit.INSTANCE);
		return Collections.singletonList(stack);
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
