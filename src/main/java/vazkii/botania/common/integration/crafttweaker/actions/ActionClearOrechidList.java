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

import vazkii.botania.common.integration.crafttweaker.OrechidManager;

public class ActionClearOrechidList implements IRuntimeAction {
	private final OrechidManager target;

	public ActionClearOrechidList(OrechidManager target) {
		this.target = target;
	}

	@Override
	public void apply() {
		target.get().clear();
	}

	@Override
	public String describe() {
		return "Clearing the " + target.getName() + " table";
	}
}
