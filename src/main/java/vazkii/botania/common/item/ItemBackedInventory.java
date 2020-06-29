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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import vazkii.botania.common.core.helper.ItemNBTHelper;

/**
 * An inventory that writes into the provided stack's NBT on save
 */
public class ItemBackedInventory extends Inventory {
	private static final String TAG_ITEMS = "Items";
	private final ItemStack stack;

	public ItemBackedInventory(ItemStack stack, int expectedSize) {
		super(expectedSize);
		this.stack = stack;

		ListNBT lst = ItemNBTHelper.getList(stack, TAG_ITEMS, 10, false);
		int i = 0;
		for (; i < expectedSize && i < lst.size(); i++) {
			setInventorySlotContents(i, ItemStack.read(lst.getCompound(i)));
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return !stack.isEmpty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		ListNBT list = new ListNBT();
		for (int i = 0; i < getSizeInventory(); i++) {
			list.add(getStackInSlot(i).write(new CompoundNBT()));
		}
		ItemNBTHelper.setList(stack, TAG_ITEMS, list);
	}
}
