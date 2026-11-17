/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellbindingClothItem extends Item {

	public SpellbindingClothItem(Properties builder) {
		super(builder);
	}

	public static boolean shouldDenyAnvil(ItemStack left, ItemStack right) {
		return left.getItem() instanceof SpellbindingClothItem
				&& !(right.getItem() instanceof SpellbindingClothItem);
	}
}
