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

import java.util.Objects;

public class LooniumComponent extends SerializableComponent {
	protected static final String TAG_TODROP = "toDrop";
	protected static final String TAG_DROP_NOTHING = "dropNothing";
	protected static final String TAG_SLOW_DESPAWN = "slowDespawn";
	private ItemStack toDrop = ItemStack.EMPTY;
	private boolean dropNothing;
	private boolean slowDespawn;

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

	public boolean isSlowDespawn() {
		return slowDespawn;
	}

	public void setSlowDespawn(boolean slowDespawn) {
		this.slowDespawn = slowDespawn;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof LooniumComponent component && ItemStack.matches(component.toDrop, toDrop)
				&& component.dropNothing == dropNothing && component.slowDespawn == slowDespawn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(toDrop.hashCode(), dropNothing, slowDespawn);
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
		if (tag.contains(TAG_SLOW_DESPAWN)) {
			setSlowDespawn(tag.getBoolean(TAG_SLOW_DESPAWN));
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
		if (isSlowDespawn()) {
			tag.putBoolean(TAG_SLOW_DESPAWN, true);
		}
	}
}
