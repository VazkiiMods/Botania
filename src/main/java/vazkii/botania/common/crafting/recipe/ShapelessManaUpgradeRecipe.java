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
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ShapelessManaUpgradeRecipe implements CraftingRecipe {
	private final ShapelessRecipe compose;

	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		return ManaUpgradeRecipe.output(compose.assemble(inv), inv);
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

	public static final RecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<ShapelessManaUpgradeRecipe> {
		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(recipeId, json));
		}

		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ShapelessManaUpgradeRecipe recipe) {
			RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe.compose);
		}
	}
}
