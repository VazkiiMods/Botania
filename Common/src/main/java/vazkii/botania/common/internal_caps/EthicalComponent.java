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
import net.minecraft.world.entity.item.PrimedTnt;

import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;

public abstract class EthicalComponent {
	protected static final String TAG_UNETHICAL = "botania:unethical";
	protected boolean unethical;

	public EthicalComponent(PrimedTnt entity) {
		unethical = SubTileEntropinnyum.isUnethical(entity);
	}

	public final boolean isUnethical() {
		return unethical;
	}

	public void readFromNbt(CompoundTag tag) {
		unethical = tag.getBoolean(TAG_UNETHICAL);
	}

	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(TAG_UNETHICAL, unethical);
	}

	public CompoundTag serializeNBT() {
		var ret = new CompoundTag();
		writeToNbt(new CompoundTag());
		return ret;
	}

	public void deserializeNBT(CompoundTag nbt) {
		readFromNbt(nbt);
	}
}
