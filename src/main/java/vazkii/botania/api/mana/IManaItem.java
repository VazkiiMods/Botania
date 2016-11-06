/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 6, 2014, 9:07:40 AM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * An item that implements this can be counted as an item that can
 * contain mana.
 */
public interface IManaItem {

	/**
	 * Gets the amount of mana this item contains
	 */
	public int getMana(ItemStack stack);

	/**
	 * Gets the max amount of mana this item can hold.
	 */
	public int getMaxMana(ItemStack stack);

	/**
	 * Adds mana to this item.
	 */
	public void addMana(ItemStack stack, int mana);

	/**
	 * Can this item receive mana from a mana Pool?
	 * @param pool The pool it's receiving mana from, can be casted to IManaPool.
	 * @see IManaPool#isOutputtingPower()
	 */
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool);

	/**
	 * Can this item recieve mana from another item?
	 */
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack);

	/**
	 * Can this item export mana to a mana Pool?
	 * @param pool The pool it's exporting mana to, can be casted to IManaPool.
	 * @see IManaPool#isOutputtingPower()
	 */
	public boolean canExportManaToPool(ItemStack stack,TileEntity pool);

	/**
	 * Can this item export mana to another item?
	 */
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack);

	/**
	 * If this item simply does not export mana at all, set this to true. This is
	 * used to skip items that contain mana but can't export it when drawing the
	 * mana bar above the XP bar.
	 */
	public boolean isNoExport(ItemStack stack);

}
