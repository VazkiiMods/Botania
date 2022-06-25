package vazkii.botania.common.integration.crafttweaker.recipe.handler;

import com.blamejared.crafttweaker.api.bracket.custom.RecipeTypeBracketHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class DelegatingCraftingRecipeHandler<T extends U, U extends V, V extends CraftingRecipe> implements IRecipeHandler<T> {

	private final RecipeType<V> wrappedType;
	private final Supplier<IRecipeHandler<U>> delegate;
	private final Function<U, T> wrapper;

	public DelegatingCraftingRecipeHandler(RecipeType<V> wrappedType, Supplier<IRecipeHandler<U>> delegate, Function<U, T> wrapper) {
		this.wrappedType = wrappedType;
		this.delegate = delegate;
		this.wrapper = wrapper;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, T recipe) {
		return delegate.get().dumpToCommandString(manager, recipe);
	}

	@Override
	public Optional<Function<ResourceLocation, T>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, T recipe, List<IReplacementRule> rules) throws ReplacementNotSupportedException {
		IRecipeHandler<U> handler = delegate.get();
		IRecipeManager<?> mgr = RecipeTypeBracketHandler.getOrDefault(wrappedType);
		Optional<Function<ResourceLocation, U>> function = handler.replaceIngredients(mgr, recipe, rules);
		return function.map(f -> f.andThen(wrapper));
	}

	@Override
	public <U extends Recipe<?>> boolean doesConflict(@SuppressWarnings("rawtypes") IRecipeManager manager, T firstRecipe, U secondRecipe) {
		return delegate.get().doesConflict(manager, firstRecipe, secondRecipe);
	}
}
