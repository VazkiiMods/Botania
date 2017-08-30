/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 25, 2014, 7:32:10 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;

/**
 * Any item that implements this interface is an item that would use mana
 * from the player's inventory. If there's any items in the inventory or
 * equipped in either the baubles or armor inventories that implement
 * this interface, a mana bar will be rendered.
 */
public interface IManaUsingItem {

	public boolean usesMana(ItemStack stack);

}
