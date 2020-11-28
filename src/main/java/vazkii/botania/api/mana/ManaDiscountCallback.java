/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ManaDiscountCallback {
	Event<ManaDiscountCallback> EVENT = EventFactory.createArrayBacked(ManaDiscountCallback.class,
			listeners -> (pl, cur, tool) -> {
				for (ManaDiscountCallback listener : listeners) {
					cur = listener.getManaDiscount(pl, cur, tool);
				}

				return cur;
			});

	float getManaDiscount(PlayerEntity player, float curDiscount, ItemStack tool);
}
