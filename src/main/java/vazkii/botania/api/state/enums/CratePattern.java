/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.IStringSerializable;

import java.util.List;
import java.util.Locale;

public enum CratePattern implements IStringSerializable {
	NONE(true, true, true,
			true, true, true,
			true, true, true),
	CRAFTY_1_1(true, false, false,
			false, false, false,
			false, false, false),
	CRAFTY_2_2(true, true, false,
			true, true, false,
			false, false, false),
	CRAFTY_1_2(true, false, false,
			true, false, false,
			false, false, false),
	CRAFTY_2_1(true, true, false,
			false, false, false,
			false, false, false),
	CRAFTY_1_3(true, false, false,
			true, false, false,
			true, false, false),
	CRAFTY_3_1(true, true, true,
			false, false, false,
			false, false, false),
	CRAFTY_2_3(true, true, false,
			true, true, false,
			true, true, false),
	CRAFTY_3_2(true, true, true,
			true, true, true,
			false, false, false),
	CRAFTY_DONUT(true, true, true,
			true, false, true,
			true, true, true);

	public final List<Boolean> openSlots;

	CratePattern(Boolean... pattern) {
		this.openSlots = ImmutableList.copyOf(pattern);
		Preconditions.checkArgument(this.openSlots.size() == 9, "Malformed pattern");
	}

	@Override
	public String getString() {
		return name().toLowerCase(Locale.ROOT);
	}
}
