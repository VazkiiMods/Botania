/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BrewRecipeProcessor implements IComponentProcessor {
	private IBrewRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		IRecipe<?> recipe = ModRecipeTypes.getRecipes(Minecraft.getInstance().world, ModRecipeTypes.BREW_TYPE).get(id);
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
			return IVariable.from(new TranslationTextComponent("botaniamisc.brewOf", new TranslationTextComponent(recipe.getBrew().getTranslationKey())));
		} else if (key.equals("vial")) {
			return IVariable.from(recipe.getOutput(new ItemStack(ModItems.vial)));
		} else if (key.equals("flask")) {
			return IVariable.from(recipe.getOutput(new ItemStack(ModItems.flask)));
		} else if (key.startsWith("input")) {
			int requestedIndex = Integer.parseInt(key.substring(5)) - 1;
			int indexOffset = (6 - recipe.getIngredients().size()) / 2; //Center the brew ingredients
			int index = requestedIndex - indexOffset;

			if (index < recipe.getIngredients().size() && index >= 0) {
				return IVariable.wrapList(Arrays.stream(recipe.getIngredients().get(index).getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
			} else {
				return null;
			}
		} else if (key.equals("is_offset")) {
			return IVariable.wrap(recipe.getIngredients().size() % 2 == 0);
		}
		return null;
	}
}
