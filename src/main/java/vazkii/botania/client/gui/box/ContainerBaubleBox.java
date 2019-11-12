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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ContainerBaubleBox extends Container {
	@ObjectHolder("botania:" + LibItemNames.BAUBLE_BOX)
	public static ContainerType<ContainerBaubleBox> TYPE;

	public static ContainerBaubleBox fromNetwork(int windowId, PlayerInventory inv, PacketBuffer buf) {
		Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return new ContainerBaubleBox(windowId, inv, inv.player.getHeldItem(hand));
	}

	private final ItemStack box;
	public IItemHandlerModifiable baubles;

	public ContainerBaubleBox(int windowId, PlayerInventory playerInv, ItemStack box) {
		super(TYPE, windowId);
		int i;
		int j;

		this.box = box;
		IItemHandlerModifiable baubleBoxInv = (IItemHandlerModifiable) box.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

        /* todo 1.13 remove these slots from the gui texture and provide a button to open curios gui instead
        baubles = BaublesApi.getBaublesHandler(player);

		addSlotToContainer(new SlotBauble(player, baubles, 0, 8, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 1, 8, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 2, 8, 8 + 2 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 3, 8, 8 + 3 * 18));
		
		addSlotToContainer(new SlotBauble(player, baubles, 4, 27, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 5, 27, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 6, 27, 8 + 2 * 18));
		*/

		for(i = 0; i < 4; ++i)
			for(j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlot(new SlotItemHandler(baubleBoxInv, k, 62 + j * 18, 8 + i * 18));
			}

		for(i = 0; i < 3; ++i)
			for(j = 0; j < 9; ++j)
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player) {
		return player.getHeldItemMainhand() == box
				|| player.getHeldItemOffhand() == box;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int boxStart = 0;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;
			
			if(slotIndex < boxEnd) {
				if(!mergeItemStack(itemstack1, boxEnd, invEnd, true))
					return ItemStack.EMPTY;
			} else {
				if(!itemstack1.isEmpty() && EquipmentHandler.instance.isAccessory(itemstack1) && !mergeItemStack(itemstack1, boxStart, boxEnd, false))
					return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
