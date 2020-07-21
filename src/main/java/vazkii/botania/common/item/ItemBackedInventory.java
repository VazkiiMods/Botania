/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import vazkii.botania.common.core.helper.ItemNBTHelper;

/**
 * An inventory that writes into the provided stack's NBT on save
 */
public class ItemBackedInventory extends SimpleInventory {
	private static final String TAG_ITEMS = "Items";
	private final ItemStack stack;

	public ItemBackedInventory(ItemStack stack, int expectedSize) {
		super(expectedSize);
		this.stack = stack;

		ListTag lst = ItemNBTHelper.getList(stack, TAG_ITEMS, 10, false);
		int i = 0;
		for (; i < expectedSize && i < lst.size(); i++) {
			setStack(i, ItemStack.fromTag(lst.getCompound(i)));
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return !stack.isEmpty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		ListTag list = new ListTag();
		for (int i = 0; i < size(); i++) {
			list.add(getStack(i).toTag(new CompoundTag()));
		}
		ItemNBTHelper.setList(stack, TAG_ITEMS, list);
	}
}
