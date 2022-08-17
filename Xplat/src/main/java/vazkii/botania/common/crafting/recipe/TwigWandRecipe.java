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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.crafting.RecipeSerializerBase;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.material.ItemPetal;

public class TwigWandRecipe extends ShapedRecipe {
	public static final RecipeSerializer<TwigWandRecipe> SERIALIZER = new Serializer();

	public TwigWandRecipe(ShapedRecipe compose) {
		super(compose.getId(), compose.getGroup(), compose.getWidth(), compose.getHeight(), compose.getIngredients(), compose.getResultItem());
	}

	@NotNull
	@Override
	public ItemStack assemble(CraftingContainer inv) {
		int first = -1;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();

			int colorId;
			if (item instanceof ItemPetal petal) {
				colorId = petal.color.getId();
			} else if (item instanceof BlockItem block && block.getBlock() instanceof BlockModMushroom mushroom) {
				colorId = mushroom.color.getId();
			} else {
				continue;
			}
			if (first == -1) {
				first = colorId;
			} else {
				return ItemTwigWand.setColors(getResultItem().copy(), first, colorId);
			}
		}
		return ItemTwigWand.setColors(getResultItem().copy(), first != -1 ? first : 0, 0);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer extends RecipeSerializerBase<TwigWandRecipe> {
		@NotNull
		@Override
		public TwigWandRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			return new TwigWandRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@NotNull
		@Override
		public TwigWandRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			return new TwigWandRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull TwigWandRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
