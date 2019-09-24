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

import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.VariableHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Patchouli custom component that draws a rotating circle of items from a provided list.
 */
public class RotatingItemListComponent extends RotatingItemListComponentBase {
	@VariableHolder
	public List<String> ingredients;

	@Override
	protected List<Ingredient> makeIngredients() {
		return ingredients.stream().map(PatchouliAPI.instance::deserializeIngredient).collect(Collectors.toList());
	}
}
