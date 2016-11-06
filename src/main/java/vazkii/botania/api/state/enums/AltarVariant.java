/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state.enums;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum AltarVariant implements IStringSerializable {
	DEFAULT,
	FOREST,
	PLAINS,
	MOUNTAIN,
	FUNGAL,
	SWAMP,
	DESERT,
	TAIGA,
	MESA,
	MOSSY;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
