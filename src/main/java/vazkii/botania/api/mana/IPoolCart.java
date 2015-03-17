/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 6:29:07 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * Interface for an entity that counts as a Mana Pool Minecart
 */
public interface IPoolCart {

	/**
	 * Adds Mana to this cart. The mana param can be both positive or negative.
	 */
	public void addMana(int mana);
	
	/**
	 * Gets the amount of mana in the cart.
	 */
	public int getMana();
	
	/**
	 * Gets the max amount of mana the cart can store.
	 */
	public int getMaxMana();
	
}
