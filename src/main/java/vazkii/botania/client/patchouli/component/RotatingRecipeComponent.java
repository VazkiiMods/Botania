/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.component;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.Map;
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
		World world = MinecraftClient.getInstance().world;
		Map<Identifier, ? extends Recipe<?>> map;
		if ("runic_altar".equals(recipeType)) {
			map = ModRecipeTypes.getRecipes(world, ModRecipeTypes.RUNE_TYPE);
		} else if ("petal_apothecary".equals(recipeType)) {
			map = ModRecipeTypes.getRecipes(world, ModRecipeTypes.PETAL_TYPE);
		} else {
			throw new IllegalArgumentException("Type must be 'runic_altar' or 'petal_apothecary'!");
		}
		Recipe<?> recipe = map.get(new Identifier(recipeName));
		if (recipe == null) {
			throw new RuntimeException("Missing recipe " + recipeName);
		}
		return recipe.getPreviewInputs();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		recipeName = lookup.apply(IVariable.wrap(recipeName)).asString();
		recipeType = lookup.apply(IVariable.wrap(recipeType)).asString();
	}
}
