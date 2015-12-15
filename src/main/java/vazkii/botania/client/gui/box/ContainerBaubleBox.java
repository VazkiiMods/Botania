/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [25/11/2015, 19:59:06 (GMT)]
 */
package vazkii.botania.client.gui.box;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.gui.SlotLocked;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.container.SlotBauble;
import baubles.common.lib.PlayerHandler;

public class ContainerBaubleBox extends Container {

	InventoryBaubleBox baubleBoxInv;
	InventoryBaubles baubles;

	public ContainerBaubleBox(EntityPlayer player) {
		int i;
		int j;

		int slot = player.inventory.currentItem;
		IInventory playerInv = player.inventory;
		baubleBoxInv = new InventoryBaubleBox(player, slot);

		baubles = new InventoryBaubles(player);
		baubles.setEventHandler(this);
		if(!player.worldObj.isRemote)
			baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;

		addSlotToContainer(new SlotBauble(baubles, BaubleType.AMULET, 0, 8, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(baubles, BaubleType.RING,   1, 8, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(baubles, BaubleType.RING,   2, 8, 8 + 2 * 18));
		addSlotToContainer(new SlotBauble(baubles, BaubleType.BELT,   3, 8, 8 + 3 * 18));

		for(i = 0; i < 4; ++i)
			for(j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlotToContainer(new SlotAnyBauble(baubleBoxInv, k, 62 + j * 18, 8 + i * 18));
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
		boolean can = baubleBoxInv.isUseableByPlayer(player);
		if(!can)
			onContainerClosed(player);

		return can;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		baubleBoxInv.pushInventory();

		if(!player.worldObj.isRemote)
			PlayerHandler.setPlayerBaubles(player, baubles);
	}

	@Override
	public void putStacksInSlots(ItemStack[] p_75131_1_) {
		baubles.blockEvents = true;
		super.putStacksInSlots(p_75131_1_);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(p_82846_2_);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			System.out.println(p_82846_2_ + " " + itemstack);
			if(p_82846_2_ < 28) {
				if(!mergeItemStack(itemstack1, 28, 64, true))
					return null;
			} else {
				if(itemstack1 != null && (itemstack1.getItem() instanceof IBauble || itemstack1.getItem() instanceof IManaItem) && !mergeItemStack(itemstack1, 4, 28, false))
					return null;
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
