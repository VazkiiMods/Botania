package com.jaquadro.minecraft.storagedrawers.api.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface IDrawerInventory extends ISidedInventory
{
    /**
     * Gets a drawer's group slot index from an IInventory slot index.
     *
     * @param inventorySlot An IInventory slot index returned from getInventorySlot.
     */
    int getDrawerSlot (int inventorySlot);

    /**
     * Gets an IInventory slot index suitable for operations for the given type.
     *
     * @param drawerSlot The index of the drawer within its group.
     * @param type The type of IInventory slot to return an index for.
     */
    int getInventorySlot (int drawerSlot, SlotType type);

    /**
     * Gets the type associated with a given IInventory slot index.
     *
     * @param inventorySlot An IInventory slot index returned from getInventorySlot.
     */
    SlotType getInventorySlotType (int inventorySlot);

    boolean canInsertItem (int slot, ItemStack stack);

    boolean canExtractItem (int slot, ItemStack stack);

    boolean syncInventoryIfNeeded ();
}
