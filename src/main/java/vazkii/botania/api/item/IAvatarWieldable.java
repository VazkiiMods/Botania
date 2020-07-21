/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * An Item that implements this can be wielded by an Avatar.
 */
public interface IAvatarWieldable {

	/**
	 * Called on update of the avatar tile.
	 */
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack);

	/**
	 * Gets the overlay resource to render on top of the avatar tile.
	 */
	public Identifier getOverlayResource(IAvatarTile tile, ItemStack stack);

}
