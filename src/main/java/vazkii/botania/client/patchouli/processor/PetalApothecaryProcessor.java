/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 10 2019, 9:32 PM (GMT)]
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.IModRecipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class PetalApothecaryProcessor implements IComponentProcessor {
	protected IModRecipe recipe;

	@Override
	public void setup(IVariableProvider<String> variables) {
		this.recipe = BotaniaAPI.petalRecipes.get(new ResourceLocation(variables.get("recipe")));
	}

	@Override
	public String process(String key) {
		if(recipe == null) {
			return null;
		}
		switch (key) {
		case "recipe":
			return recipe.getId().toString();
		case "output":
			return PatchouliAPI.instance.serializeItemStack(recipe.getOutput());
		case "heading":
			return recipe.getOutput().getDisplayName().getString();
		}
		return null;
	}
}
