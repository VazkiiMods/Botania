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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ArmorUpgradeRecipe implements CraftingRecipe {
	private final ShapedRecipe compose;

	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack out = compose.assemble(inv);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && stack.hasTag()) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), out);
				break;
			}
		}
		return out;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return compose.canCraftInDimensions(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return compose.getResultItem();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<ArmorUpgradeRecipe> {
		@Override
		public ArmorUpgradeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ArmorUpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public ArmorUpgradeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new ArmorUpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ArmorUpgradeRecipe recipe) {
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.compose);
		}
	};
}
