/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * An armor item that implements this can provide a mana discount for mana tools.
 * Mana tools are the ones on the main toolset (Pick, Shovel, Axe, Sword and Shovel)
 * as well as Rods.
 */
public interface IManaDiscountArmor {

	/**
	 * Gets the mana discount that this piece of armor provides for the specified
	 * tool. This is added together to create the full discount.
	 * Value is to be from 0.0 to 1.0. 0.1 is 10% discount, as an example.
	 * You can also return negative values to make tools cost more.
	 */
	default float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return 0;
	}
}
