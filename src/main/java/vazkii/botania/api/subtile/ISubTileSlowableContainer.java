/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [29/12/2015, 23:00:06 (GMT)]
 */
package vazkii.botania.api.subtile;

/**
 * An extension of ISubTileContainer  that allows for the SubTile to be "slowed down".
 * Slowing down is the action that happens when a flower is planted in Podzol or Mycellium.
 * Any flowers that pick up items from the ground should have a delay on the time the item
 * needs to be on the floor equal to the value of getSlowDownFactor().
 */
public interface ISubTileSlowableContainer extends ISubTileContainer {

	public static final int SLOWDOWN_FACTOR_PODZOL = 5;
	public static final int SLOWDOWN_FACTOR_MYCEL = 10;

	public int getSlowdownFactor();

}
