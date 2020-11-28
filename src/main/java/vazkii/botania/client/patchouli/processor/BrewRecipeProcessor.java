/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BrewRecipeProcessor implements IComponentProcessor {
	private IBrewRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		Identifier id = new Identifier(variables.get("recipe").asString());
		Recipe<?> recipe = ModRecipeTypes.getRecipes(MinecraftClient.getInstance().world, ModRecipeTypes.BREW_TYPE).get(id);
		if (recipe instanceof IBrewRecipe) {
			this.recipe = (IBrewRecipe) recipe;
		} else {
			Botania.LOGGER.warn("Missing brew recipe " + id);
		}
	}

	@Override
	public IVariable process(String key) {
		if (recipe == null) {
			return null;
		} else if (key.equals("heading")) {
			return IVariable.from(new TranslatableText("botaniamisc.brewOf", new TranslatableText(recipe.getBrew().getTranslationKey())));
		} else if (key.equals("vial")) {
			return IVariable.from(recipe.getOutput(new ItemStack(ModItems.vial)));
		} else if (key.equals("flask")) {
			return IVariable.from(recipe.getOutput(new ItemStack(ModItems.flask)));
		} else if (key.startsWith("input")) {
			int requestedIndex = Integer.parseInt(key.substring(5)) - 1;
			int indexOffset = (6 - recipe.getPreviewInputs().size()) / 2; //Center the brew ingredients
			int index = requestedIndex - indexOffset;

			if (index < recipe.getPreviewInputs().size() && index >= 0) {
				return IVariable.wrapList(Arrays.stream(recipe.getPreviewInputs().get(index).getMatchingStacksClient()).map(IVariable::from).collect(Collectors.toList()));
			} else {
				return null;
			}
		} else if (key.equals("is_offset")) {
			return IVariable.wrap(recipe.getPreviewInputs().size() % 2 == 0);
		}
		return null;
	}
}
