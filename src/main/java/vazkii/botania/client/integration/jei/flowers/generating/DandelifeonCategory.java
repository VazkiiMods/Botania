/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Collection;
import java.util.Collections;

import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MANA_PER_GEN;
import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MAX_MANA_GENERATIONS;

public class DandelifeonCategory extends SimpleGenerationCategory {

	public DandelifeonCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.dandelifeon, ModSubtiles.dandelifeonFloating);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		super.setRecipeInputs(recipeLayout, recipe, ingredients);

		recipeLayout.getIngredientsGroup(ManaIngredient.TYPE).addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			tooltip.add(new TranslationTextComponent("botania.nei.dandelifeon.tooltip").func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("botania.nei.dandelifeon.tooltip1", ingredient.getAmount() / MANA_PER_GEN).func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
		});
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		int[] mana = new int[MAX_MANA_GENERATIONS + 1];
		for (int i = 0; i <= MAX_MANA_GENERATIONS; i++) {
			mana[i] = MANA_PER_GEN * i;
		}
		return Collections.singleton(new SimpleManaGenRecipe(Collections.singletonList(new ItemStack(ModBlocks.cellBlock)), mana));
	}

}
