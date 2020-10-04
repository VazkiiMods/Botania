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

import vazkii.botania.api.mana.ManaItemHandler;

public class ItemAuraRing extends ItemBauble {

	private final int interval;

	public ItemAuraRing(Properties props, int interval) {
		super(props);
		this.interval = interval;
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (!player.world.isRemote && player instanceof PlayerEntity && player.ticksExisted % interval == 0) {
			ManaItemHandler.instance().dispatchManaExact(stack, (PlayerEntity) player, 1, true);
		}
	}
}
