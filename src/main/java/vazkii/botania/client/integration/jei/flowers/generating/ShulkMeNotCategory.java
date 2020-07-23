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
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.misc.EntityIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ShulkMeNotCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	private final IDrawableStatic levitation;
	private final IDrawableStatic arrow;

	public ShulkMeNotCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.shulkMeNot, ModSubtiles.shulkMeNotFloating);
		levitation = guiHelper.drawableBuilder(new ResourceLocation("minecraft", "textures/mob_effect/levitation.png"), 0, 0, 18, 18)
				.setTextureSize(18, 18)
				.build();
		arrow = guiHelper.drawableBuilder(prefix("textures/gui/arrow.png"), 0, 0, 22, 15)
				.setTextureSize(22, 15)
				.build();
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(EntityIngredient.TYPE, ((ShulkMeNotRecipe) recipe).ingredients);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<Entity> entities = recipeLayout.getIngredientsGroup(EntityIngredient.TYPE);
		entities.init(0, true, 52, 6);
		entities.init(1, true, 100, 6);
		entities.set(ingredients);
	}

	@Override
	public void draw(ManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		arrow.draw(matrixStack, 73, 6);
		levitation.draw(matrixStack, 100, 6);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new ShulkMeNotRecipe());
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return ShulkMeNotRecipe.class;
	}

	private static class ShulkMeNotRecipe extends ManaGenRecipe {

		private final List<List<Entity>> ingredients;

		public ShulkMeNotRecipe() {
			super(SubTileShulkMeNot.MAX_MANA);
			ingredients = new ArrayList<>();
			ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
			ingredients.add(Collections.singletonList(EntityType.SHULKER.create(world)));
			List<Entity> otherEntities = new ArrayList<>();
			otherEntities.add(EntityType.ZOMBIE.create(world));
			otherEntities.add(EntityType.CREEPER.create(world));
			otherEntities.add(EntityType.SKELETON.create(world));
			otherEntities.add(EntityType.SPIDER.create(world));
			otherEntities.add(EntityType.SHULKER.create(world));
			ingredients.add(otherEntities);
		}
	}
}
