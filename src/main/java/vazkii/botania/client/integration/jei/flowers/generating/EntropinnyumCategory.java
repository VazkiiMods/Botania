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
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.Collection;
import java.util.Collections;

public class EntropinnyumCategory extends SimpleGenerationCategory {

	private final IDrawable water;
	private final IDrawable barrier;

	public EntropinnyumCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.entropinnyum, ModSubtiles.entropinnyumFloating);
		water = guiHelper.createDrawableIngredient(new ItemStack(Items.WATER_BUCKET));
		barrier = guiHelper.createDrawableIngredient(new ItemStack(Blocks.BARRIER));
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		matrixStack.push();
		matrixStack.translate(5, 25, 0);
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrix);
		water.draw(matrixStack);
		barrier.draw(matrixStack);
		RenderSystem.popMatrix();
		matrixStack.pop();
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new EntropinnyumRecipe());
	}

	@Override
	public Class<? extends SimpleManaGenRecipe> getRecipeClass() {
		return EntropinnyumRecipe.class;
	}

	private static class EntropinnyumRecipe extends SimpleManaGenRecipe {

		protected EntropinnyumRecipe() {
			super(Collections.singletonList(new ItemStack(Blocks.TNT)), 6500);
		}

	}

}
