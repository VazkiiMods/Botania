/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IElvenTradeRecipe extends IRecipe<IInventory> {
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
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.func_241873_b(TYPE_ID).get();
	}

	// Ignored IRecipe boilerplate

	@Override
	default boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return false;
	}

	@Nonnull
	@Override
	default ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
