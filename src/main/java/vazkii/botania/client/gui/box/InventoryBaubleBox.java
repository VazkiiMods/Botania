/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [25/11/2015, 19:59:16 (GMT)]
 */
package vazkii.botania.client.gui.box;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ItemBaubleBox;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class InventoryBaubleBox implements IInventory {

	private static final ItemStack[] FALLBACK_INVENTORY = new ItemStack[16];

	EntityPlayer player;
	int slot;
	ItemStack[] stacks = null;

	boolean invPushed = false;
	ItemStack storedInv = null;

	public InventoryBaubleBox(EntityPlayer player, int slot) {
		this.player = player;
		this.slot = slot;
	}

	public static boolean isBaubleBox(ItemStack stack) {
		return stack != null && stack.getItem() == ModItems.baubleBox;
	}

	ItemStack getStack() {
		ItemStack stack = player.inventory.getStackInSlot(slot);
		if(stack != null)
			storedInv = stack;
		return stack;
	}

	ItemStack[] getInventory() {
		if(stacks != null)
			return stacks;

		ItemStack stack = getStack();
		if(isBaubleBox(getStack())) {
			stacks = ItemBaubleBox.loadStacks(stack);
			return stacks;
		}

		return FALLBACK_INVENTORY;
	}

	public void pushInventory() {
		if(invPushed)
			return;

		ItemStack stack = getStack();
		if(stack == null)
			stack = storedInv;

		if(stack != null) {
			ItemStack[] inv = getInventory();
			ItemBaubleBox.setStacks(stack, inv);
		}

		invPushed = true;
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getInventory()[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack[] inventorySlots = getInventory();
		if (inventorySlots[i] != null) {
			ItemStack stackAt;

			if (inventorySlots[i].stackSize <= j) {
				stackAt = inventorySlots[i];
				inventorySlots[i] = null;
				return stackAt;
			} else {
				stackAt = inventorySlots[i].splitStack(j);

				if (inventorySlots[i].stackSize == 0)
					inventorySlots[i] = null;

				return stackAt;
			}
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return getStackInSlot(i);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		ItemStack[] inventorySlots = getInventory();
		inventorySlots[slot] = itemstack;
	}

	@Override
	public int getInventoryStackLimit() {
		return isBaubleBox(getStack()) ? 64 : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return isBaubleBox(getStack());
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return isBaubleBox(getStack());
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
		// NO-OP
	}

	@Override
	public void closeInventory() {
		// NO-OP
	}

	@Override
	public String getInventoryName() {
		return LibItemNames.BAUBLE_BOX;
	}

	@Override
	public void markDirty() {
		// NO-OP
	}

}
