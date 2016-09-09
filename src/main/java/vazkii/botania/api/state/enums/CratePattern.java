/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CratePattern implements IStringSerializable {
	NONE,
	CRAFTY_1_1,
	CRAFTY_2_2,
	CRAFTY_1_2,
	CRAFTY_2_1,
	CRAFTY_1_3,
	CRAFTY_3_1,
	CRAFTY_2_3,
	CRAFTY_3_2,
	CRAFTY_DONUT;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
