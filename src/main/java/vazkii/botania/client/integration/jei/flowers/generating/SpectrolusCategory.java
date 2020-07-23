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
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.misc.EntityIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.core.helper.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class SpectrolusCategory extends AbstractGenerationCategory<AbstractGenerationCategory.ManaGenRecipe> {

	public SpectrolusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.spectrolus, ModSubtiles.spectrolusFloating);
	}

	@Override
	protected Collection<ManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return ImmutableList.of(new WoolRecipe(), new SheepRecipe());
	}

	@Override
	protected void setIngredientsInputs(ManaGenRecipe recipe, IIngredients ingredients) {
		set((TypedRecipe<?>) recipe, ingredients);
	}

	private <T> void set(TypedRecipe<T> recipe, IIngredients ingredients) {
		ingredients.setInputLists(recipe.getType(), Collections.singletonList(recipe.getIngredients()));
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, ManaGenRecipe recipe, IIngredients ingredients) {
		set(recipeLayout, (TypedRecipe<?>) recipe, ingredients);
	}

	private <T> void set(IRecipeLayout recipeLayout, TypedRecipe<T> recipe, IIngredients ingredients) {
		IGuiIngredientGroup<T> group = recipeLayout.getIngredientsGroup(recipe.getType());
		group.init(0, true, 76, 4);
		group.set(ingredients);
	}

	@Override
	public Class<? extends ManaGenRecipe> getRecipeClass() {
		return ManaGenRecipe.class;
	}

	private interface TypedRecipe<T> {

		IIngredientType<T> getType();

		List<T> getIngredients();

	}

	private static class SheepRecipe extends ManaGenRecipe implements TypedRecipe<Entity> {

		public final List<Entity> sheep;

		public SheepRecipe() {
			super(SubTileSpectrolus.SHEEP_GEN);
			ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
			this.sheep = Arrays.stream(DyeColor.values())
					.map(color -> {
						SheepEntity thisSheep = EntityType.SHEEP.create(world);
						assert thisSheep != null;
						thisSheep.setFleeceColor(color);
						return thisSheep;
					})
					.collect(Collectors.toList());
		}

		@Override
		public IIngredientType<Entity> getType() {
			return EntityIngredient.TYPE;
		}

		@Override
		public List<Entity> getIngredients() {
			return sheep;
		}
	}

	private static class WoolRecipe extends SimpleManaGenRecipe implements TypedRecipe<ItemStack> {

		public WoolRecipe() {
			super(Arrays.stream(DyeColor.values())
					.map(ColorHelper.WOOL_MAP)
					.map(ItemStack::new)
					.collect(Collectors.toList()), SubTileSpectrolus.WOOL_GEN);
		}

		@Override
		public IIngredientType<ItemStack> getType() {
			return VanillaTypes.ITEM;
		}

		@Override
		public List<ItemStack> getIngredients() {
			return getStacks();
		}
	}

}
