/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class PetalApothecaryProcessor implements IComponentProcessor {
	protected Recipe<?> recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		this.recipe = PatchouliUtils.getRecipe(level, BotaniaRecipeTypes.PETAL_TYPE, id);
	}

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			return null;
		}
		return switch (key) {
			case "recipe" -> IVariable.wrap(recipe.getId().toString());
			case "output" -> IVariable.from(recipe.getResultItem(level.registryAccess()));
			case "heading" -> IVariable.from(recipe.getResultItem(level.registryAccess()).getHoverName());
			default -> null;
		};
	}
}
