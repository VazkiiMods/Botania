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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class PetalApothecaryProcessor implements IComponentProcessor {
	protected IRecipe<?> recipe;

	@Override
	public void setup(IVariableProvider<String> variables) {
		ResourceLocation id = new ResourceLocation(variables.get("recipe"));
		this.recipe = Minecraft.getInstance().world.getRecipeManager().getRecipes(ModRecipeTypes.PETAL_TYPE).get(id);
	}

	@Override
	public String process(String key) {
		if (recipe == null) {
			return null;
		}
		switch (key) {
		case "recipe":
			return recipe.getId().toString();
		case "output":
			return PatchouliAPI.instance.serializeItemStack(recipe.getRecipeOutput());
		case "heading":
			return recipe.getRecipeOutput().getDisplayName().getString();
		}
		return null;
	}
}
