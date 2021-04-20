/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IManaInfusionRecipe extends IRecipe<IInventory> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_infusion");

	/**
	 * Attempts to match the recipe.
	 *
	 * @param stack The whole stack that is in the Mana Pool (when actually crafting)
	 *              or in the player's hand (for the HUD).
	 * @return Whether this recipe matches the given stack.
	 */
	boolean matches(ItemStack stack);

	/**
	 * Get the recipe output, used for display (in JEI or the HUD).
	 * If {@link #getRecipeOutput(ItemStack)} isn't overridden, this is also the actual result of the crafting recipe.
	 *
	 * @return The output stack of the recipe.
	 */
	@Nonnull
	@Override
	ItemStack getRecipeOutput();

	/**
	 * Get the actual recipe output, not just for display. Defaults to a copy of {@link #getRecipeOutput()}.
	 *
	 * @param input The whole stack that is in the Mana Pool, not a copy.
	 * @return The output stack of the recipe for the specific input.
	 */
	@Nonnull
	default ItemStack getRecipeOutput(@Nonnull ItemStack input) {
		return getRecipeOutput().copy();
	}

	/**
	 * Get the catalyst that must be under the Mana Pool for this recipe, or {@code null} if it can be anything.
	 *
	 * @return The catalyst ingredient.
	 */
	@Nullable
	StateIngredient getRecipeCatalyst();

	@Nullable
	@Deprecated
	BlockState getCatalyst();

	/**
	 * @return How much mana this recipe consumes from the pool.
	 */
	int getManaToConsume();

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	// Ignored IRecipe stuff

	@Nonnull
	@Override
	default ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return false;
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}

	@Override
	default boolean isDynamic() {
		return true;
	}
}
