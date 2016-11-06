/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [15/11/2015, 19:27:07 (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * Any TileEntity that implements this is defined as having a direction,
 * and has two rotations (X and Y).
 * An example of this is the Mana Spreader.
 */
public interface IDirectioned {

	/**
	 * @return The X rotation, in degrees
	 */
	public float getRotationX();

	/**
	 * @return The Y rotation, in degrees
	 */
	public float getRotationY();

	/**
	 * Set the X rotation
	 * @param rot X rotation, in degrees
	 */
	public void setRotationX(float rot);

	/**
	 * Set the Y rotation
	 * @param rot Y rotation, in degrees
	 */
	public void setRotationY(float rot);

	/**
	 * This should be called after rotation setting is done to allow
	 * for the block to re-calculate.
	 */
	public void commitRedirection();

}
