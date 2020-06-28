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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface IPetalRecipe extends IRecipe<IInventory> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "petal_apothecary");

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getValue(TYPE_ID).get();
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}
}
