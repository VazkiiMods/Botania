package vazkii.botania.data.recipes;

import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public interface BotaniaRecipeProvider {
	void registerRecipes(Consumer<RecipeJsonProvider> consumer);
}
