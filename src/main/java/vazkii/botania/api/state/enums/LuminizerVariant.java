/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state.enums;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public enum LuminizerVariant implements StringIdentifiable {
	DEFAULT,
	DETECTOR,
	FORK,
	TOGGLE;

	@Override
	public String asString() {
		return name().toLowerCase(Locale.ROOT);
	}

}
