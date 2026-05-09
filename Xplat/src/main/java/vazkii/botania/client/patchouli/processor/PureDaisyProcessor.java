/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;

public class PureDaisyProcessor implements IComponentProcessor {
	@Nullable
	private List<PureDaisyRecipe> recipes;
	@Nullable
	private List<PureDaisyRecipe> recipes2;
	@Nullable
	private String title;
	@Nullable
	private String title2;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		this.recipes = getRecipeData(level, variables, "recipes");

		if (variables.has("recipes2")) {
			this.recipes2 = getRecipeData(level, variables, "recipes2");
		}

		this.title = variables.has("title") ? variables.get("title", level.registryAccess()).asString() : null;
		this.title2 = variables.has("title2") ? variables.get("title2", level.registryAccess()).asString() : null;
	}

	private static List<PureDaisyRecipe> getRecipeData(Level level, IVariableProvider variables, String recipesVariable) {
		var recipes = new ArrayList<PureDaisyRecipe>();
		variables.get(recipesVariable, level.registryAccess())
				.asStreamOrSingleton(level.registryAccess())
				.map(IVariable::asString)
				.forEach(name -> {
					PureDaisyRecipe recipe = PatchouliUtils.getRecipe(level, BotaniaRecipeTypes.PURE_DAISY_TYPE, ResourceLocation.parse(name));
					if (recipe != null) {
						recipes.add(recipe);
					} else {
						BotaniaAPI.LOGGER.warn("Missing Pure Daisy recipe {}", name);
					}
				});
		return recipes;
	}

	@Nullable
	@Override
	public IVariable process(Level level, String key) {
		if (this.title != null && "title".equals(key)) {
			return IVariable.from(Component.translatable(this.title), level.registryAccess());
		}
		if (this.title2 != null && "title2".equals(key)) {
			return IVariable.from(Component.translatable(this.title2), level.registryAccess());
		}
		var variable = processRecipe(level, key, this.recipes, "title", "input", "output");
		return variable != null ? variable : processRecipe(level, key, this.recipes2, "title2", "input2", "output2");
	}

	@Nullable
	private IVariable processRecipe(Level level, String key, @Nullable List<PureDaisyRecipe> recipeList, String titleKey,
			String inputKey, String outputKey) {
		if (recipeList == null || recipeList.isEmpty()) {
			return null;
		}

		if (key.equals(titleKey)) {
			return IVariable.from(recipeList.getFirst().getOutput().getDisplayedStacks().getFirst().getHoverName(),
					level.registryAccess());
		}
		if (key.equals(inputKey)) {
			return PatchouliUtils.interweaveStateIngredients(
					recipeList.stream().map(PureDaisyRecipe::getInput).toList(), level);
		}
		if (key.equals(outputKey)) {
			return PatchouliUtils.interweaveStateIngredients(
					recipeList.stream().map(PureDaisyRecipe::getOutput).toList(), level);
		}
		return null;
	}
}
