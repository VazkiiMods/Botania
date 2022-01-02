/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.brew;

import net.minecraft.world.item.ItemStack;

/**
 * An Item that implements this is a Brew item, by which it contains
 * a brew. This is only used in vanilla to prevent the brew item
 * from going back into the brewery but other mods might use it for whatever.
 */
public interface IBrewItem {

	Brew getBrew(ItemStack brew);

}
