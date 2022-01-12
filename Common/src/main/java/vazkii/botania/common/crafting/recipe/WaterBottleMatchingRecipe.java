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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import vazkii.botania.common.crafting.RecipeSerializerBase;

import javax.annotation.Nonnull;

public class WaterBottleMatchingRecipe extends ShapedRecipe {
	public static final RecipeSerializer<WaterBottleMatchingRecipe> SERIALIZER = new Serializer();

	public WaterBottleMatchingRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
		super(id, group, width, height, NonNullList.of(Ingredient.EMPTY, recipeItems.stream().map(i -> {
			if (i.test(new ItemStack(Items.POTION))) {
				return Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			}
			return i;
		}).toArray(Ingredient[]::new)), result);
	}

	private WaterBottleMatchingRecipe(ShapedRecipe recipe) {
		this(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem());
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer craftingContainer, @Nonnull Level level) {
		if (!super.matches(craftingContainer, level)) {
			return false;
		}
		for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
			var item = craftingContainer.getItem(i);
			if (item.is(Items.POTION) && !(PotionUtils.getPotion(item) == Potions.WATER)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer extends RecipeSerializerBase<WaterBottleMatchingRecipe> {
		@Override
		public WaterBottleMatchingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new WaterBottleMatchingRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public WaterBottleMatchingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new WaterBottleMatchingRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull WaterBottleMatchingRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
