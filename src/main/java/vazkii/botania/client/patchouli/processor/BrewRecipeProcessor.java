/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 22 2019, 7:03 PM (GMT)]
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.item.ModItems;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class BrewRecipeProcessor implements IComponentProcessor {
	private RecipeBrew recipe;

	@Override
	public void setup(IVariableProvider<String> variables) {
		this.recipe = BotaniaAPI.brewRecipes.get(new ResourceLocation(variables.get("recipe")));
	}

	@Override
	public String process(String key) {
		if(recipe == null) {
			return null;
		} else if(key.equals("heading")) {
			return I18n.format("botaniamisc.brewOf", I18n.format(recipe.getBrew().getUnlocalizedName()));
		} else if(key.equals("vial")) {
			return PatchouliAPI.instance.serializeItemStack(recipe.getOutput(new ItemStack(ModItems.vial)));
		} else if(key.equals("flask")) {
			return PatchouliAPI.instance.serializeItemStack(recipe.getOutput(new ItemStack(ModItems.flask)));
		} else if(key.startsWith("input")) {
			int requestedIndex = Integer.parseInt(key.substring(5)) - 1;
			int indexOffset = (6 - recipe.getInputs().size()) / 2; //Center the brew ingredients
			int index = requestedIndex - indexOffset;
			
			if(index < recipe.getInputs().size() && index >= 0)
				return PatchouliAPI.instance.serializeIngredient(recipe.getInputs().get(index));
			else
				return null;
		} else if(key.equals("is_offset")) {
			return Boolean.toString(recipe.getInputs().size() % 2 == 0);
		}
		return null;
	}
}
