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
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerFlowerBag extends ScreenHandler {
	public static ContainerFlowerBag fromNetwork(int windowId, PlayerInventory inv, PacketByteBuf buf) {
		Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return new ContainerFlowerBag(windowId, inv, inv.player.getStackInHand(hand));
	}

	private final ItemStack bag;
	public final Inventory flowerBagInv;

	public ContainerFlowerBag(int windowId, PlayerInventory playerInv, ItemStack bag) {
		super(ModItems.FLOWER_BAG_CONTAINER, windowId);
		int i;
		int j;

		this.bag = bag;
		if (!playerInv.player.world.isClient) {
			flowerBagInv = ItemFlowerBag.getInventory(bag);
		} else {
			flowerBagInv = new SimpleInventory(ItemFlowerBag.SIZE);
		}

		for (i = 0; i < 2; ++i) {
			for (j = 0; j < 8; ++j) {
				int k = j + i * 8;
				addSlot(new Slot(flowerBagInv, k, 17 + j * 18, 26 + i * 18) {
					@Override
					public boolean canInsert(@Nonnull ItemStack stack) {
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
	public boolean canUse(@Nonnull PlayerEntity player) {
		ItemStack main = player.getMainHandStack();
		ItemStack off = player.getOffHandStack();
		return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
	}

	@Nonnull
	@Override
	public ItemStack transferSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if (slot != null && slot.hasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex < 16) {
				if (!insertItem(itemstack1, 16, 52, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				Block b = Block.getBlockFromItem(itemstack.getItem());
				int i = b instanceof BlockModFlower ? ((BlockModFlower) b).color.getId() : -1;
				if (i >= 0 && i < 16) {
					Slot slot1 = slots.get(i);
					if (slot1.canInsert(itemstack) && !insertItem(itemstack1, i, i + 1, true)) {
						return ItemStack.EMPTY;
					}
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
