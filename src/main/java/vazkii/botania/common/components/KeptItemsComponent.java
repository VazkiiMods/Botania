package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class KeptItemsComponent implements Component {
	private List<ItemStack> stacks = new ArrayList<>();

	public void add(ItemStack stack) {
		stacks.add(stack);
	}

	public List<ItemStack> take() {
		List<ItemStack> ret = stacks;
		stacks = new ArrayList<>();
		return ret;
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
