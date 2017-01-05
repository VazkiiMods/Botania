/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:37 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class ItemThorRing extends ItemRelicBauble {

	public ItemThorRing() {
		super(LibItemNames.THOR_RING);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

	public static ItemStack getThorRing(EntityPlayer player) {
		IInventory baubles = BaublesApi.getBaubles(player);
		ItemStack stack1 = baubles.getStackInSlot(1);
		ItemStack stack2 = baubles.getStackInSlot(2);
		return isThorRing(stack1) ? stack1 : isThorRing(stack2) ? stack2 : null;
	}

	private static boolean isThorRing(ItemStack stack) {
		return !stack.isEmpty() && (stack.getItem() == ModItems.thorRing || stack.getItem() == ModItems.aesirRing);
	}

}
