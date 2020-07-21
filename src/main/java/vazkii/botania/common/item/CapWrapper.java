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

public class CapWrapper implements Inventory {
	private final IItemHandler handler;

	public CapWrapper(IItemHandler handler) {
		this.handler = handler;
	}

	@Override
	public int size() {
		return handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			if (!getStack(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int index) {
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack removeStack(int index, int count) {
		return handler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStack(int index) {
		return handler.extractItem(index, Integer.MAX_VALUE, false);
	}

	@Override
	public void setStack(int index, ItemStack stack) {
		if (handler instanceof IItemHandlerModifiable) {
			((IItemHandlerModifiable) handler).setStackInSlot(index, stack);
		}
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size(); i++) {
			removeStack(i);
		}
	}
}
