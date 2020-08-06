/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ElvenTradeProcessor implements IComponentProcessor {
	private List<IElvenTradeRecipe> recipes;
	private int longestIngredientSize, mostInputs, mostOutputs;

	@Override
	public void setup(IVariableProvider variables) {
		ImmutableList.Builder<IElvenTradeRecipe> builder = ImmutableList.builder();
		for (IVariable s : variables.get("recipes").asListOrSingleton()) {
			IRecipe<?> recipe = ModRecipeTypes.getRecipes(Minecraft.getInstance().world, ModRecipeTypes.ELVEN_TRADE_TYPE).get(new ResourceLocation(s.asString()));
			if (recipe instanceof IElvenTradeRecipe) {
				builder.add((IElvenTradeRecipe) recipe);
			} else {
				Botania.LOGGER.warn("Missing elven trade recipe " + s);
			}
		}
		recipes = builder.build();
		for (IElvenTradeRecipe recipe : recipes) {
			List<Ingredient> inputs = recipe.getIngredients();
			for (Ingredient ingredient : inputs) {
				int length = ingredient.getMatchingStacks().length;
				if (length > longestIngredientSize) {
					longestIngredientSize = length;
				}
			}
			if (inputs.size() > mostInputs) {
				mostInputs = inputs.size();
			}
			if (recipe.getOutputs().size() > mostOutputs) {
				mostOutputs = recipe.getOutputs().size();
			}
		}
	}

	@Override
	public IVariable process(String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		if (key.equals("heading")) {
			return IVariable.from(recipes.get(0).getOutputs().get(0).getDisplayName());
		} else if (key.startsWith("input")) {
			int index = Integer.parseInt(key.substring(5)) - 1;
			if (index < mostInputs) {
				return interweaveIngredients(index);
			} else {
				return null;
			}
		}
		if (key.startsWith("output")) {
			int index = Integer.parseInt(key.substring(6)) - 1;
			if (index < mostOutputs) {
				return IVariable.wrapList(recipes.stream().map(IElvenTradeRecipe::getOutputs)
						.map(l -> index < l.size() ? l.get(index) : ItemStack.EMPTY)
						.map(IVariable::from)
						.collect(Collectors.toList()));
			}
		}
		return null;
	}

	private IVariable interweaveIngredients(int inputIndex) {
		List<Ingredient> recipes = this.recipes.stream().map(IElvenTradeRecipe::getIngredients).map(ingredients -> {
			if (inputIndex < ingredients.size()) {
				return ingredients.get(inputIndex);
			} else {
				return Ingredient.EMPTY;
			}
		}).collect(Collectors.toList());
		return PatchouliUtils.interweaveIngredients(recipes, longestIngredientSize);
	}

}
