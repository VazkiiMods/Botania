/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.util.WeighedRandom;

import vazkii.botania.api.recipe.StateIngredient;

public final class OrechidOutput extends WeighedRandom.WeighedRandomItem implements Comparable<OrechidOutput> {
	private final StateIngredient output;

	public OrechidOutput(int weight, StateIngredient output) {
		super(weight);
		this.output = output;
	}

	public StateIngredient getOutput() {
		return output;
	}

	public int getWeight() {
		return weight;
	}

	/** Note: ordering is inconsistent with equals */
	@Override
	public int compareTo(OrechidOutput o) {
		return Integer.compare(o.weight, this.weight);
	}
}
