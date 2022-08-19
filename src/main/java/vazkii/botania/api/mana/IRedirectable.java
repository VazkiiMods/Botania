/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [15/11/2015, 19:28:18 (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * This is an extension of IDirectioned for TileEntities
 * that allow for their rotation to be public and modifiable outside.
 * This is used in botania for the Redirective mana lens.
 */
public interface IRedirectable extends IDirectioned {

	public void setRotationX(float rot);

	public void setRotationY(float rot);

	/**
	 * This should be called after rotation setting is done to allow
	 * for the block to re-calculate.
	 */
	public void commitRedirection();
}
