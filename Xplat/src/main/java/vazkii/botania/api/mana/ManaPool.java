/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.item.DyeColor;

import vazkii.botania.api.BotaniaAPI;

/**
 * Any {@link ManaReceiver} that also implements this is considered a Mana Pool,
 * by which nearby functional flowers will pull mana from it.<br>
 * Mana Distributors will also accept it as valid output.<br>
 * <br>
 * <b>Implementation Instructions:</b><br>
 * - When joining the world (e.g. on first tick), call
 * {@link vazkii.botania.api.internal.ManaNetwork#fireManaNetworkEvent}
 * with this object, type {@link ManaBlockType#POOL}, and action {@link ManaNetworkAction#ADD}.
 * - When leaving the world (e.g. in setRemoved), call
 * {@link vazkii.botania.api.internal.ManaNetwork#fireManaNetworkEvent}
 * with this object, type {@link ManaBlockType#POOL}, and action {@link ManaNetworkAction#REMOVE}.
 *
 * Get the mana network using {@link BotaniaAPI#getManaNetworkInstance()}.
 */
public interface ManaPool extends ManaReceiver {

	/**
	 * Returns false if the mana pool is accepting power from other power items,
	 * true if it's sending power into them.
	 */
	boolean isOutputtingPower();

	/**
	 * @return The color of this pool.
	 */
	DyeColor getColor();

	/**
	 * Sets the color of this pool.
	 * 
	 * @param color The color to set.
	 */
	void setColor(DyeColor color);

}
