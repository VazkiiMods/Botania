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

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.mixin.ShapelessRecipeAccessor;

import java.util.function.Function;

public class ShapelessUncoverSpreaderRecipe extends ShapelessRecipe {
	public static final WrappingRecipeSerializer<ShapelessUncoverSpreaderRecipe> SERIALIZER = new Serializer();

	private ShapelessUncoverSpreaderRecipe(ShapelessRecipe recipe) {
		super(recipe.getGroup(), recipe.category(), ((ShapelessRecipeAccessor) recipe).botania_getResult(), recipe.getIngredients());
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); i++) {
			Item item = input.getItem(i).getItem();
			if (item instanceof BlockItem blockItem
					&& blockItem.getBlock() instanceof ManaSpreaderBlock manaSpreaderBlock
					&& manaSpreaderBlock.getOptionalColor().isPresent()) {
				nonnulllist.set(i, new ItemStack(ColorHelper.WOOL_MAP.apply(manaSpreaderBlock.getOptionalColor().get())));
			}
		}

		return nonnulllist;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements WrappingRecipeSerializer<ShapelessUncoverSpreaderRecipe> {
		public static final MapCodec<ShapelessUncoverSpreaderRecipe> CODEC = SHAPELESS_RECIPE.codec()
				.xmap(ShapelessUncoverSpreaderRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessUncoverSpreaderRecipe> STREAM_CODEC = SHAPELESS_RECIPE.streamCodec()
				.map(ShapelessUncoverSpreaderRecipe::new, Function.identity());

		@Override
		public ShapelessUncoverSpreaderRecipe wrap(Recipe<?> recipe) {
			if (!(recipe instanceof ShapelessRecipe shapelessRecipe)) {
				throw new IllegalArgumentException("Unsupported recipe type to wrap: " + recipe.getType());
			}
			return new ShapelessUncoverSpreaderRecipe(shapelessRecipe);
		}

		@Override
		public MapCodec<ShapelessUncoverSpreaderRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessUncoverSpreaderRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
