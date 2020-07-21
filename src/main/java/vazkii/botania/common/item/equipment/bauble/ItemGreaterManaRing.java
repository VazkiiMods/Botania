/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.item.ItemStack;

public class ItemGreaterManaRing extends ItemManaRing {

	private static final int MAX_MANA = ItemManaRing.MAX_MANA * 4;

	public ItemGreaterManaRing(Settings props) {
		super(props);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA;
	}

}
