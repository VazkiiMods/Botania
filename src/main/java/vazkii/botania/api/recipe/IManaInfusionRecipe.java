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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IManaInfusionRecipe extends Recipe<Inventory> {
	Identifier TYPE_ID = new Identifier(BotaniaAPI.MODID, "mana_infusion");

	boolean matches(ItemStack stack);

	@Nullable
	BlockState getCatalyst();

	int getManaToConsume();

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	// Ignored IRecipe stuff

	@Nonnull
	@Override
	default ItemStack craft(@Nonnull Inventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean matches(@Nonnull Inventory inv, @Nonnull World world) {
		return false;
	}

	@Override
	default boolean fits(int width, int height) {
		return false;
	}

	@Override
	default boolean isIgnoredInRecipeBook() {
		return true;
	}
}
