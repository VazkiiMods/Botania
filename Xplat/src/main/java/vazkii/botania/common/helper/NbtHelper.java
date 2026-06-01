/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.helper;

import net.minecraft.nbt.CompoundTag;

public final class NbtHelper {
	/**
	 * Uses the smallest possible tag type to store the specified integer value in the provided tag. The value can be
	 * read again like a regular INT tag. This is supposed to be used only for network update packets.
	 *
	 * @see net.minecraft.network.codec.ByteBufCodecs#VAR_INT
	 */
	public static void putVarInt(CompoundTag tag, String key, int value) {
		if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
			tag.putInt(key, value);
		} else if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
			tag.putShort(key, (short) value);
		} else {
			tag.putByte(key, (byte) value);
		}
	}

	private NbtHelper() {}
}
