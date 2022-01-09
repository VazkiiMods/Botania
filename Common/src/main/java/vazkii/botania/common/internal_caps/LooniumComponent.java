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
import net.minecraft.world.item.ItemStack;

public class LooniumComponent {
	protected static final String TAG_TODROP = "toDrop";
	private ItemStack toDrop = ItemStack.EMPTY;

	public ItemStack getDrop() {
		return toDrop;
	}

	public void setDrop(ItemStack stack) {
		this.toDrop = stack;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof LooniumComponent && ItemStack.matches(((LooniumComponent) obj).toDrop, toDrop);
	}

	@Override
	public int hashCode() {
		return toDrop.hashCode();
	}

	public void readFromNbt(CompoundTag tag) {
		if (tag.contains(TAG_TODROP)) {
			setDrop(ItemStack.of(tag.getCompound(TAG_TODROP)));
		} else {
			setDrop(ItemStack.EMPTY);
		}
	}

	public void writeToNbt(CompoundTag tag) {
		if (!getDrop().isEmpty()) {
			tag.put(TAG_TODROP, getDrop().save(new CompoundTag()));
		}
	}
}
