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

public abstract class NarslimmusComponent {
	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";
	private boolean naturalSpawned = false;

	public void readFromNbt(CompoundTag tag) {
		naturalSpawned = tag.getBoolean(TAG_WORLD_SPAWNED);
	}

	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(TAG_WORLD_SPAWNED, naturalSpawned);
	}

	public CompoundTag serializeNBT() {
		var ret = new CompoundTag();
		writeToNbt(new CompoundTag());
		return ret;
	}

	public void deserializeNBT(CompoundTag nbt) {
		readFromNbt(nbt);
	}

	public boolean isNaturalSpawned() {
		return naturalSpawned;
	}

	public void setNaturalSpawn(boolean naturalSpawned) {
		this.naturalSpawned = naturalSpawned;
	}
}
