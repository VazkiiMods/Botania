/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public class TerraPlateProcessor implements IComponentProcessor {
	private ITerraPlateRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		Identifier id = new Identifier(variables.get("recipe").asString());
		this.recipe = PatchouliUtils.getRecipe(ModRecipeTypes.TERRA_PLATE_TYPE, id);
	}

	@Override
	public IVariable process(String key) {
		if (recipe == null) {
			return null;
		}
		if (key.equals("output")) {
			return IVariable.from(recipe.getOutput());
		}
		if (key.startsWith("input")) {
			int index = Integer.parseInt(key.substring(5)) - 1;
			List<Ingredient> list = recipe.getPreviewInputs();
			if (index >= 0 && index < list.size()) {
				return IVariable.from(list.get(index).getMatchingStacksClient());
			}
		}
		return null;
	}
}
