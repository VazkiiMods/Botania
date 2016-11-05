/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 4:55:00 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * Any TileEntity that implements this can receive mana from mana bursts.
 */
public interface IManaReceiver extends IManaBlock {

	/**
	 * Is this Mana Receiver is full? Being full means no mana bursts will be sent.
	 */
	public boolean isFull();

	/**
	 * Called when this receiver receives mana.
	 */
	public void recieveMana(int mana);

	/**
	 * Can this tile receive mana from bursts? Generally set to false for
	 * implementations of IManaCollector.
	 */
	public boolean canRecieveManaFromBursts();

}
