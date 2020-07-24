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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;

import java.util.*;

import static vazkii.botania.common.block.subtile.generating.SubTileRafflowsia.STREAK_OUTPUTS;

public class RafflowsiaCategory extends SimpleGenerationCategory {
	public RafflowsiaCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.rafflowsia, ModSubtiles.rafflowsiaFloating);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		super.setRecipeInputs(recipeLayout, recipe, ingredients);

		recipeLayout.getIngredientsGroup(ManaIngredient.TYPE).addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int streak = Arrays.binarySearch(STREAK_OUTPUTS, ingredient.getAmount()) + 1;
			tooltip.add(new TranslationTextComponent("botania.nei.rafflowsia.tooltip", streak).func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
		});
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<ItemStack> flowers = new ArrayList<>();
		for (Block block : ModTags.Blocks.SPECIAL_FLOWERS.getAllElements()) {
			if (block != ModSubtiles.rafflowsia) {
				flowers.add(new ItemStack(block));
			}
		}
		return Collections.singletonList(new SimpleManaGenRecipe(flowers, STREAK_OUTPUTS));
	}
}
