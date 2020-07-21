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
import net.minecraft.util.Identifier;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RunicAltarProcessor extends PetalApothecaryProcessor {
	@Override
	public void setup(IVariableProvider variables) {
		Identifier id = new Identifier(variables.get("recipe").asString());
		this.recipe = ModRecipeTypes.getRecipes(MinecraftClient.getInstance().world, ModRecipeTypes.RUNE_TYPE).get(id);
		if (recipe == null) {
			Botania.LOGGER.warn("Missing rune altar recipe " + id);
		}
	}

	@Override
	public IVariable process(String key) {
		if (key.equals("mana")) {
			return IVariable.wrap(((IRuneAltarRecipe) recipe).getManaUsage());
		}
		return super.process(key);
	}
}
