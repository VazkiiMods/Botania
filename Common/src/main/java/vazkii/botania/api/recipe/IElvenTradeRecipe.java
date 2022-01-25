/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IElvenTradeRecipe extends Recipe<Container> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "elven_trade");

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
	NonNullList<Ingredient> getIngredients();

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
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	// Ignored IRecipe boilerplate

	@Override
	default boolean matches(@Nonnull Container inv, @Nonnull Level world) {
		return false;
	}

	@Nonnull
	@Override
	default ItemStack assemble(@Nonnull Container inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
