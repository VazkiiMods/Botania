/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 16, 2015, 6:42:40 PM (GMT)]
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.gui.SlotLocked;

public class ContainerFlowerBag extends Container {

	InventoryFlowerBag flowerBagInv;

	public ContainerFlowerBag(EntityPlayer player) {
		int i;
		int j;

		int slot = player.inventory.currentItem;
		IInventory playerInv = player.inventory;
		flowerBagInv = new InventoryFlowerBag(player, slot);

		for(i = 0; i < 2; ++i)
			for(j = 0; j < 8; ++j) {
				int k = j + i * 8;
				addSlotToContainer(new SlotFlower(flowerBagInv, k, 17 + j * 18, 26 + i * 18, k));
			}

		for(i = 0; i < 3; ++i)
			for(j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(i = 0; i < 9; ++i) {
			if(player.inventory.currentItem == i)
				addSlotToContainer(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			else addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		boolean can = flowerBagInv.isUseableByPlayer(player);
		if(!can)
			onContainerClosed(player);

		return can;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		flowerBagInv.pushInventory();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(p_82846_2_);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(p_82846_2_ < 16) {
				if(!mergeItemStack(itemstack1, 16, 52, true))
					return null;
			} else {
				int i = itemstack.getItemDamage();
				if(i < 16) {
					Slot slot1 = (Slot)inventorySlots.get(i);
					if(slot1.isItemValid(itemstack) && !mergeItemStack(itemstack1, i, i + 1, true))
						return null;
				}
			}

			if(itemstack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else slot.onSlotChanged();

			if(itemstack1.stackSize == itemstack.stackSize)
				return null;

			slot.onPickupFromSlot(p_82846_1_, itemstack1);
		}

		return itemstack;
	}

}
