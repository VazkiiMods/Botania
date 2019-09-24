/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 20 2019, 8:49 PM (GMT)]
 */
package vazkii.botania.client.patchouli.component;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.IModRecipe;
import vazkii.patchouli.api.VariableHolder;

import java.util.List;
import java.util.Map;

/**
 * Patchouli custom component that draws a rotating circle of items from the defined recipe.
 */
public class RotatingRecipeComponent extends RotatingItemListComponentBase {
	@VariableHolder
	@SerializedName("recipe_name")
	public String recipeName;

	@VariableHolder
	@SerializedName("recipe_type")
	public String recipeType;

	@Override
	protected List<Ingredient> makeIngredients() {
		Map<ResourceLocation, ? extends IModRecipe> map;
		if("runic_altar".equals(recipeType)) {
			map = BotaniaAPI.runeAltarRecipes;
		} else if("petal_apothecary".equals(recipeType)) {
			map = BotaniaAPI.petalRecipes;
		} else {
			throw new IllegalArgumentException("Type must be 'runic_altar' or 'petal_apothecary'!");
		}
		IModRecipe recipe = map.get(new ResourceLocation(recipeName));
		return recipe.getInputs();
	}
}
