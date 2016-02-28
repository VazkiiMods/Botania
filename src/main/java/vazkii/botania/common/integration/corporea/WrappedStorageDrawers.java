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

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;

/**
 * Wrapper for StorageDrawers compatibility.
 *
 */
public class WrappedStorageDrawers extends WrappedInventoryBase {

	private IDrawerGroup inv;

	private WrappedStorageDrawers(IDrawerGroup inv, ICorporeaSpark spark) {
		this.inv = inv;
		this.spark = spark;
	}

	@Override
	public IInventory getWrappedObject() {
		return (IInventory) inv;
	}

	@Override
	public List<ItemStack> countItems(CorporeaRequest request) {
		return iterateOverStacks(request, false);
	}

	@Override
	public List<ItemStack> extractItems(CorporeaRequest request) {
		return iterateOverStacks(request, true);
	}

	private List<ItemStack> iterateOverStacks(CorporeaRequest request, boolean doit) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		boolean removedAny = false;

		for(int i = 0; i < inv.getDrawerCount(); i++) {
			IDrawer drawer = inv.getDrawer(i);
			if(drawer == null) {
				continue;
			}
			ItemStack prototype = drawer.getStoredItemPrototype();
			int storedCount = drawer.getStoredItemCount();

			// WARNING: this code is very similar in all implementations of
			// IWrappedInventory - keep it synch
			// also this code is near duplicate to WrappedDeepStorage - keep it
			// synchronized
			if(isMatchingItemStack(request.matcher, request.checkNBT, prototype)) {
				int rem = Math.min(storedCount, request.count == -1 ? storedCount : request.count);

				if(rem > 0) {
					ItemStack copy = prototype.copy();
					copy.stackSize = rem;
					if(doit) {
						stacks.addAll(breakDownBigStack(copy));
					} else {
						stacks.add(copy);
					}
				}

				request.foundItems += storedCount;
				request.extractedItems += rem;

				if(doit && rem > 0) {
					decreaseStoredCount(drawer, rem);

					removedAny = true;
					if(spark != null)
						spark.onItemExtracted(prototype);
				}
				if(request.count != -1)
					request.count -= rem;
			}
		}
		if(removedAny) {
			inv.markDirtyIfNeeded();
		}
		return stacks;
	}

	private void decreaseStoredCount(IDrawer drawer, int rem) {
		drawer.setStoredItemCount(drawer.getStoredItemCount() - rem);
	}

	/**
	 * Creates {@link WrappedStorageDrawers} if specified inv can be wrapped.
	 * 
	 * @return wrapped inventory or null if it has incompatible type.
	 */
	public static IWrappedInventory wrap(IInventory inv, ICorporeaSpark spark) {
		return inv instanceof IDrawerGroup ? new WrappedStorageDrawers((IDrawerGroup) inv, spark) : null;
	}
}
