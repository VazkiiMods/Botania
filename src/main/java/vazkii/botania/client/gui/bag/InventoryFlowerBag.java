/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 16, 2015, 6:42:56 PM (GMT)]
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class InventoryFlowerBag implements IInventory {

	private static final ItemStack[] FALLBACK_INVENTORY = new ItemStack[16];

	EntityPlayer player;
	int slot;
	ItemStack[] stacks = null;

	boolean invPushed = false;
	ItemStack storedInv = null;

	public InventoryFlowerBag(EntityPlayer player, int slot) {
		this.player = player;
		this.slot = slot;
	}

	public static boolean isFlowerBag(ItemStack stack) {
		return stack != null && stack.getItem() == ModItems.flowerBag;
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
		if(isFlowerBag(getStack())) {
			stacks = ItemFlowerBag.loadStacks(stack);
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
			ItemFlowerBag.setStacks(stack, inv);
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
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ItemStack[] inventorySlots = getInventory();
		inventorySlots[i] = itemstack;
	}

	@Override
	public int getInventoryStackLimit() {
		return isFlowerBag(getStack()) ? 64 : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return isFlowerBag(getStack());
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return isFlowerBag(getStack());
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
		return LibItemNames.FLOWER_BAG;
	}

	@Override
	public void markDirty() {
		// NO-OP
	}

}
