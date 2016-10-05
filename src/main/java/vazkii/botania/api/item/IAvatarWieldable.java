/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 24, 2015, 6:45:50 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack);

}
