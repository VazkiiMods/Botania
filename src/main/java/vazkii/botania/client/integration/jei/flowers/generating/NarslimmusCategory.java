/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;

import vazkii.botania.client.integration.jei.misc.EntityIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.mixin.AccessorSlimeEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NarslimmusCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	private final IIngredientRenderer<Entity> slimeRenderer;

	public NarslimmusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.narslimmus, ModSubtiles.narslimmusFloating);
		slimeRenderer = new EntityIngredient.Renderer() {
			@Override
			protected void doScaling(MatrixStack matrixStack, Entity ingredient) {}
		};
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(EntityIngredient.TYPE, ((NarslimmusRecipe) recipe).slime);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<Entity> entities = recipeLayout.getIngredientsGroup(EntityIngredient.TYPE);
		entities.init(0, true,
				slimeRenderer,
				76, 4,
				18, 18,
				1, 1);
		entities.set(ingredients);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<ManaGenRecipe> recipes = new ArrayList<>();
		for (int i = 1; i < 4; i++) {
			recipes.add(new NarslimmusRecipe(i));
		}
		return recipes;
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return NarslimmusRecipe.class;
	}

	private class NarslimmusRecipe extends ManaGenRecipe {

		public SlimeEntity slime;

		protected NarslimmusRecipe(int slimeSize) {
			super((int) Math.pow(2, slimeSize) * SubTileNarslimmus.MANA_PER_UNIT_SLIME);
			slime = EntityType.SLIME.create(world);
			assert slime != null;
			((AccessorSlimeEntity) slime).callSetSlimeSize(slimeSize, false);
		}
	}

}
