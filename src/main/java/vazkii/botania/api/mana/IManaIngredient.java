package vazkii.botania.api.mana;

import mezz.jei.api.ingredients.IIngredientRenderer;
import vazkii.botania.api.BotaniaAPIClient;

/**
 * The JEI ingredient for mana.
 *
 * Can be created using {@link BotaniaAPIClient#createManaIngredient(int, int, boolean)}
 */
public interface IManaIngredient {

    int getAmount();

    boolean isCreative();

    IIngredientRenderer<IManaIngredient> getSquareRenderer();

    IIngredientRenderer<IManaIngredient> getBarRenderer();

}
