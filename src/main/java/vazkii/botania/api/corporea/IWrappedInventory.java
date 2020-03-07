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

import java.util.List;

/**
 * This interface wraps IInventory corporea works with in order to provide
 * compatibility with different storage mods.
 */
public interface IWrappedInventory {

	/**
	 * Break encapsulation and exposes original inventory.
	 */
	InvWithLocation getWrappedObject();

	/**
	 * Counts items in the inventory matching the request
	 *
	 * @param  request
	 *                 - specifies what should be found
	 * @return         List of ItemStack, individual stacks may exceed maxStackSize for
	 *                 purposes of counting huge amounts. To get final count requestor
	 *                 should sum stackSize of all stacks.
	 */
	List<ItemStack> countItems(CorporeaRequest request);

	/**
	 * Convenience method for accessing spark over inventory
	 */
	ICorporeaSpark getSpark();

	/**
	 * Extracts items matching request from the inventory.<br/>
	 * {@link CorporeaRequest#count} is updated to reflect how many items are
	 * yet to be extracted.<br/>
	 * {@link CorporeaRequest#foundItems} and
	 * {@link CorporeaRequest#extractedItems} are updated to reflect how many
	 * items were found and extracted.
	 *
	 * @param  request
	 * @return         List of ItemStacks to be delivered to the destination.
	 */
	List<ItemStack> extractItems(CorporeaRequest request);
}
