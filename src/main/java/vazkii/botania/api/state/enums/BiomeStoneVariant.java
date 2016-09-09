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

public enum BiomeStoneVariant implements IStringSerializable {
	FOREST,
	PLAINS,
	MOUNTAIN,
	FUNGAL,
	SWAMP,
	DESERT,
	TAIGA,
	MESA,
	FOREST_COBBLE,
	PLAINS_COBBLE,
	MOUNTAIN_COBBLE,
	FUNGAL_COBBLE,
	SWAMP_COBBLE,
	DESERT_COBBLE,
	TAIGA_COBBLE,
	MESA_COBBLE;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
