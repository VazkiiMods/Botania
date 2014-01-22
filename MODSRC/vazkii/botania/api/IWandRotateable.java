/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 5:14:05 PM (GMT)]
 */
package vazkii.botania.api;

/**
 * A TileEntity that implements this can be rotated using the wand's rotation function.
 */
public interface IWandRotateable {
	
	/**
	 * Gets the horizontal rotation of this Tile, in degrees.
	 */
	public int getHorizontalRotation();
	
	/**
	 * Gets the vertical rotation of this Tile, in degrees.
	 */
	public int getVerticalRotation();
	
	/**
	 * Called when the rotation of this TileEntity should be changed.
	 * @param horizontal The change in the rotation setting player's horizontal rotation.
	 * @param vertical The change in the rotation setting player's vertical rotation.
	 */
	public void changeRotation(float horizontal, float vertical);
	
	/**
	 * Called (on the client of the rotation setting player) per tick. Generally
	 * used to display the vector being pointed at.
	 */
	public void onClientTick();

}
