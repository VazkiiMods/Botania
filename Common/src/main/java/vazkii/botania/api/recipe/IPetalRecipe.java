/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface IPetalRecipe extends Recipe<Container> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "petal_apothecary");

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
