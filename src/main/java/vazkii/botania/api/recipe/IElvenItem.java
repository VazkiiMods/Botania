/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * Any Item that implements this is classified as an "Elven Item", by which,
 * it'll not go through the alfheim portal. Any item that comes out of it
 * must implement this or it'll just go back in.
 */
public interface IElvenItem {

	public boolean isElvenItem(ItemStack stack);

}
