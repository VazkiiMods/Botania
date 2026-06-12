/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.gui.enderhand;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.gui.SlotLocked;
import vazkii.botania.common.item.BotaniaItems;

// [VanillaCopy] ChestMenu (3 rows), except the slot containing the Hand of Ender is locked
public class HandOfEnderMenu extends AbstractContainerMenu {
	private static final int SLOTS_PER_ROW = 9;
	private static final int NUM_ROWS = 3;
	private final Container container;
	private final ItemStack enderHand;

	public HandOfEnderMenu(int containerId, Inventory playerInventory, boolean isMainHand) {
		this(containerId, playerInventory, new SimpleContainer(SLOTS_PER_ROW * NUM_ROWS), isMainHand);
	}

	public HandOfEnderMenu(int containerId, Inventory playerInventory, Container container, boolean isMainHand) {
		super(BotaniaItems.HAND_OF_ENDER_MENU_TYPE, containerId);
		checkContainerSize(container, NUM_ROWS * SLOTS_PER_ROW);
		this.container = container;
		this.enderHand = playerInventory.player.getItemInHand(isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);

		container.startOpen(playerInventory.player);

		// ender chest inventory
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < SLOTS_PER_ROW; x++) {
				this.addSlot(new Slot(container, x + y * 9, 8 + x * 18, 18 + y * 18));
			}
		}

		// player inventory
		int inventoryTop = (NUM_ROWS - 4) * 18;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 103 + y * 18 + inventoryTop));
			}
		}

		// hot bar
		for (int y = 0; y < 9; y++) {
			if (playerInventory.getItem(y) == enderHand) {
				this.addSlot(new SlotLocked(playerInventory, y, 8 + y * 18, 161 + inventoryTop));
			} else {
				this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 161 + inventoryTop));
			}
		}
	}

	// Botania: valid as long as the player is still holding the Hand of Ender
	@Override
	public boolean stillValid(Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == enderHand || !off.isEmpty() && off == enderHand;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack clicked = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);
		if (slot.hasItem()) {
			ItemStack stack = slot.getItem();
			clicked = stack.copy();
			if (slotIndex < NUM_ROWS * SLOTS_PER_ROW) {
				if (!this.moveItemStackTo(stack, NUM_ROWS * SLOTS_PER_ROW, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stack, 0, NUM_ROWS * SLOTS_PER_ROW, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return clicked;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}
}
