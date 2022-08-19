/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 7, 2014, 3:39:48 PM (GMT)]
 */
package vazkii.botania.api.internal;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.mana.TileSignature;

/**
 * A basic interface for a world's Mana Network.
 * @see IInternalMethodHandler#getManaNetworkInstance()
 */
public interface IManaNetwork {

	/**
	 * Clears the entire Mana Network of all it's contents, you probably
	 * don't want to call this unless you have a very good reason.
	 */
	public void clear();

	/**
	 * Gets the closest Mana Collector (eg. Mana Spreader) in the network to the Chunk
	 * Coordinates passed in, in the given dimension.<br>
	 * A way of getting the dimension is via worldObj.provider.dimensionId<br>
	 * Note that this function *can* get performance intensive, it's reccomended you
	 * call it sparingly and take cache of the value returned.
	 * @param limit The maximum distance the closest block can be, if the closest block
	 * is farther away than that, null will be returned instead.
	 */
	public TileEntity getClosestCollector(ChunkCoordinates pos, World world, int limit);

	/**
	 * Gets the closest Mana Pool in the network to the Chunk Coordinates passed in,
	 * in the given dimension.<br>
	 * A way of getting the dimension is via worldObj.provider.dimensionId<br>
	 * Note that this function *can* get performance intensive, it's reccomended you
	 * call it sparingly and take cache of the value returned.
	 * @param limit The maximum distance the closest block can be, if the closest block
	 * is farther away than that, null will be returned instead.
	 */
	public TileEntity getClosestPool(ChunkCoordinates pos, World world, int limit);

	/**
	 * Gets the list of all Mana Collectors (eg. Mana Spreader) in the dimension
	 * passed in. Note that this is the actual list and not a copy, make sure to
	 * clone the list if you intend to change it in any way.
	 */
	public List<TileSignature> getAllCollectorsInWorld(World world);

	/**
	 * Gets the list of all Mana Pools in the dimension passed in. Note that this
	 * is the actual list and not a copy, make sure to clone the list if you intend
	 * to change it in any way.
	 */
	public List<TileSignature> getAllPoolsInWorld(World world);
}
