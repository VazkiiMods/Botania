/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 30, 2014, 4:22:15 PM (GMT)]
 */
package vazkii.botania.api.item;

/**
 * Base Interface for the Petal Apothecary block. Can
 * be safely casted to TileEntity.
 */
public interface IPetalApothecary {

	/**
	 * Sets if the the apothecary has water or not.
	 */
	public void setWater(boolean water);

	/**
	 * Does the apothecary have water in it?
	 */
	public boolean hasWater();

}
