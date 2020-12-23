/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IElvenTradeRecipe extends Recipe<Inventory> {
	Identifier TYPE_ID = new Identifier(BotaniaAPI.MODID, "elven_trade");

	/**
	 * Attempts to match the recipe
	 *
	 * @param stacks Entire contents of the portal's buffer
	 * @return {@link Optional#empty()} if recipe doesn't match, Optional with a set of items used by recipe
	 *         otherwise
	 */
	Optional<List<ItemStack>> match(List<ItemStack> stacks);

	/**
	 * If the recipe does not contain the item, it will be destroyed upon entering the portal.
	 */
	boolean containsItem(ItemStack stack);

	/**
	 * @return Preview of the inputs
	 */
	@Nonnull
	@Override
	DefaultedList<Ingredient> getPreviewInputs();

	/**
	 * @return Preview of the outputs
	 */
	List<ItemStack> getOutputs();

	/**
	 * Actually evaluate the recipe
	 */
	List<ItemStack> getOutputs(List<ItemStack> inputs);

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	// Ignored IRecipe boilerplate

	@Override
	default boolean matches(@Nonnull Inventory inv, @Nonnull World world) {
		return false;
	}

	@Nonnull
	@Override
	default ItemStack craft(@Nonnull Inventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean fits(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isIgnoredInRecipeBook() {
		return true;
	}
}
