/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;

public interface PetalApothecaryRecipe extends Recipe<Container> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "petal_apothecary");

	/**
	 * @return Ingredient matching the final item that needs to be thrown into the apothecary
	 *         to perform a craft after a matching recipe is in.
	 */
	Ingredient getReagent();

	@NotNull
	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
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
