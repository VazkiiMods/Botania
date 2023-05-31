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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeSerializerBase;

public class ArmorUpgradeRecipe extends ShapedRecipe {
	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		super(compose.getId(), compose.getGroup(), CraftingBookCategory.EQUIPMENT, compose.getWidth(), compose.getHeight(), compose.getIngredients(), compose.getResultItem());
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingContainer inv) {
		ItemStack out = super.assemble(inv);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && stack.hasTag()) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), out);
				break;
			}
		}
		return out;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer extends RecipeSerializerBase<ArmorUpgradeRecipe> {
		@Override
		public ArmorUpgradeRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			return new ArmorUpgradeRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public ArmorUpgradeRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			return new ArmorUpgradeRecipe(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ArmorUpgradeRecipe recipe) {
			SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
