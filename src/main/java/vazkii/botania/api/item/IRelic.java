/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 7:17:41 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * An item that implements this counts as a Relic item. This is purely for interaction
 * and other mod items should not implement this interface.
 */
public interface IRelic {

	/**
	 * Binds to the UUID passed in.
	 */
	public void bindToUUID(UUID uuid, ItemStack stack);

	/**
	 * Gets the UUID of the person this relic is bound to, or null if a well-formed UUID could not be found
	 */
	@Nullable
	public UUID getSoulbindUUID(ItemStack stack);

	/**
	 * Checks if the relic contains a well-formed UUID.
	 */
	public boolean hasUUID(ItemStack stack);

	/**
	 * Get the advancement granted when this relic binds
	 */
	@Nullable
	default ResourceLocation getAdvancement() {
		return null;
	}

}
