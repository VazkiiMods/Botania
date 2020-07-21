/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.box;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ItemBaubleBox;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerBaubleBox extends ScreenHandler {
	public static ContainerBaubleBox fromNetwork(int windowId, PlayerInventory inv, PacketByteBuf buf) {
		Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return new ContainerBaubleBox(windowId, inv, inv.player.getStackInHand(hand));
	}

	private final ItemStack box;

	public ContainerBaubleBox(int windowId, PlayerInventory playerInv, ItemStack box) {
		super(ModItems.BAUBLE_BOX_CONTAINER, windowId);
		int i;
		int j;

		this.box = box;
		Inventory baubleBoxInv;
		if (!playerInv.player.world.isClient) {
			baubleBoxInv = ItemBaubleBox.getInventory(box);
		} else {
			baubleBoxInv = new SimpleInventory(ItemBaubleBox.SIZE);
		}

		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlot(new Slot(baubleBoxInv, k, 62 + j * 18, 8 + i * 18) {
					@Override
					public boolean canInsert(@Nonnull ItemStack stack) {
						return EquipmentHandler.instance.isAccessory(stack);
					}
				});
			}
		}

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canUse(@Nonnull PlayerEntity player) {
		ItemStack main = player.getMainHandStack();
		ItemStack off = player.getOffHandStack();
		return !main.isEmpty() && main == box || !off.isEmpty() && off == box;
	}

	@Nonnull
	@Override
	public ItemStack transferSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if (slot != null && slot.hasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int boxStart = 0;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;

			if (slotIndex < boxEnd) {
				if (!insertItem(itemstack1, boxEnd, invEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!itemstack1.isEmpty() && EquipmentHandler.instance.isAccessory(itemstack1) && !insertItem(itemstack1, boxStart, boxEnd, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemstack1);
		}

		return itemstack;
	}

}
