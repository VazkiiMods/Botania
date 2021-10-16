/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import vazkii.botania.api.BotaniaAPI;

/**
 * An Item that has this capability this can be wielded by an Avatar.
 */
public interface IAvatarWieldable {
	ItemApiLookup<IAvatarWieldable, Unit> API = ItemApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "avatar_wieldable"), IAvatarWieldable.class, Unit.class);

	/**
	 * Called on update of the avatar tile.
	 */
	void onAvatarUpdate(IAvatarTile tile);

	/**
	 * Gets the overlay resource to render on top of the avatar tile.
	 */
	ResourceLocation getOverlayResource(IAvatarTile tile);

}
