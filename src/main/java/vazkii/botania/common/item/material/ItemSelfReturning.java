/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 7:00:34 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

public class ItemSelfReturning extends Item {

	public ItemSelfReturning(Item.Properties builder) {
		super(builder);
	}

	@Override
	public boolean hasContainerItem() {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(this);
	}

}
