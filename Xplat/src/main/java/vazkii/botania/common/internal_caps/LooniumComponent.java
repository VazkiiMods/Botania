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

public class LooniumComponent extends SerializableComponent {
	protected static final String TAG_TODROP = "toDrop";
	protected static final String TAG_DROP_NOTHING = "dropNothing";
	private ItemStack toDrop = ItemStack.EMPTY;
	private boolean dropNothing;

	public ItemStack getDrop() {
		return toDrop;
	}

	public void setDrop(ItemStack stack) {
		this.toDrop = stack;
	}

	public boolean isDropNothing() {
		return dropNothing;
	}

	public void setDropNothing(boolean dropNothing) {
		if (dropNothing) {
			setDrop(ItemStack.EMPTY);
		}
		this.dropNothing = dropNothing;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof LooniumComponent component && ItemStack.matches(component.toDrop, toDrop)
				&& component.dropNothing == dropNothing;
	}

	@Override
	public int hashCode() {
		return toDrop.hashCode() ^ Boolean.hashCode(dropNothing);
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains(TAG_TODROP)) {
			setDrop(ItemStack.of(tag.getCompound(TAG_TODROP)));
		} else {
			setDrop(ItemStack.EMPTY);
		}
		if (tag.contains(TAG_DROP_NOTHING)) {
			setDropNothing(tag.getBoolean(TAG_DROP_NOTHING));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		if (!getDrop().isEmpty()) {
			tag.put(TAG_TODROP, getDrop().save(new CompoundTag()));
		}
		if (isDropNothing()) {
			tag.putBoolean(TAG_DROP_NOTHING, true);
		}
	}
}
