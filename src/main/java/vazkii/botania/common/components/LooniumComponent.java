package vazkii.botania.common.components;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class LooniumComponent implements Component {
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
		return obj instanceof LooniumComponent && ItemStack.areEqual(((LooniumComponent) obj).toDrop, toDrop);
	}

	@Override
	public int hashCode() {
		return toDrop.hashCode();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		if (tag.contains("toDrop")) {
			toDrop = ItemStack.fromTag(tag.getCompound("toDrop"));
		} else {
			toDrop = ItemStack.EMPTY;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (!toDrop.isEmpty()) {
			tag.put("toDrop", toDrop.toTag(new CompoundTag()));
		}
		return tag;
	}
}
