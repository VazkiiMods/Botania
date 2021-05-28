/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;

/**
 * An item that implements this can be counted as an item that can
 * contain mana.
 */
public interface IManaItem {

	/**
	 * Gets the amount of mana this item contains
	 */
	int getMana(ItemStack stack);

	/**
	 * Gets the max amount of mana this item can hold.
	 */
	int getMaxMana(ItemStack stack);

	/**
	 * Adds mana to this item.
	 */
	void addMana(ItemStack stack, int mana);

	/**
	 * Can this item receive mana from a mana Pool?
	 * 
	 * @param pool The pool it's receiving mana from, can be casted to IManaPool.
	 * @see IManaPool#isOutputtingPower()
	 */
	boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool);

	/**
	 * Can this item recieve mana from another item?
	 */
	boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack);

	/**
	 * Can this item export mana to a mana Pool?
	 * 
	 * @param pool The pool it's exporting mana to, can be casted to IManaPool.
	 * @see IManaPool#isOutputtingPower()
	 */
	boolean canExportManaToPool(ItemStack stack, BlockEntity pool);

	/**
	 * Can this item export mana to another item?
	 */
	boolean canExportManaToItem(ItemStack stack, ItemStack otherStack);

	/**
	 * If this item simply does not export mana at all, set this to true. This is
	 * used to skip items that contain mana but can't export it when drawing the
	 * mana bar above the XP bar.
	 */
	boolean isNoExport(ItemStack stack);

}
