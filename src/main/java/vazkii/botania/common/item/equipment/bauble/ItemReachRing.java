/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 22, 2014, 3:00:09 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;

public class ItemReachRing extends ItemBauble {

	public ItemReachRing() {
		super(LibItemNames.REACH_RING);
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, 3.5F);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, -3.5F);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

}
