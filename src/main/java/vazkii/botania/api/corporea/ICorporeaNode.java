/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * The unit of interaction for the Corporea network
 * All Corporea Sparks are attached to one of these
 * Note that not all implementations of this are actual inventories (e.g. interceptors)
 */
public interface ICorporeaNode {

	World getWorld();

	BlockPos getPos();

	/**
	 * Counts items in the node matching the request
	 *
	 * @param request specifies what should be found
	 * @return List of ItemStack. Individual stacks may be over-sized (exceed the item's maxStackSize) for
	 *         purposes of counting huge amounts. The list should not be modified.
	 */
	List<ItemStack> countItems(ICorporeaRequest request);

	/**
	 * Convenience method for accessing the spark over this node
	 */
	ICorporeaSpark getSpark();

	/**
	 * Extracts items matching request from the node.<br/>
	 * {@link ICorporeaRequest#getStillNeeded()} is updated to reflect how many items are
	 * yet to be extracted.<br/>
	 * {@link ICorporeaRequest#getFound()} and
	 * {@link ICorporeaRequest#getExtracted()} are updated to reflect how many
	 * items were found and extracted.
	 *
	 * @return List of ItemStacks to be delivered to the destination. The list should not be modified.
	 */
	List<ItemStack> extractItems(ICorporeaRequest request);
}
