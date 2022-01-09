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

import vazkii.botania.common.block.BlockGhostRail;

public class GhostRailComponent {
	public int floatTicks = 0;

	public void readFromNbt(CompoundTag tag) {
		floatTicks = tag.getInt(BlockGhostRail.TAG_FLOAT_TICKS);
	}

	public void writeToNbt(CompoundTag tag) {
		tag.putInt(BlockGhostRail.TAG_FLOAT_TICKS, floatTicks);
	}
}
