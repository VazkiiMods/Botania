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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;

public class WrappedIInventory extends WrappedInventoryBase{

	private IInventory inv;

	private WrappedIInventory(IInventory inv, ICorporeaSpark spark) {
		this.inv = inv;
		this.spark = spark;
	}

	@Override
	public IInventory getWrappedObject() {
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

	private List<ItemStack> iterateOverSlots(CorporeaRequest request, boolean doit) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();

		boolean removedAny = false;
		for (int i = inv.getSizeInventory() - 1; i >= 0; i--) {
			if(!CorporeaHelper.isValidSlot(inv, i))
				continue;

			ItemStack stackAt = inv.getStackInSlot(i);
			// WARNING: this code is very similar in all implementations of
			// IWrappedInventory - keep it synch
			if(isMatchingItemStack(request.matcher, request.checkNBT, stackAt)) {
				int rem = Math.min(stackAt.stackSize, request.count == -1 ? stackAt.stackSize : request.count);

				if(rem > 0) {
					ItemStack copy = stackAt.copy();
					if(rem < copy.stackSize)
						copy.stackSize = rem;
					stacks.add(copy);
				}

				request.foundItems += stackAt.stackSize;
				request.extractedItems += rem;

				if(doit && rem > 0) {
					inv.decrStackSize(i, rem);
					removedAny = true;
					if(spark != null)
						spark.onItemExtracted(stackAt);
				}
				if(request.count != -1)
					request.count -= rem;
			}
		}

		if(removedAny) {
			inv.markDirty();
		}

		return stacks;
	}

	public static IWrappedInventory wrap(IInventory inv, ICorporeaSpark spark) {
		return new WrappedIInventory(inv, spark);
	}

}
