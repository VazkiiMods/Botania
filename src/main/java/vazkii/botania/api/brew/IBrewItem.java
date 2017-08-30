/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 9:20:33 PM (GMT)]
 */
package vazkii.botania.api.brew;

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this is a Brew item, by which it contains
 * a brew. This is only used in vanilla to prevent the brew item
 * from going back into the brewery but other mods might use it for whatever.
 */
public interface IBrewItem {

	public Brew getBrew(ItemStack brew);

}
