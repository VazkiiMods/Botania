/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiCraftingProcessor implements IComponentProcessor {
	@SuppressWarnings("NotNullFieldNotInitialized")
	private List<CraftingRecipe> recipes;
	@Nullable
	private List<CraftingRecipe> recipes2;
	private boolean shapeless = true;
	private boolean shapeless2 = true;
	private int longestIngredientSize = 0;
	private int longestIngredientSize2 = 0;
	private boolean hasCustomHeading;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		RecipeData result = getRecipeData(level, variables, "recipes");
		this.recipes = result.recipes();
		this.shapeless = result.shapeless();
		this.longestIngredientSize = result.longestIngredientSize();

		if (variables.has("recipes2")) {
			RecipeData result2 = getRecipeData(level, variables, "recipes2");
			this.recipes2 = result2.recipes();
			this.shapeless2 = result2.shapeless();
			this.longestIngredientSize2 = result2.longestIngredientSize();
		}
		this.hasCustomHeading = variables.has("heading");
	}

	private static RecipeData getRecipeData(Level level, IVariableProvider variables, String recipesVariable) {
		List<String> names = variables.get(recipesVariable, level.registryAccess()).asStream(level.registryAccess()).map(IVariable::asString).toList();
		var recipes = new ArrayList<CraftingRecipe>();
		boolean shapeless = true;
		int longestIngredientSize = 0;
		for (String name : names) {
			CraftingRecipe recipe = PatchouliUtils.getRecipe(level, RecipeType.CRAFTING, ResourceLocation.parse(name));
			if (recipe != null) {
				recipes.add(recipe);
				if (shapeless) {
					shapeless = !(recipe instanceof ShapedRecipe);
				}
				for (Ingredient ingredient : recipe.getIngredients()) {
					int size = ingredient.getItems().length;
					if (longestIngredientSize < size) {
						longestIngredientSize = size;
					}
				}
			} else {
				BotaniaAPI.LOGGER.warn("Missing crafting recipe {}", name);
			}
		}
		return new RecipeData(recipes, shapeless, longestIngredientSize);
	}

	private record RecipeData(ArrayList<CraftingRecipe> recipes, boolean shapeless, int longestIngredientSize) {
	}

	@SuppressWarnings("NullableProblems")
	@Nullable
	@Override
	public IVariable process(Level level, String key) {
		if (key.equals("heading")) {
			if (!hasCustomHeading && !recipes.isEmpty()) {
				return IVariable.from(recipes.getFirst().getResultItem(level.registryAccess()).getHoverName(), level.registryAccess());
			}
			return null;
		}
		IVariable recipes1Variable = processRecipes(level, key, recipes, "input", "output", "shapeless", longestIngredientSize, shapeless);
		if (recipes1Variable != null) {
			return recipes1Variable;
		}
		if (recipes2 != null) {
			return processRecipes(level, key, recipes2, "input2_", "output2", "shapeless2", longestIngredientSize2, shapeless2);
		}
		return null;
	}

	@Nullable
	private IVariable processRecipes(Level level, String key, List<CraftingRecipe> recipeList, String inputKeyPrefix,
			String outputKey, String shapelessKey, int size, boolean isShapeless) {
		if (recipeList.isEmpty()) {
			return null;
		}
		if (key.startsWith(inputKeyPrefix) && key.length() == inputKeyPrefix.length() + 1) {
			int index = Integer.parseInt(key.substring(inputKeyPrefix.length())) - 1;
			int shapedX = index % 3;
			int shapedY = index / 3;
			List<Ingredient> ingredients = new ArrayList<>();
			for (CraftingRecipe recipe : recipeList) {
				if (recipe instanceof ShapedRecipe shaped) {
					if (shaped.getWidth() < shapedX + 1) {
						ingredients.add(Ingredient.EMPTY);
					} else {
						int realIndex = index - (shapedY * (3 - shaped.getWidth()));
						NonNullList<Ingredient> list = recipe.getIngredients();
						ingredients.add(list.size() > realIndex ? list.get(realIndex) : Ingredient.EMPTY);
					}

				} else {
					NonNullList<Ingredient> list = recipe.getIngredients();
					ingredients.add(list.size() > index ? list.get(index) : Ingredient.EMPTY);
				}
			}
			return PatchouliUtils.interweaveIngredients(ingredients, size, level);
		}
		if (key.equals(outputKey)) {
			return IVariable.wrapList(recipeList.stream()
					.map(r -> IVariable.from(r.getResultItem(level.registryAccess()), level.registryAccess()))
					.collect(Collectors.toList()), level.registryAccess());
		}
		if (key.equals(shapelessKey)) {
			return IVariable.wrap(isShapeless, level.registryAccess());
		}
		return null;
	}
}
