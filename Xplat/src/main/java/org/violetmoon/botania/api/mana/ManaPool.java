/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

/**
 * Any {@link ManaReceiver} that also implements this is considered a Mana Pool,
 * by which nearby functional flowers will pull mana from it.
 * Mana Distributors will also accept it as valid output.
 */
public interface ManaPool extends ManaReceiver {

	/**
	 * Returns false if the mana pool is accepting power from other power items,
	 * true if it's sending power into them.
	 */
	boolean isOutputtingPower();

	/**
	 * @return Maximum amount of storable mana
	 */
	int getMaxMana();

}
