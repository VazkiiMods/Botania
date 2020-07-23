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
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.misc.EntityIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.mixin.AccessorSlimeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NarslimmusCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	public NarslimmusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.narslimmus, ModSubtiles.narslimmusFloating);
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(EntityIngredient.TYPE, ((NarslimmusRecipe) recipe).slime);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<Entity> entities = recipeLayout.getIngredientsGroup(EntityIngredient.TYPE);
		entities.init(0, true, 76, 4);
		entities.set(ingredients);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return IntStream.range(1, 4)
				.mapToObj(NarslimmusRecipe::new)
				.collect(Collectors.toList());
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return NarslimmusRecipe.class;
	}

	private static class NarslimmusRecipe extends ManaGenRecipe {

		public SlimeEntity slime;

		protected NarslimmusRecipe(int slimeSize) {
			super((int) Math.pow(2, slimeSize) * SubTileNarslimmus.MANA_PER_UNIT_SLIME);
			slime = EntityType.SLIME.create(Objects.requireNonNull(Minecraft.getInstance().world));
			assert slime != null;
			((AccessorSlimeEntity) slime).callSetSlimeSize(slimeSize, false);
		}
	}

}
