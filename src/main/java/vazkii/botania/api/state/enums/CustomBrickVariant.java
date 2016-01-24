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

public enum CustomBrickVariant implements IStringSerializable {
	HELLISH_BRICK,
	SOUL_BRICK,
	FROSTY_BRICK,
	ROOF_TILE,
	AZULEJO_1,
	AZULEJO_2,
	AZULEJO_3,
	AZULEJO_4,
	AZULEJO_5,
	AZULEJO_6,
	AZULEJO_7,
	AZULEJO_8,
	AZULEJO_9,
	AZULEJO_10,
	AZULEJO_11,
	AZULEJO_12;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
