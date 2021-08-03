/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public final class IslandPos {
	private final int x;
	private final int z;

	public IslandPos(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public BlockPos getCenter() {
		return new BlockPos(x << 8, 64, z << 8);
	}

	public static IslandPos fromTag(CompoundTag tag) {
		return new IslandPos(tag.getInt("IslandX"), tag.getInt("IslandZ"));
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("IslandX", x);
		tag.putInt("IslandZ", z);
		return tag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof IslandPos)) {
			return false;
		}
		IslandPos islandPos = (IslandPos) o;
		return this.x == islandPos.x && this.z == islandPos.z;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + z;
		return result;
	}
}
