package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

// Serializer for dynamic recipes that don't have a json/network representation
// The recipe type fully identifies the recipe
public class NoOpRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
	private final Function<ResourceLocation, T> constructor;

	public NoOpRecipeSerializer(Function<ResourceLocation, T> constructor) {
		this.constructor = constructor;
	}

	@Override
	public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
		return this.constructor.apply(recipeId);
	}

	@Override
	public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		return this.constructor.apply(recipeId);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, T recipe) {}
}
