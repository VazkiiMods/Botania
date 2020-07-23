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
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.client.integration.jei.flowers.AbstractFlowerCategory;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.client.integration.jei.mana.ManaIngredientRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public abstract class AbstractGenerationCategory<T extends AbstractGenerationCategory.ManaGenRecipe> extends AbstractFlowerCategory<T> {
	protected final IDrawableStatic background;

	public AbstractGenerationCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
		super(guiHelper, flower, floatingFlower);
		background = guiHelper.createBlankDrawable(168, 64);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		setIngredientsInputs(recipe, ingredients);
		ingredients.setOutput(ManaIngredient.Type.INSTANCE, new ManaIngredient(recipe.getMana(), false));
	}

	protected abstract void setIngredientsInputs(T recipe, IIngredients ingredients);

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		setRecipeInputs(recipeLayout, recipe, ingredients);

		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(-1, true, 76, 24);
		stacks.set(-1, flower);

		IGuiIngredientGroup<IManaIngredient> manaIngredients = recipeLayout.getIngredientsGroup(ManaIngredient.Type.INSTANCE);
		manaIngredients.init(0, false,
				ManaIngredientRenderer.Bar.INSTANCE,
				33, 49,
				102, 5,
				0, 0);
		manaIngredients.set(ingredients);
	}

	protected abstract void setRecipeInputs(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients);

	public static class ManaGenRecipe {
		private final int mana;

		public ManaGenRecipe(int mana) {
			this.mana = mana;
		}

		public int getMana() {
			return mana;
		}
	}

	@Override
	protected ResourceLocation getLexiconPage() {
		return prefix("generating_flowers/" + getEntryName());
	}

	protected String getEntryName() {
		return Registry.ITEM.getKey(flower.getItem()).getPath();
	}
}
