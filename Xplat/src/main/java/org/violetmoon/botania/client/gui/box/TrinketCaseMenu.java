/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.box;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.inventory.BotaniaMenuWithLockedSlot;
import vazkii.botania.client.gui.SlotLocked;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BaubleBoxItem;
import vazkii.botania.common.item.BotaniaItems;

public class TrinketCaseMenu extends AbstractContainerMenu implements BotaniaMenuWithLockedSlot {
	private final ItemStack trinketCase;

	public TrinketCaseMenu(int containerId, Inventory playerInv, boolean isMainHand) {
		super(BotaniaItems.TRINKET_CASE_MENU_TYPE, containerId);
		int i;
		int j;

		this.trinketCase = playerInv.player.getItemInHand(isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
		Container baubleBoxInv;
		if (!playerInv.player.level().isClientSide()) {
			baubleBoxInv = BaubleBoxItem.getInventory(trinketCase);
		} else {
			baubleBoxInv = new SimpleContainer(BaubleBoxItem.SIZE);
		}

		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlot(new Slot(baubleBoxInv, k, 62 + j * 18, 8 + i * 18) {
					@Override
					public boolean mayPlace(ItemStack stack) {
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
			if (playerInv.getItem(i) == trinketCase) {
				addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			} else {
				addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
			}
		}

	}

	@Override
	public boolean stillValid(Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == trinketCase || !off.isEmpty() && off == trinketCase;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			int boxStart = 0;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;

			if (index < boxEnd) {
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

	@Override
	public void removed(Player player) {
		if (!player.level().isClientSide()) {
			trinketCase.remove(BotaniaDataComponents.ACTIVE_TRANSIENT);
		}
		super.removed(player);
	}

	@Override
	public boolean canMoveItem(ItemStack stack) {
		return stack != trinketCase || trinketCase.isEmpty();
	}
}
