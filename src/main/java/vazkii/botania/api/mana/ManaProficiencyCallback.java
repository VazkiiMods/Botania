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

public interface ManaProficiencyCallback {
	Event<ManaProficiencyCallback> EVENT = EventFactory.createArrayBacked(ManaProficiencyCallback.class,
			listeners -> (pl, st, cur) -> {
				for (ManaProficiencyCallback listener : listeners) {
					cur = listener.getProficient(pl, st, cur);
				}

				return cur;
			});

	boolean getProficient(PlayerEntity player, ItemStack tool, boolean curProficient);
}
