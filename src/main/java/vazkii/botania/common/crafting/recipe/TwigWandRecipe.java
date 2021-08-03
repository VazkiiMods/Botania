/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.material.ItemPetal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TwigWandRecipe implements CraftingRecipe {
	public static final RecipeSerializer<TwigWandRecipe> SERIALIZER = new Serializer();
	private final ShapedRecipe compose;

	public TwigWandRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level worldIn) {
		return compose.matches(inv, worldIn);
	}

	@Nonnull
	@Override
	public ItemStack assemble(CraftingContainer inv) {
		int first = -1;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();

			int colorId;
			if (item instanceof ItemPetal) {
				colorId = ((ItemPetal) item).color.getId();
			} else if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof BlockModMushroom) {
				colorId = ((BlockModMushroom) ((BlockItem) item).getBlock()).color.getId();
			} else {
				continue;
			}
			if (first == -1) {
				first = colorId;
			} else {
				return ItemTwigWand.forColors(first, colorId);
			}
		}
		return ItemTwigWand.forColors(first != -1 ? first : 0, 0);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return compose.canCraftInDimensions(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return new ItemStack(ModItems.twigWand);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		return compose.getRemainingItems(inv);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Nonnull
	@Override
	public String getGroup() {
		return compose.getGroup();
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return compose.getToastSymbol();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements RecipeSerializer<TwigWandRecipe> {
		@Nonnull
		@Override
		public TwigWandRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new TwigWandRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Nullable
		@Override
		public TwigWandRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new TwigWandRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull TwigWandRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe.compose);
		}
	}
}
