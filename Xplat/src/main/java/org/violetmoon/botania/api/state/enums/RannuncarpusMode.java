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

public enum RannuncarpusMode implements StringRepresentable {
	STATE_INSENSITIVE("state_insensitive"),
	STATE_SENSITIVE("state_sensitive");

	private final String name;

	RannuncarpusMode(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
