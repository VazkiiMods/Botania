/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [25/11/2015, 19:59:22 (GMT)]
 */
package vazkii.botania.client.gui.box;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaItem;
import baubles.api.IBauble;

public class SlotAnyBauble extends Slot {

	InventoryBaubleBox inv;

	public SlotAnyBauble(InventoryBaubleBox p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		inv = p_i1824_1_;
	}

	@Override
	public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
		inv.setInventorySlotContents(slotNumber, newStack);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof IBauble || stack.getItem() instanceof IManaItem;
	}

}
