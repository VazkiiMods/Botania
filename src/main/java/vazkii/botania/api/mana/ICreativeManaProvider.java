/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 25, 2014, 7:34:00 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;

/**
 * Have an item implement this to flag it as an infinite
 * mana source for the purposes of the HUD rendered when
 * an IManaUserItem implementing item is present.
 */
public interface ICreativeManaProvider {

	public boolean isCreative(ItemStack stack);

}

