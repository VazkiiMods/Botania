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

import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.AbstractElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.stream.Collectors;

public class ElvenTradeProcessor implements IComponentProcessor {
	private List<AbstractElvenTradeRecipe> recipes;
	private int longestIngredientSize, mostInputs, mostOutputs;

	@Override
	public void setup(IVariableProvider<String> variables) {
		ImmutableList.Builder<AbstractElvenTradeRecipe> builder = ImmutableList.builder();
		for (String s : variables.get("recipes").split(";")) {
			IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipes(ModRecipeTypes.ELVEN_TRADE_TYPE).get(new ResourceLocation(s));
			if (recipe instanceof AbstractElvenTradeRecipe) {
				builder.add((AbstractElvenTradeRecipe) recipe);
			}
		}
		recipes = builder.build();
		for (AbstractElvenTradeRecipe recipe : recipes) {
			List<Ingredient> inputs = recipe.getInputs();
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
	public String process(String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		if (key.equals("heading")) {
			return recipes.get(0).getOutputs().get(0).getDisplayName().getString();
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
				return recipes.stream().map(AbstractElvenTradeRecipe::getOutputs)
						.map(l -> index < l.size() ? l.get(index) : ItemStack.EMPTY)
						.map(PatchouliAPI.instance::serializeItemStack).collect(Collectors.joining(","));
			} else {
				return null;
			}
		}
		return null;
	}

	private String interweaveIngredients(int inputIndex) {
		List<Ingredient> recipes = this.recipes.stream().map(AbstractElvenTradeRecipe::getInputs).map(ingredients -> {
			if (inputIndex < ingredients.size()) {
				return ingredients.get(inputIndex);
			} else {
				return Ingredient.EMPTY;
			}
		}).collect(Collectors.toList());
		return PatchouliUtils.interweaveIngredients(recipes, longestIngredientSize);
	}

}
