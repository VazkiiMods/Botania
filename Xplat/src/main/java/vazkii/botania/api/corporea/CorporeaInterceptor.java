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

import java.util.List;
import java.util.Set;

/**
 * A BlockEntity that implements this will be able to intercept corporea
 * requests case there is a Corporea Spark on top.
 */
public interface CorporeaInterceptor {

	/**
	 * Intercepts a request as it goes. The list of nodes has all the nodes
	 * at this point, but the list of stacks is not complete.
	 */
	void interceptRequest(CorporeaRequestMatcher request, int count, CorporeaSpark spark, CorporeaSpark source, List<ItemStack> stacks, Set<CorporeaNode> nodes, boolean doit);

	/**
	 * Intercepts a request after all the stacks have been found and processed. Both the
	 * list of nodes and stacks is complete at this point.
	 */
	void interceptRequestLast(CorporeaRequestMatcher request, int count, CorporeaSpark spark, CorporeaSpark source, List<ItemStack> stacks, Set<CorporeaNode> nodes, boolean doit);

}
