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
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IVariableProvider;

public class RunicAltarProcessor extends PetalApothecaryProcessor {
	@Override
	public void setup(IVariableProvider<String> variables) {
		ResourceLocation id = new ResourceLocation(variables.get("recipe"));
		this.recipe = Minecraft.getInstance().world.getRecipeManager().getRecipes(ModRecipeTypes.RUNE_TYPE).get(id);
		if (recipe == null) {
			Botania.LOGGER.warn("Missing rune altar recipe " + id);
		}
	}

	@Override
	public String process(String key) {
		if (key.equals("mana")) {
			return String.valueOf(((IRuneAltarRecipe) recipe).getManaUsage());
		}
		return super.process(key);
	}
}
