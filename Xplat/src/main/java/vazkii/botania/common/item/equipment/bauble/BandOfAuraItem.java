/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.ManaItemHandler;

public class BandOfAuraItem extends BaubleItem {

	private final int interval;

	public BandOfAuraItem(Properties props, int interval) {
		super(props);
		this.interval = 5 * interval;
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && entity instanceof Player player && player.tickCount % interval == 0) {
			ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
		}
	}
}
