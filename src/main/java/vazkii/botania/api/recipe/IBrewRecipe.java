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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

import javax.annotation.Nonnull;

public interface IBrewRecipe extends IRecipe<IInventory> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "brew");

	Brew getBrew();

	int getManaUsage();

	ItemStack getOutput(ItemStack container);

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.func_241873_b(TYPE_ID).get();
	}

	@Nonnull
	@Override
	default ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
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
}
