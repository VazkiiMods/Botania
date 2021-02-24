/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
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
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		this.recipe = PatchouliUtils.getRecipe(ModRecipeTypes.BREW_TYPE, id);
	}

	@Override
	public IVariable process(String key) {
		if (recipe == null) {
			if (key.equals("is_offset")) {
				return IVariable.wrap(false);
			}
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
		}
		if (key.equals("is_offset")) {
			return IVariable.wrap(recipe.getIngredients().size() % 2 == 0);
		}
		return null;
	}
}
