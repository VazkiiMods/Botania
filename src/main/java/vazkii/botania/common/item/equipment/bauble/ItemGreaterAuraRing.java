/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemGreaterAuraRing extends ItemBauble implements IManaGivingItem {

	public ItemGreaterAuraRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (!player.world.isRemote && player instanceof PlayerEntity && player.ticksExisted % 2 == 0) {
			ManaItemHandler.dispatchManaExact(stack, (PlayerEntity) player, 1, true);
		}
	}
}
