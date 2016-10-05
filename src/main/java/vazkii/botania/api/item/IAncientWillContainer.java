/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2015, 11:24:54 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;

/**
 * An item that implements this can have Ancient Wills
 * crafted onto it.
 */
public interface IAncientWillContainer {

	public void addAncientWill(ItemStack stack, int will);

	public boolean hasAncientWill(ItemStack stack, int will);

}
