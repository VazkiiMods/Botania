/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 11, 2014, 2:16:47 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.IElvenItem;

public class ItemElven extends ItemMod implements IElvenItem {
	public ItemElven(String name) {
		super(name);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}
}
