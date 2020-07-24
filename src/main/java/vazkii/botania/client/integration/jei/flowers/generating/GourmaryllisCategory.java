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

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;

import java.util.*;
import java.util.function.Consumer;

import static vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis.*;

public class GourmaryllisCategory extends SimpleGenerationCategory implements Consumer<ParticleDrawable> {

	public GourmaryllisCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.gourmaryllis, ModSubtiles.gourmaryllisFloating);
		particle = new ParticleDrawable(this);
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		currentFood = recipe.getStacks().get(0);
		super.draw(recipe, matrixStack, mouseX, mouseY);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		super.setRecipeInputs(recipeLayout, recipe, ingredients);

		recipeLayout.getIngredientsGroup(ManaIngredient.TYPE).addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int streak = Arrays.binarySearch(recipe.getMana(), ingredient.getAmount()) + 1;
			tooltip.add(new TranslationTextComponent("botania.nei.gourmaryllis.tooltip", streak).func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
		});
	}

	private ItemStack currentFood = null;

	@Override
	public void accept(ParticleDrawable drawable) {
		if (ClientTickHandler.ticksInGame % 3 == 0) {
			ItemParticleData data = new ItemParticleData(ParticleTypes.ITEM, currentFood);
			for (int i = 0; i < 10; i++) {
				Random rand = new Random();
				Particle particle = drawable.addParticle(data,
						0.4 + 0.1 * rand.nextGaussian(),
						0.6 + 0.1 * rand.nextGaussian(),
						1.0 + 0.1 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian());
				if (particle != null) {
					particle.setMaxAge(5);
				}
			}
		}
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (ItemStack stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM)) {
			if (!stack.getItem().isFood()) {
				continue;
			}

			int[] mana = new int[STREAK_MULTIPLIERS.length - 1];
			for (int i = 1; i < STREAK_MULTIPLIERS.length; i++) {
				mana[i - 1] = getDigestingMana(getEffectiveFoodValue(stack), STREAK_MULTIPLIERS[i]);
			}
			recipes.add(new SimpleManaGenRecipe(Collections.singletonList(stack), mana));
		}
		return recipes;
	}
}
