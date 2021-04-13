/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.exceptions.ScriptException;
import com.blamejared.crafttweaker.api.logger.ILogger;

import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.integration.crafttweaker.OrechidManager;

public class ActionAddOrechidOre implements IRuntimeAction {
	private final StateIngredient input;
	private final int weight;
	private final OrechidManager target;

	public ActionAddOrechidOre(StateIngredient input, int weight, OrechidManager target) {
		this.input = input;
		this.weight = weight;
		this.target = target;
	}

	@Override
	public void apply() {
		target.get().add(new OrechidOutput(weight, input));
	}

	@Override
	public String describe() {
		return "Adding " + input + " with weight " + weight + " to " + target.getName() + " tables";
	}

	@Override
	public boolean validate(ILogger logger) {
		if (weight <= 0) {
			logger.throwingWarn("Weight must be positive!", new ScriptException("Output weight is " + weight + ", must be positive!"));
			return false;
		}
		return true;
	}
}
