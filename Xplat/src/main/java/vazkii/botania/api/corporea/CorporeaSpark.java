/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.ISparkEntity;

import java.util.List;
import java.util.Set;

/**
 * An interface for a Corporea Spark. Includes functions for handling
 * connections.
 */
public interface CorporeaSpark extends ISparkEntity {

	/**
	 * Look around this spark for neighbors and introduce them to the network by adding them
	 * to {@code network}. If they weren't already in {@code network}, this method
	 * should then recursively call this method on all newcomers.
	 *
	 * This spark should then retain the {@code network} object internally for quick
	 * access to all members of the network.
	 */
	void introduceNearbyTo(Set<CorporeaSpark> network, CorporeaSpark master);

	/**
	 * @return Corporea node this spark is attached to, generally belonging to the block below it
	 */
	CorporeaNode getSparkNode();

	/**
	 * @return All sparks in the same logical corporea network as this one, including this
	 *         spark itself.
	 */
	Set<CorporeaSpark> getConnections();

	/**
	 * Gets the list of sparks that this spark added to the list of connections during registerConnections(), this
	 * is mainly used to create a non messy chain of particles to display the network when a spark is right
	 * clicked with a wand.
	 */
	List<CorporeaSpark> getRelatives();

	/**
	 * @return The master spark of the network this spark is part of. A master spark's
	 *         master is itself. Returns {@code null} if this spark is not in a network with a valid master.
	 */
	@Nullable
	CorporeaSpark getMaster();

	/**
	 * Called when an item is extracted from the node this spark is attached to through this
	 * spark.
	 * 
	 * @param stack The itemstack, before any extraction was done. Do not mutate this.
	 */
	void onItemExtracted(ItemStack stack);

	/**
	 * Called when this spark requests items, passes in the result of the request and not the actual requested stack(s).
	 */
	void onItemsRequested(List<ItemStack> stacks);

	/**
	 * Gets if this spark is considered a master spark.
	 */
	boolean isMaster();

	/**
	 * Gets if this spark is considered a creative spark.
	 */
	boolean isCreative();

}
