/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.mixin.ShapelessRecipeAccessor;

import java.util.function.Function;

public class LexiconReturningShapelessRecipe extends ShapelessRecipe {
	public static final WrappingRecipeSerializer<LexiconReturningShapelessRecipe> SERIALIZER = new LexiconReturningShapelessRecipe.Serializer();

	private LexiconReturningShapelessRecipe(ShapelessRecipe recipe) {
		super(recipe.getGroup(), recipe.category(), ((ShapelessRecipeAccessor) recipe).botania_getResult(), recipe.getIngredients());
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
		return ManaUpgradeRecipe.output(super.assemble(inv, registries), inv);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); i++) {
			ItemStack itemStack = input.getItem(i);
			Item item = itemStack.getItem();
			if (item.hasCraftingRemainingItem()) {
				nonnulllist.set(i, new ItemStack(item.getCraftingRemainingItem()));
			} else if (itemStack.is(BotaniaItems.lexicon)) {
				nonnulllist.set(i, itemStack.copy());
			}
		}

		return nonnulllist;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements WrappingRecipeSerializer<LexiconReturningShapelessRecipe> {
		public static final MapCodec<LexiconReturningShapelessRecipe> CODEC = SHAPELESS_RECIPE.codec()
				.xmap(LexiconReturningShapelessRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, LexiconReturningShapelessRecipe> STREAM_CODEC = SHAPELESS_RECIPE.streamCodec()
				.map(LexiconReturningShapelessRecipe::new, Function.identity());

		@Override
		public LexiconReturningShapelessRecipe wrap(Recipe<?> recipe) {
			if (!(recipe instanceof ShapelessRecipe shapelessRecipe)) {
				throw new IllegalArgumentException("Unsupported recipe type to wrap: " + recipe.getType());
			}
			return new LexiconReturningShapelessRecipe(shapelessRecipe);
		}

		@Override
		public MapCodec<LexiconReturningShapelessRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, LexiconReturningShapelessRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
