/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import mezz.jei.api.ingredients.IIngredientRenderer;

import vazkii.botania.api.BotaniaAPIClient;

/**
 * The JEI ingredient for mana.
 * Can be created using {@link BotaniaAPIClient#createManaIngredient(int, int, boolean)}
 */
public interface IManaIngredient {

	int getAmount();

	boolean isCreative();

	IIngredientRenderer<IManaIngredient> getSquareRenderer();

	IIngredientRenderer<IManaIngredient> getBarRenderer();

}
