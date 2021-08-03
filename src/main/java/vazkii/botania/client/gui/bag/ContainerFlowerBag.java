/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerFlowerBag extends AbstractContainerMenu {
	public static ContainerFlowerBag fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new ContainerFlowerBag(windowId, inv, inv.player.getItemInHand(hand));
	}

	private final ItemStack bag;
	public final Container flowerBagInv;

	public ContainerFlowerBag(int windowId, Inventory playerInv, ItemStack bag) {
		super(ModItems.FLOWER_BAG_CONTAINER, windowId);
		int i;
		int j;

		this.bag = bag;
		if (!playerInv.player.level.isClientSide) {
			flowerBagInv = ItemFlowerBag.getInventory(bag);
		} else {
			flowerBagInv = new SimpleContainer(ItemFlowerBag.SIZE);
		}

		for (i = 0; i < 2; ++i) {
			for (j = 0; j < 8; ++j) {
				int k = j + i * 8;
				addSlot(new Slot(flowerBagInv, k, 17 + j * 18, 26 + i * 18) {
					@Override
					public boolean mayPlace(@Nonnull ItemStack stack) {
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
	public boolean stillValid(@Nonnull Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotIndex < 16) {
				if (!moveItemStackTo(itemstack1, 16, 52, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				Block b = Block.byItem(itemstack.getItem());
				int i = b instanceof BlockModFlower ? ((BlockModFlower) b).color.getId() : -1;
				if (i >= 0 && i < 16) {
					Slot slot1 = slots.get(i);
					if (slot1.mayPlace(itemstack) && !moveItemStackTo(itemstack1, i, i + 1, true)) {
						return ItemStack.EMPTY;
					}
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
