/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:42:56 PM (GMT)]
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class InventoryFlowerBag implements IItemHandlerModifiable {

	private final IItemHandlerModifiable bagInv;
	final ItemStack bag;

	public InventoryFlowerBag(ItemStack bag) {
		this.bag = bag;
		bagInv = (IItemHandlerModifiable) bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		bagInv.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return bagInv.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return bagInv.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return bagInv.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return bagInv.extractItem(slot, amount, simulate);
	}
}
