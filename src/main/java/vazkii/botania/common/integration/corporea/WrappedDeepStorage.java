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

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;

/**
 * Wrapper for StorageDrawers compatibility.
 *
 */
public class WrappedDeepStorage extends WrappedInventoryBase {

	private static boolean checkedInterface = false;
	private static boolean deepStoragePresent = false;

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
		if(prototype == null) {
			// for the case of barrel without contents set
			return stacks;
		}
		int storedCount = prototype.stackSize;

		// WARNING: this code is very similar in all implementations of
		// IWrappedInventory - keep it synch
		// this code is also near duplicate to WrappedStorageDrawers - keep it
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
				decreaseStoredCount(inv, rem);

				removedAny = true;
				if(spark != null)
					spark.onItemExtracted(prototype);
			}
			if(request.count != -1)
				request.count -= rem;
		}
		if(removedAny) {
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
	 * @return wrapped inventory or null if it has incompatible type.
	 */
	public static IWrappedInventory wrap(IInventory inv, ICorporeaSpark spark) {
		if(!isDeepStorageNeeded()) {
			return null;
		}
		return inv instanceof IDeepStorageUnit ? new WrappedDeepStorage((IDeepStorageUnit) inv, spark) : null;
	}

	/**
	 * This method checks for presence of Deep Storage API in the pack - if some
	 * other mod provides IDeepStorageUnit, support for Deep Storage will be
	 * used. This way we don't have to ship IDeepStorageUnit in Botania jar.
	 */
	private static boolean isDeepStorageNeeded() {
		if(!checkedInterface) {
			try {
				deepStoragePresent = (Class.forName("powercrystals.minefactoryreloaded.api.IDeepStorageUnit") != null);
			} catch (ClassNotFoundException e) {
				deepStoragePresent = false;
			}
			checkedInterface = true;
			FMLLog.log(Level.INFO, "[Botania] Corporea support for Deep Storage: %b", deepStoragePresent);
		}
		return deepStoragePresent;
	}
}
