/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 6:26:40 PM (GMT)]
 */
package vazkii.botania.api.brew;

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this counts as a Brew Container, by which
 * it can be used a center item for brew recipes and can contain
 * a brew.
 */
public interface IBrewContainer {

	/**
	 * Returs an ItemStack that should be an item that has the brew
	 * passed in.
	 */
	public ItemStack getItemForBrew(Brew brew, ItemStack stack);

	/**
	 * Gets the cost to add this brew onto this container. Return -1
	 * to not allow for the brew to be added. Normally you'd
	 * use brew.getManaCost(stack);
	 */
	public int getManaCost(Brew brew, ItemStack stack);

}
