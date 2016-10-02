/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 24, 2014, 4:43:47 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemAuraRing extends ItemBauble implements IManaGivingItem {

	public ItemAuraRing(String name) {
		super(name);
	}

	public ItemAuraRing() {
		this(LibItemNames.AURA_RING);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		if(player instanceof EntityPlayer && player.ticksExisted % getDelay() == 0)
			ManaItemHandler.dispatchManaExact(stack, (EntityPlayer) player, 1, true);
	}

	int getDelay() {
		return 10;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

}
