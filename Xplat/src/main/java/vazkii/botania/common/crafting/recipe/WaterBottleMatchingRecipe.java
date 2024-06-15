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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

public class WaterBottleMatchingRecipe extends ShapedRecipe {
	public static final RecipeSerializer<WaterBottleMatchingRecipe> SERIALIZER = new Serializer();

	public WaterBottleMatchingRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
		super(id, group, category, width, height, NonNullList.of(Ingredient.EMPTY, recipeItems.stream().map(i -> {
			if (i.test(new ItemStack(Items.POTION))) {
				return Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			}
			return i;
		}).toArray(Ingredient[]::new)), result);
	}

	public WaterBottleMatchingRecipe(ShapedRecipe recipe) {
		this(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(),
				recipe.getIngredients(),
				// XXX: Hacky, but compose should always be a vanilla shaped recipe which doesn't do anything with the
				// RegistryAccess
				recipe.getResultItem(RegistryAccess.EMPTY));
	}

	@Override
	public boolean matches(@NotNull CraftingContainer craftingContainer, @NotNull Level level) {
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

	private static class Serializer implements RecipeSerializer<WaterBottleMatchingRecipe> {
		@Override
		public WaterBottleMatchingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			return new WaterBottleMatchingRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public WaterBottleMatchingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			return new WaterBottleMatchingRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull WaterBottleMatchingRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
