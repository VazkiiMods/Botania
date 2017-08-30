/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 30, 2014, 4:28:29 PM (GMT)]
 */
package vazkii.botania.api.item;

/**
 * A TileEntity that implements this can be heated by an Exoflame flower.
 */
public interface IExoflameHeatable {

	/**
	 * Can this TileEntity smelt its contents. If true, the Exoflame is allowed
	 * to fuel it.
	 */
	public boolean canSmelt();

	/**
	 * Gets the amount of ticks left for the fuel. If below 2, the exoflame
	 * will call boostBurnTime.
	 */
	public int getBurnTime();

	/**
	 * Called to increase the amount of time this furnace should be burning
	 * the fuel for. Even if it doesn't have any fuel.
	 */
	public void boostBurnTime();

	/**
	 * Called once every two ticks to increase the speed of the furnace. Feel
	 * free to not do anything if all you want is to allow the exoflame to feed
	 * it, not make it faster.
	 */
	public void boostCookTime();

}
