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

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShapelessManaUpgradeRecipe implements CraftingRecipe {
	private final ShapelessRecipe compose;

	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		return ManaUpgradeRecipe.output(compose.craft(inv), inv);
	}

	@Override
	public boolean fits(int width, int height) {
		return compose.fits(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return compose.getOutput();
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return compose.getPreviewInputs();
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShapelessManaUpgradeRecipe> {
		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializer.SHAPELESS.read(recipeId, json));
		}

		@Nonnull
		@Override
		public ShapelessManaUpgradeRecipe read(@Nonnull Identifier recipeId, @Nonnull PacketByteBuf buffer) {
			return new ShapelessManaUpgradeRecipe(RecipeSerializer.SHAPELESS.read(recipeId, buffer));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void write(@Nonnull PacketByteBuf buffer, @Nonnull ShapelessManaUpgradeRecipe recipe) {
			RecipeSerializer.SHAPELESS.write(buffer, recipe.compose);
		}
	}
}
