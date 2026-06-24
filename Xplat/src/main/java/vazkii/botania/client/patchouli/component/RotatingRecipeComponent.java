/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.component;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Patchouli custom component that draws a rotating circle of items from the defined recipe.
 */
public class RotatingRecipeComponent extends RotatingItemListComponentBase {
	@SerializedName("recipe_name")
	public String recipeName;

	@SerializedName("recipe_type")
	public String recipeType;

	@Override
	protected List<Ingredient> makeIngredients() {
		Level world = Minecraft.getInstance().level;
		if ("runic_altar".equals(recipeType)) {
			var recipe = BotaniaRecipeTypes.getRecipe(world,
					ResourceLocation.parse(recipeName), BotaniaRecipeTypes.RUNIC_ALTAR_TYPE
			);
			if (recipe.isEmpty()) {
				return ImmutableList.of();
			}
			var ingredients = new ArrayList<>(recipe.get().value().getIngredients());
			ingredients.addAll(recipe.get().value().getCatalysts());
			return NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		} else if ("petal_apothecary".equals(recipeType)) {
			var recipe = BotaniaRecipeTypes.getRecipe(world,
					ResourceLocation.parse(recipeName), BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE
			);
			return recipe.isPresent() ? recipe.get().value().getIngredients() : ImmutableList.of();
		} else {
			throw new IllegalArgumentException("Type must be 'runic_altar' or 'petal_apothecary'!");
		}
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
		recipeName = lookup.apply(IVariable.wrap(recipeName, registries)).asString();
		recipeType = lookup.apply(IVariable.wrap(recipeType, registries)).asString();
	}
}
