/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.internal_caps;

import net.minecraft.nbt.CompoundTag;

public class TigerseyeComponent {
	private static final String TAG_PACIFIED = "botania:tigerseye_pacified";
	private boolean pacified = false;

	public boolean isPacified() {
		return pacified;
	}

	public void setPacified() {
		this.pacified = true;
	}

	public void readFromNbt(CompoundTag tag) {
		this.pacified = tag.getBoolean(TAG_PACIFIED);
	}

	public void writeToNbt(CompoundTag tag) {
		if (pacified) {
			tag.putBoolean(TAG_PACIFIED, true);
		}
	}
}
