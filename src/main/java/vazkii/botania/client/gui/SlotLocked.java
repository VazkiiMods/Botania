/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotLocked extends Slot {

	public SlotLocked(IInventory inv, int index, int xPos, int yPos) {
		super(inv, index, xPos, yPos);
	}

	@Override
	public boolean canTakeStack(PlayerEntity player) {
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

}
