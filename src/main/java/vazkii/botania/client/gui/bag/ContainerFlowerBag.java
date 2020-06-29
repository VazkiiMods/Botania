/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerFlowerBag extends Container {
	public static ContainerFlowerBag fromNetwork(int windowId, PlayerInventory inv, PacketBuffer buf) {
		Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return new ContainerFlowerBag(windowId, inv, inv.player.getHeldItem(hand));
	}

	private final ItemStack bag;
	public final IInventory flowerBagInv;

	public ContainerFlowerBag(int windowId, PlayerInventory playerInv, ItemStack bag) {
		super(ModItems.FLOWER_BAG_CONTAINER, windowId);
		int i;
		int j;

		this.bag = bag;
		if (!playerInv.player.world.isRemote) {
			flowerBagInv = ItemFlowerBag.getInventory(bag);
		} else {
			flowerBagInv = new Inventory(ItemFlowerBag.SIZE);
		}

		for (i = 0; i < 2; ++i) {
			for (j = 0; j < 8; ++j) {
				int k = j + i * 8;
				addSlot(new Slot(flowerBagInv, k, 17 + j * 18, 26 + i * 18) {
					@Override
					public boolean isItemValid(@Nonnull ItemStack stack) {
						return ItemFlowerBag.isValid(k, stack);
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
	public boolean canInteractWith(@Nonnull PlayerEntity player) {
		ItemStack main = player.getHeldItemMainhand();
		ItemStack off = player.getHeldItemOffhand();
		return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex < 16) {
				if (!mergeItemStack(itemstack1, 16, 52, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				Block b = Block.getBlockFromItem(itemstack.getItem());
				int i = b instanceof BlockModFlower ? ((BlockModFlower) b).color.getId() : -1;
				if (i >= 0 && i < 16) {
					Slot slot1 = inventorySlots.get(i);
					if (slot1.isItemValid(itemstack) && !mergeItemStack(itemstack1, i, i + 1, true)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
