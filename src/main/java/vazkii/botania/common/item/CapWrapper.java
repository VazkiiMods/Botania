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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CapWrapper implements IInventory {
	private final IItemHandler handler;

	public CapWrapper(IItemHandler handler) {
		this.handler = handler;
	}

	@Override
	public int getSizeInventory() {
		return handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return handler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return handler.extractItem(index, Integer.MAX_VALUE, false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (handler instanceof IItemHandlerModifiable) {
			((IItemHandlerModifiable) handler).setStackInSlot(index, stack);
		}
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		for (int i = 0; i < getSizeInventory(); i++) {
			removeStackFromSlot(i);
		}
	}
}
