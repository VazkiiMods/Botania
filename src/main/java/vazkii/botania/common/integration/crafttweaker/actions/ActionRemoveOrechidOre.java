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

import net.minecraft.block.BlockState;

import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.common.integration.crafttweaker.OrechidManager;

import java.util.function.Predicate;

public class ActionRemoveOrechidOre implements IRuntimeAction {
	private final Predicate<OrechidOutput> filter;
	private final BlockState output;
	private final OrechidManager target;

	public ActionRemoveOrechidOre(BlockState state, OrechidManager target) {
		this.filter = output -> output.getOutput().test(state);
		this.target = target;
		this.output = state;
	}

	@Override
	public void apply() {
		target.get().removeIf(filter);
	}

	@Override
	public String describe() {
		return "Removing all outputs for state " + output;
	}
}
