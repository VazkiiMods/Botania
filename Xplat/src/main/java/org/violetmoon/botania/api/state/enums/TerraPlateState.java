/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;

public enum TerraPlateState implements StringRepresentable {
	IDLE("idle", true),
	COLLECTING("collecting", false),
	DISSIPATING("dissipating", true),
	DONE("done", false);

	private final String serializedName;
	private final boolean lookingForIngredients;

	TerraPlateState(String name, boolean lookingForIngredients) {
		this.serializedName = name;
		this.lookingForIngredients = lookingForIngredients;
	}

	@Override
	public String getSerializedName() {
		return serializedName;
	}

	public boolean isLookingForIngredients() {
		return lookingForIngredients;
	}
}
