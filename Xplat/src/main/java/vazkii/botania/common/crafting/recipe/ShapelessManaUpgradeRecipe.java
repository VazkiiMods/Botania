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

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.NotNull;

public class ShapelessManaUpgradeRecipe extends ShapelessRecipe {
	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		super(compose.getId(), compose.getGroup(), CraftingBookCategory.EQUIPMENT,
				// XXX: Hacky, but compose should always be a vanilla shaped recipe which doesn't do anything with the
				// RegistryAccess
				compose.getResultItem(RegistryAccess.EMPTY),
				compose.getIngredients());
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registries) {
		return ManaUpgradeRecipe.output(super.assemble(inv, registries), inv);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer implements RecipeSerializer<ShapelessManaUpgradeRecipe> {
		@NotNull
		@Override
		public ShapelessManaUpgradeRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
			return new ShapelessManaUpgradeRecipe(SHAPELESS_RECIPE.fromJson(recipeId, json));
		}

		@NotNull
		@Override
		public ShapelessManaUpgradeRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
			return new ShapelessManaUpgradeRecipe(SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ShapelessManaUpgradeRecipe recipe) {
			SHAPELESS_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
