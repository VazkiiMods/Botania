package vazkii.botania.api.recipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * A recipe that requires some sort of reagent item to complete the process of producing the output item.
 */
public interface RecipeWithReagent<T extends RecipeInput> extends Recipe<T> {
	/**
	 * @return Ingredient matching the final item that needs to be added to craft the output.
	 */
	Ingredient getReagent();
}
