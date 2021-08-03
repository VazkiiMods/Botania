/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.item.ItemStack;

/**
 * An Item that implements this has a mana tooltip display (like how the Mana Tablet
 * and Mana Mirror) do.
 */
public interface IManaTooltipDisplay {

	/**
	 * Returns the fraction of mana in this item for display. From 0 to 1 (exclusive).
	 */
	float getManaFractionForDisplay(ItemStack stack);

}
