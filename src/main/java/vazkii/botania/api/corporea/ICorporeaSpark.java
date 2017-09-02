/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 13, 2015, 10:53:05 PM (GMT)]
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * An interface for a Corporea Spark. Includes functions for handling
 * connections.
 */
public interface ICorporeaSpark {

	/**
	 * Called to register the connections for the spark network passed in. Parameters include the master spark,
	 * which is the one that initiated the chain call, the referrer which passed the call to this instance
	 * and the List of sparks connected. Normal behavior should be to find any sparks around that are not
	 * already present in the list of connections and add them to it, passing the function call to them.
	 * <br><br>
	 * The connections List and the master Spark should be kept in this instance as pointers for use in
	 * getConnections() and getMaster() and passed in to any subsequent registerConnections calls on sparks
	 * found nearby. This is only called whenever a new spark is added or removed from and to the network,
	 * at that point the connection list in the master spark would be cleared out, also clearing out the one
	 * in this instance, as it should be a pointer.
	 */
	public void registerConnections(ICorporeaSpark master, ICorporeaSpark referrer, List<ICorporeaSpark> connections);

	/**
	 * Gets the inventory this spark is bound to, generally the one right below it.
	 */
	public InvWithLocation getSparkInventory();

	/**
	 * Gets the list of sparks this spark is connected to, see registerConnections(). This list
	 * should also include itself. This list must be checked against on a regular basis to verify
	 * that the spark is still in the network, if that's not the case, the pointer should be
	 * eliminated.
	 */
	public List<ICorporeaSpark> getConnections();

	/**
	 * Gets the list of sparks that this spark added to the list of connections during registerConnections(), this
	 * is mainly used to create a non messy chain of particles to display the network when a spark is right
	 * clicked with a wand.
	 */
	public List<ICorporeaSpark> getRelatives();

	/**
	 * Gets the master spark in this network, see registerConnections(). The value this returns
	 * should be null and the pointer should be eliminated if the spark is no longer present
	 * in the network.
	 */
	public ICorporeaSpark getMaster();

	/**
	 * Called when an item is extracted from the inventory this spark is attached to through this
	 * spark.
	 */
	public void onItemExtracted(ItemStack stack);

	/**
	 * Called when this spark requests items, passes in the result of the request and not the actual requested stack(s).
	 */
	public void onItemsRequested(List<ItemStack> stacks);

	/**
	 * Gets if this spark is considered a master spark.
	 */
	public boolean isMaster();

	/**
	 * Gets the network that this spark is on, or the color it's displaying. Sparks may only connect to others
	 * of the same network, and on changing network should trigger a re-cache of the previous network.
	 */
	public EnumDyeColor getNetwork();

}
