/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this can be crafted with Phantom Ink.
 */
public interface IPhantomInkable {

	public boolean hasPhantomInk(ItemStack stack);

	public void setPhantomInk(ItemStack stack, boolean ink);

}
