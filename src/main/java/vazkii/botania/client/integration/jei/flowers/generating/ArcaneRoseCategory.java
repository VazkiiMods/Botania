/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.misc.ArbIngredientRenderer;
import vazkii.botania.client.integration.jei.misc.EntityIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ArcaneRoseCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {
	public ArcaneRoseCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.rosaArcana, ModSubtiles.rosaArcanaFloating);
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(EntityIngredient.TYPE, Collections.singletonList(((IArcaneRoseRecipe) recipe).getEntities()));
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<Entity> entities = recipeLayout.getIngredientsGroup(EntityIngredient.TYPE);
		entities.init(0, true,
				((IArcaneRoseRecipe) recipe).getRenderer(),
				76, 4,
				18, 18,
				1, 1);
		entities.set(ingredients);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return ImmutableList.of(new ExperienceOrbRecipe(helpers.getGuiHelper()), new PlayerRecipe());
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return ManaGenRecipe.class;
	}

	@Override
	protected String getEntryName() {
		return "arcanerose";
	}

	public static int EXPERIENCE_POINTS = 100;

	private interface IArcaneRoseRecipe {
		List<Entity> getEntities();

		IIngredientRenderer<Entity> getRenderer();
	}

	private static class ExperienceOrbRecipe extends ManaGenRecipe implements IArcaneRoseRecipe {

		private final ExperienceOrbRenderer renderer;
		private final List<Entity> orb;

		public ExperienceOrbRecipe(IGuiHelper guiHelper) {
			super(SubTileArcaneRose.ORB_MANA * EXPERIENCE_POINTS);
			ClientWorld world = Minecraft.getInstance().world;
			assert world != null;
			orb = Collections.singletonList(new ExperienceOrbEntity(world, 0, 0, 0, 100));
			renderer = new ExperienceOrbRenderer(guiHelper);
		}

		@Override
		public List<Entity> getEntities() {
			return orb;
		}

		@Override
		public IIngredientRenderer<Entity> getRenderer() {
			return renderer;
		}

		private static class ExperienceOrbRenderer extends ArbIngredientRenderer<Entity> {

			private final IDrawableStatic orbs;

			public ExperienceOrbRenderer(IGuiHelper guiHelper) {
				orbs = guiHelper.drawableBuilder(prefix("textures/gui/experience_orbs.png"), 0, 0, 16, 16)
						.setTextureSize(16, 16)
						.build();
			}

			@Override
			public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable Entity ingredient) {
				if (!(ingredient instanceof ExperienceOrbEntity)) {
					return;
				}

				RenderSystem.enableAlphaTest();
				orbs.draw(matrixStack, xPosition, yPosition);
			}
		}
	}

	private static class PlayerRecipe extends ManaGenRecipe implements IArcaneRoseRecipe {
		public PlayerRecipe() {
			super(SubTileArcaneRose.PLAYER_MANA * EXPERIENCE_POINTS);
		}

		@Override
		public List<Entity> getEntities() {
			ClientWorld world = Minecraft.getInstance().world;
			assert world != null;
			return new ArrayList<>(world.getPlayers());
		}

		@Override
		public IIngredientRenderer<Entity> getRenderer() {
			return EntityIngredient.RENDERER;
		}
	}
}
