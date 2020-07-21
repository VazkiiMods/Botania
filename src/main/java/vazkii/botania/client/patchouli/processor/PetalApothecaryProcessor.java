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
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class PetalApothecaryProcessor implements IComponentProcessor {
	protected Recipe<?> recipe;

	@Override
	public void setup(IVariableProvider variables) {
		Identifier id = new Identifier(variables.get("recipe").asString());
		this.recipe = ModRecipeTypes.getRecipes(MinecraftClient.getInstance().world, ModRecipeTypes.PETAL_TYPE).get(id);
		if (recipe == null) {
			Botania.LOGGER.warn("Missing apothecary recipe " + id);
		}
	}

	@Override
	public IVariable process(String key) {
		if (recipe == null) {
			return null;
		}
		switch (key) {
		case "recipe":
			return IVariable.wrap(recipe.getId().toString());
		case "output":
			return IVariable.from(recipe.getOutput());
		case "heading":
			return IVariable.from(recipe.getOutput().getName());
		}
		return null;
	}
}
