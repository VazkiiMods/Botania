/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.block.IAvatarTile;

/**
 * An Item that has this capability this can be wielded by an Avatar.
 */
public interface IAvatarWieldable {

	/**
	 * Called on update of the avatar tile.
	 */
	void onAvatarUpdate(IAvatarTile tile);

	/**
	 * Gets the overlay resource to render on top of the avatar tile.
	 */
	ResourceLocation getOverlayResource(IAvatarTile tile);

}
