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

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/**
 * An item that implements this counts as a Relic item. This is purely for interaction
 * and other mod items should not implement this interface.
 */
public interface IRelic {

	/**
	 * Binds to the player name passed in.
	 */
	@Deprecated
	public void bindToUsername(String playerName, ItemStack stack);

	/**
	 * Gets the username of the person this relic is bound to, or the empty String if the username field is empty.
	 * You should not use this to determine if a relic is bound, use UUIDs instead.
	 */
	@Deprecated
	public String getSoulbindUsername(ItemStack stack);

	/**
	 * Binds to the UUID passed in.
	 */
	public void bindToUUID(UUID uuid, ItemStack stack);

	/**
	 * Gets the UUID of the person this relic is bound to, or null if a well-formed UUID could not be found
	 */
	public UUID getSoulbindUUID(ItemStack stack);

	/**
	 * Checks if the relic contains a well-formed UUID.
	 */
	public boolean hasUUID(ItemStack stack);

	/**
	 * Sets the achievement that this relic binds to.
	 */
	public void setBindAchievement(Achievement achievement);

	/**
	 * Gets the achievement that this relic binds to.
	 */
	public Achievement getBindAchievement();

}
