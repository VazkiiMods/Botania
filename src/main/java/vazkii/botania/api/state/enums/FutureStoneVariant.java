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

public enum FutureStoneVariant implements IStringSerializable {
	ANDESITE,
	BASALT,
	DIORITE,
	GRANITE,
	POLISHED_ANDESITE,
	POLISHED_BASALT,
	POLISHED_DIORITE,
	POLISHED_GRANITE,
	ANDESITE_BRICK,
	BASALT_BRICK,
	DIORITE_BRICK,
	GRANITE_BRICK,
	CHISELED_ANDESITE,
	CHISELED_BASALT,
	CHISELED_DIORITE,
	CHISELED_GRANITE;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
