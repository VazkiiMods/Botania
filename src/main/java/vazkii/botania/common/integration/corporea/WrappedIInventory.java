/**
 * This class was created by <Vindex>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;

import java.util.ArrayList;
import java.util.List;

public class WrappedIInventory extends WrappedInventoryBase {

	protected final InvWithLocation inv;

	protected WrappedIInventory(InvWithLocation inv, ICorporeaSpark spark) {
		super(spark);
		this.inv = inv;
	}

	@Override
	public InvWithLocation getWrappedObject() {
		return inv;
	}

	@Override
	public List<ItemStack> countItems(CorporeaRequest request) {
		return iterateOverSlots(request, false);
	}

	@Override
	public List<ItemStack> extractItems(CorporeaRequest request) {
		return iterateOverSlots(request, true);
	}

	protected List<ItemStack> iterateOverSlots(CorporeaRequest request, boolean doit) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();

		for (int i = inv.handler.getSlots() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.handler.getStackInSlot(i);
			// WARNING: this code is very similar in all implementations of
			// IWrappedInventory - keep it synch
			if(isMatchingItemStack(request.matcher, request.checkNBT, stackAt)) {
				int rem = Math.min(stackAt.getCount(), request.count == -1 ? stackAt.getCount() : request.count);
				request.foundItems += stackAt.getCount();

				if(rem > 0) {
					stacks.add(inv.handler.extractItem(i, rem, !doit));
					if(doit && spark != null)
						spark.onItemExtracted(stackAt);
				}

				request.extractedItems += rem;

				if(request.count != -1)
					request.count -= rem;
			}
		}

		return stacks;
	}

	public static IWrappedInventory wrap(InvWithLocation inv, ICorporeaSpark spark) {
		return new WrappedIInventory(inv, spark);
	}

}
