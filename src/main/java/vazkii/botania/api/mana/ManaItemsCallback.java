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

import java.util.List;

/**
 * Fired when the mod is gathering all items on a player that have mana.
 * For example, if you are an accessory mod, you would listen to this event and add stacks in your
 * accessory slots to the list.
 */
public interface ManaItemsCallback {
	Event<ManaItemsCallback> EVENT = EventFactory.createArrayBacked(ManaItemsCallback.class,
			listeners -> (pl, items) -> {
				for (ManaItemsCallback listener : listeners) {
					listener.getManaItems(pl, items);
				}
			});

	void getManaItems(PlayerEntity player, List<ItemStack> items);
}
