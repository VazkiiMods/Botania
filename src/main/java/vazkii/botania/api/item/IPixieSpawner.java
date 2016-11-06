/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 6, 2014, 6:06:27 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;

/**
 * Any item that implements this allows for pixies to be spawned when the player takes damage when...<br>
 * - Case the item is armor, having it equipped.<br>
 * - Case the item is a bauble, having it worn.<br>
 * - On any other case, having the item being the current held item.
 */
public interface IPixieSpawner {

	/**
	 * The chance this item adds for pixies to be spawned. From 0.0 to 1.0. All values
	 * are put together when calculating.
	 */
	public float getPixieChance(ItemStack stack);

}