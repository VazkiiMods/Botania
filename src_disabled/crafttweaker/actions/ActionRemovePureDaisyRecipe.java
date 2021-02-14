/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.exceptions.ScriptException;
import com.blamejared.crafttweaker.api.logger.ILogger;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;

import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

import java.util.Iterator;
import java.util.Map;

public class ActionRemovePureDaisyRecipe extends ActionRecipeBase {

	private final BlockState output;

	public ActionRemovePureDaisyRecipe(IRecipeManager manager, BlockState output) {
		super(manager);
		this.output = output;
	}

	@Override
	public void apply() {
		StateIngredient state = StateIngredientHelper.of(output);
		Iterator<Map.Entry<ResourceLocation, IRecipe<?>>> iter = getManager().getRecipes().entrySet().iterator();
		while (iter.hasNext()) {
			IPureDaisyRecipe recipe = (IPureDaisyRecipe) iter.next().getValue();
			if (state.test(recipe.getOutputState())) {
				iter.remove();
			}
		}
	}

	@Override
	public String describe() {
		return "Removing \"" + Registry.RECIPE_TYPE
				.getKey(getManager().getRecipeType()) + "\" recipes with output: " + output + "\"";
	}

	@Override
	public boolean validate(ILogger logger) {
		if (output == null) {
			logger.throwingWarn("output cannot be null!", new ScriptException("output MCBlockState cannot be null!"));
			return false;
		}
		return true;
	}
}
