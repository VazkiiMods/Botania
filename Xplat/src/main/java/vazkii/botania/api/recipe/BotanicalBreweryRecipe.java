/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.brew.Brew;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public interface BotanicalBreweryRecipe extends Recipe<ProcessingRecipeInput> {
	ResourceLocation TYPE_ID = botaniaRL("brew");

	Brew getBrew();

	int getManaUsage();

	ItemStack getOutput(ItemStack container);

	@Override
	default RecipeType<?> getType() {
		return Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID));
	}

	@Override
	default ItemStack getResultItem(HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default ItemStack assemble(ProcessingRecipeInput inv, HolderLookup.Provider registries) {
		return inv.isEmpty() ? ItemStack.EMPTY : getOutput(inv.getItem(0));
	}

	@Override
	default NonNullList<ItemStack> getRemainingItems(ProcessingRecipeInput input) {
		return Recipe.super.getRemainingItems(input.isEmpty() ? input : input.getSubset(1, input.size()));
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return width * height >= getIngredients().size() + 1;
	}
}
