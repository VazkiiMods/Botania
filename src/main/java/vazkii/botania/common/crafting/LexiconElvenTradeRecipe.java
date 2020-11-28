/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LexiconElvenTradeRecipe implements IElvenTradeRecipe {
	private final Identifier id;

	LexiconElvenTradeRecipe(Identifier id) {
		this.id = id;
	}

	@Override
	public boolean containsItem(ItemStack stack) {
		return stack.getItem() == ModItems.lexicon && !ItemNBTHelper.getBoolean(stack, ItemLexicon.TAG_ELVEN_UNLOCK, false);
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.ofSize(1, Ingredient.ofItems(ModItems.lexicon));
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.alfPortal);
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public List<ItemStack> getOutputs() {
		ItemStack stack = new ItemStack(ModItems.lexicon);
		stack.getOrCreateTag().putBoolean(ItemLexicon.TAG_ELVEN_UNLOCK, true);
		return Collections.singletonList(stack);
	}

	@Override
	public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
		if (stacks.size() == 1 && stacks.get(0).getItem() == ModItems.lexicon && !ItemNBTHelper.getBoolean(stacks.get(0), ItemLexicon.TAG_ELVEN_UNLOCK, false)) {
			return Optional.of(new ArrayList<>(stacks));
		}
		return Optional.empty();
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		ItemStack stack = inputs.get(0).copy();
		stack.getOrCreateTag().putBoolean(ItemLexicon.TAG_ELVEN_UNLOCK, true);
		return Collections.singletonList(stack);
	}

	@Nonnull
	@Override
	public RecipeSerializer<LexiconElvenTradeRecipe> getSerializer() {
		return ModRecipeTypes.LEXICON_ELVEN_TRADE_SERIALIZER;
	}
}
