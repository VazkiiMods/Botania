/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 19, 2015, 6:23:31 PM (GMT)]
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * A TileEntity that implements this will be able to intercept corporea
 * requests case there is a Corporea Spark on top.
 */
public interface ICorporeaInterceptor {

	/**
	 * Intercepts a request as it goes. The list of inventories has all the inventories
	 * at this point, but the list of stacks is not complete. The request parameter can
	 * be either a String or ItemStack.
	 */
	public void interceptRequest(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit);

	/**
	 * Intercepts a request after all the stacks have been found and processed. Both the
	 * list of inventories and stacks is complete at this point. The request parameter can
	 * be either a String or ItemStack.
	 */
	public void interceptRequestLast(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit);

}
