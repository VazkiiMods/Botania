/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EndoflameCategory extends SimpleGenerationCategory {

	public EndoflameCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.endoflame, ModSubtiles.endoflameFloating);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (ItemStack stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM)) {
			if(stack.getItem().hasContainerItem(stack)) {
				continue;
			}
			int burnTime = SubTileEndoflame.getBurnTime(stack);
			if (burnTime > 0) {
				recipes.add(new SimpleManaGenRecipe(stack, burnTime * SubTileEndoflame.GEN));
			}
		}
		return recipes;
	}

}
