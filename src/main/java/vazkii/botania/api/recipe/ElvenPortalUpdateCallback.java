/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.List;

/**
 * An event fired when an Elven Portal updates. The portal's
 * relevant bounding box and other stuff is passed in for convenience.
 */
public interface ElvenPortalUpdateCallback {
	Event<ElvenPortalUpdateCallback> EVENT = EventFactory.createArrayBacked(ElvenPortalUpdateCallback.class,
			listeners -> (be, bounds, open, stacks) -> {
				for (ElvenPortalUpdateCallback listener : listeners) {
					listener.onElvenPortalTick(be, bounds, open, stacks);
				}
			});

	/**
	 * @param portal May be casted to TileAlfPortal if you have botania code access aside from the API.
	 */
	void onElvenPortalTick(BlockEntity portal, Box bounds, boolean open, List<ItemStack> stacksInside);

}
