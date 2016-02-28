package com.jaquadro.minecraft.storagedrawers.api.inventory;

/**
 * Classifies different IInventory slots according to how they should be used.
 */
public enum SlotType
{
    /** An inventory slot for input-only operations; stack sizes artificially held low. */
    INPUT,

    /** An inventory slot for output-only operations; stack sizes artificially held high. */
    OUTPUT;

    public static final SlotType[] values = values();
}
