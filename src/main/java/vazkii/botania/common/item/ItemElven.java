/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.recipe.IElvenItem;

public class ItemElven extends Item implements IElvenItem {
	public ItemElven(Settings props) {
		super(props);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}
}
