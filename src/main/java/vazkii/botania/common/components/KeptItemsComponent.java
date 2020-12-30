/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.onyxstudios.cca.api.v3.component.Component;

public class KeptItemsComponent implements Component {
	private final List<ItemStack> stacks = new ArrayList<>();

	public void addAll(Collection<ItemStack> stack) {
		stacks.addAll(stack);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		stacks.clear();
		ListTag list = tag.getList("stacks", 10);
		for (Tag t : list) {
			stacks.add(ItemStack.fromTag((CompoundTag) t));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag list = new ListTag();
		for (ItemStack stack : stacks) {
			list.add(stack.toTag(new CompoundTag()));
		}
		tag.put("stacks", list);
	}
}
