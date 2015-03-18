/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 16, 2015, 7:20:05 PM (GMT)]
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;

public class SlotFlower extends Slot {

	InventoryFlowerBag inv;
	int color;

	public SlotFlower(InventoryFlowerBag p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int color) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.color = color;
		inv = p_i1824_1_;
	}

	@Override
	public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
		inv.setInventorySlotContents(color, newStack);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(ModBlocks.flower) && stack.getItemDamage() == color;
	}

}
