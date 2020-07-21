/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSelfReturning extends Item {

	public ItemSelfReturning(Item.Settings builder) {
		super(builder);
	}

	@Override
	public boolean hasRecipeRemainder() {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(this);
	}

}
