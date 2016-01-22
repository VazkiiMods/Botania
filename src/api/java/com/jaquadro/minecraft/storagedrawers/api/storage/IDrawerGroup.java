package com.jaquadro.minecraft.storagedrawers.api.storage;

import com.jaquadro.minecraft.storagedrawers.api.inventory.IDrawerInventory;

public interface IDrawerGroup
{
    /**
     * Gets the number of drawers contained within this group.
     */
    int getDrawerCount ();

    /**
     * Gets the drawer at the given slot within this group.
     */
    IDrawer getDrawer (int slot);

    /**
     * Gets whether the drawer in the given slot is usable.
     */
    boolean isDrawerEnabled (int slot);

    IDrawerInventory getDrawerInventory ();

    boolean markDirtyIfNeeded ();
}
