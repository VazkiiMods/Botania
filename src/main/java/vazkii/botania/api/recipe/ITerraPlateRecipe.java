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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface ITerraPlateRecipe extends Recipe<Inventory> {
	Identifier TERRA_PLATE_ID = new Identifier(BotaniaAPI.MODID, "terra_plate");
	Identifier TYPE_ID = TERRA_PLATE_ID;

	int getMana();

	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	@Override
	default boolean fits(int width, int height) {
		return false;
	}

	@Nonnull
	@Override
	default ItemStack getRecipeKindIcon() {
		return Registry.ITEM.getOrEmpty(TERRA_PLATE_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}
}
