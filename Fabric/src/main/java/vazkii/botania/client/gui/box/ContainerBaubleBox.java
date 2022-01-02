/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.box;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.gui.SlotLocked;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ItemBaubleBox;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerBaubleBox extends AbstractContainerMenu {
	public static ContainerBaubleBox fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new ContainerBaubleBox(windowId, inv, inv.player.getItemInHand(hand));
	}

	private final ItemStack box;

	public ContainerBaubleBox(int windowId, Inventory playerInv, ItemStack box) {
		super(ModItems.BAUBLE_BOX_CONTAINER, windowId);
		int i;
		int j;

		this.box = box;
		Container baubleBoxInv;
		if (!playerInv.player.level.isClientSide) {
			baubleBoxInv = ItemBaubleBox.getInventory(box);
		} else {
			baubleBoxInv = new SimpleContainer(ItemBaubleBox.SIZE);
		}

		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlot(new Slot(baubleBoxInv, k, 62 + j * 18, 8 + i * 18) {
					@Override
					public boolean mayPlace(@Nonnull ItemStack stack) {
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
			if (playerInv.getItem(i) == box) {
				addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			} else {
				addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
			}
		}

	}

	@Override
	public boolean stillValid(@Nonnull Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == box || !off.isEmpty() && off == box;
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			int boxStart = 0;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;

			if (slotIndex < boxEnd) {
				if (!moveItemStackTo(itemstack1, boxEnd, invEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!itemstack1.isEmpty() && EquipmentHandler.instance.isAccessory(itemstack1) && !moveItemStackTo(itemstack1, boxStart, boxEnd, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
