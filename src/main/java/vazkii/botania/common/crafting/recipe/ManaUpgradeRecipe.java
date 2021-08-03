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
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.IManaItem;

import javax.annotation.Nonnull;

public class ManaUpgradeRecipe implements CraftingRecipe {
	private final ShapedRecipe compose;

	public ManaUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	public static ItemStack output(ItemStack output, Container inv) {
		ItemStack out = output.copy();
		if (!(out.getItem() instanceof IManaItem)) {
			return out;
		}
		IManaItem outItem = (IManaItem) out.getItem();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IManaItem) {
					IManaItem item = (IManaItem) stack.getItem();
					outItem.addMana(out, item.getMana(stack));
				}
			}
		}
		return out;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		return output(compose.assemble(inv), inv);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
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
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<ManaUpgradeRecipe> {
		@Override
		public ManaUpgradeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ManaUpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public ManaUpgradeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new ManaUpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, ManaUpgradeRecipe recipe) {
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.compose);
		}
	};
}
