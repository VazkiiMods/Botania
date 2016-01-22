package vazkii.botania.api.corporea;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

/**
 * Wrapper for StorageDrawers compatibility.
 *
 */
public class WrappedDeepStorage extends WrappedInvenotryBase implements IWrappedInventory {

	private IDeepStorageUnit inv;

	private WrappedDeepStorage(IDeepStorageUnit inv, ICorporeaSpark spark) {
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

		ItemStack prototype = inv.getStoredItemType();
		if (prototype == null){
			//for the case of barrel without contents set
			return stacks;
		}
		int storedCount = prototype.stackSize;
		
		//WARNING: this code is very similar in all implementations of IWrappedInventory - keep it synch
		//this code is also near duplicate to WrappedStorageDrawers - keep it synchronized
		if (isMatchingItemStack(request.matcher, request.checkNBT, prototype)) {
			int rem = Math.min(storedCount, request.count == -1 ? storedCount : request.count);

			if (rem > 0) {
				ItemStack copy = prototype.copy();
				copy.stackSize = rem;
				if (doit) {
					stacks.addAll(breakDownBigStack(copy));
				} else {
					stacks.add(copy);
				}
			}

			request.foundItems += storedCount;
			request.extractedItems += rem;

			if (doit && rem > 0) {
				decreaseStoredCount(inv, rem);

				removedAny = true;
				if (spark != null)
					spark.onItemExtracted(prototype);
			}
			if (request.count != -1)
				request.count -= rem;
		}
		if (removedAny) {
			// inv.markDirtyIfNeeded();
		}
		return stacks;
	}

	private void decreaseStoredCount(IDeepStorageUnit inventory, int rem) {
		inventory.setStoredItemCount(inventory.getStoredItemType().stackSize - rem);
	}

	/**
	 * Creates {@link WrappedDeepStorage} if specified inv can be wrapped.
	 * 
	 * @return wrapped invenotry or null if it has incompatible type.
	 */
	public static IWrappedInventory wrap(IInventory inv, ICorporeaSpark spark) {
		return inv instanceof IDeepStorageUnit ? new WrappedDeepStorage((IDeepStorageUnit) inv, spark) : null;
	}
}
