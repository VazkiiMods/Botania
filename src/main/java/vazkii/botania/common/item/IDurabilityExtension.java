/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this interface can have its durability bar modified.
 */
public interface IDurabilityExtension {
	double getDurability(ItemStack stack);

	default int getDurabilityColor(ItemStack stack) {
		return 0x00ff00;
	}

	boolean showDurability(ItemStack stack);
}
