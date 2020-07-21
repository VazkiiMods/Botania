/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.util.DyeColor;

/**
 * Any TileEntity that implements this is considered a Mana Pool,
 * by which nearby functional flowers will pull mana from it.<br>
 * Mana Distributors will also accept it as valid output.<br>
 * <br>
 * <b>Implementation Instructions:</b><br>
 * - Override invalidate() and onChunkUnload(), calling <i>ManaNetworkEvent.removePool(this);</i> on both.<br>
 * - On the first tick of onUpdate(), call <i>ManaNetworkEvent.addPool(this);</i>
 */
public interface IManaPool extends IManaReceiver {

	/**
	 * Returns false if the mana pool is accepting power from other power items,
	 * true if it's sending power into them.
	 */
	public boolean isOutputtingPower();

	/**
	 * @return The color of this pool.
	 */
	public DyeColor getColor();

	/**
	 * Sets the color of this pool.
	 * 
	 * @param color The color to set.
	 */
	public void setColor(DyeColor color);

}
