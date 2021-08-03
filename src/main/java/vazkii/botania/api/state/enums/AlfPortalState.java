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

import java.util.Locale;

public enum AlfPortalState implements StringRepresentable {
	OFF,
	ON_Z,
	ON_X;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
