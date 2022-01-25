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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class KeptItemsComponent {
	private final List<ItemStack> stacks = new ArrayList<>();

	public void addAll(Collection<ItemStack> stack) {
		stacks.addAll(stack);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	public void readFromNbt(CompoundTag tag) {
		stacks.clear();
		ListTag list = tag.getList("stacks", 10);
		for (Tag t : list) {
			stacks.add(ItemStack.of((CompoundTag) t));
		}
	}

	public void writeToNbt(CompoundTag tag) {
		ListTag list = new ListTag();
		for (ItemStack stack : stacks) {
			list.add(stack.save(new CompoundTag()));
		}
		tag.put("stacks", list);
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
