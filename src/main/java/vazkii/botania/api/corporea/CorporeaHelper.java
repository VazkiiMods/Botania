/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 14, 2015, 3:28:54 PM (GMT)]
 */
package vazkii.botania.api.corporea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class CorporeaHelper {

	private static final List<IInventory> empty = Collections.unmodifiableList(new ArrayList());
	private static final WeakHashMap<List<ICorporeaSpark>, List<IInventory>> cachedNetworks = new WeakHashMap();

	/**
	 * Gets a list of all the inventories on this spark network. This list is cached for use once every tick, 
	 * and if something changes during that tick it'll still have the first result.
	 */
	public static List<IInventory> getInventoriesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if(master == null)
			return empty;
		List<ICorporeaSpark> network = master.getConnections();	

		if(cachedNetworks.containsKey(network))
			return cachedNetworks.get(network);

		List<IInventory> inventories = new ArrayList();
		if(network != null)
			for(ICorporeaSpark otherSpark : network)
				if(otherSpark != null) {
					IInventory inv = otherSpark.getInventory();
					if(inv != null)
						inventories.add(inv);
				}

		cachedNetworks.put(network, inventories);
		return inventories;
	}

	/**
	 * Gets the amount of available items in the network of the type passed in, checking NBT or not.
	 * The higher level functions that use a List< IInventory > or a Map< IInventory, Integer > should be
	 * called instead if the context for those exists to avoid having to get the values again. 
	 */
	public static int getCountInNetwork(ItemStack stack, ICorporeaSpark spark, boolean checkNBT) {
		List<IInventory> inventories = getInventoriesOnNetwork(spark);
		return getCountInNetwork(stack, inventories, checkNBT);
	}

	/**
	 * Gets the amount of available items in the network of the type passed in, checking NBT or not.
	 * The higher level function that use a Map< IInventory, Integer > should be
	 * called instead if the context for this exists to avoid having to get the value again. 
	 */	
	public static int getCountInNetwork(ItemStack stack, List<IInventory> inventories, boolean checkNBT) {
		Map<IInventory, Integer> map = getInventoriesWithItemInNetwork(stack, inventories, checkNBT);
		return getCountInNetwork(stack, map, checkNBT);
	}

	/**
	 * Gets the amount of available items in the network of the type passed in, checking NBT or not.
	 */
	public static int getCountInNetwork(ItemStack stack, Map<IInventory, Integer> inventories, boolean checkNBT) {
		int count = 0;

		for(IInventory inv : inventories.keySet())
			count += inventories.get(inv);

		return count;
	}

	/**
	 * Gets a Map mapping IInventories to the amount of items of the type passed in that exist
	 * The higher level function that use a List< IInventory > should be
	 * called instead if the context for this exists to avoid having to get the value again. 
	 */
	public static Map<IInventory, Integer> getInventoriesWithItemInNetwork(ItemStack stack, ICorporeaSpark spark, boolean checkNBT) {
		List<IInventory> inventories = getInventoriesOnNetwork(spark);
		return getInventoriesWithItemInNetwork(stack, inventories, checkNBT);
	}

	/**
	 * Gets a Map mapping IInventories to the amount of items of the type passed in that exist
	 * The deeper level function that use a List< IInventory > should be
	 * called instead if the context for this exists to avoid having to get the value again. 
	 */
	public static Map<IInventory, Integer> getInventoriesWithItemInNetwork(ItemStack stack, List<IInventory> inventories, boolean checkNBT) {
		Map<IInventory, Integer> countMap = new HashMap();

		for(IInventory inv : inventories) {
			int count = 0;
			for(int i = 0; i < inv.getSizeInventory(); i++) {
				if(!isValidSlot(inv, i))
					continue;
				
				ItemStack stackAt = inv.getStackInSlot(i);
				if(stacksMatch(stack, stackAt, checkNBT))
					count += stackAt.stackSize;
			}

			if(count > 0)
				countMap.put(inv, count);
		}

		return countMap;
	}

	/**
	 * Requests an ItemStack of the type passed in from the network, or tries to, checking NBT or not.
	 * This will remove the items from the adequate inventories unless the "doit" parameter is false.
	 * Returns a new ItemStack of the item acquired or null if none was found.
	 */
	public static ItemStack requestItem(ItemStack stack, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		Map<IInventory, Integer> inventories = getInventoriesWithItemInNetwork(stack, spark, checkNBT);
		int count = getCountInNetwork(stack, inventories, checkNBT);
		int size = Math.min(count, stack.stackSize);
		if(size == 0)
			return null;

		ItemStack retStack = stack.copy();
		retStack.stackSize = size;
		if(!doit)
			return retStack;

		int remove = size;
		checkInvs : {
			for(IInventory inv : inventories.keySet()) {
				for(int i = 0; i < inv.getSizeInventory(); i++) {
					if(!isValidSlot(inv, i))
						continue;

					ItemStack stackAt = inv.getStackInSlot(i);
					if(stacksMatch(stack, stackAt, checkNBT)) {
						int rem = Math.min(stackAt.stackSize, remove);
						stackAt.stackSize -= rem;
						remove -= rem;

						if(stackAt.stackSize == 0)
							inv.setInventorySlotContents(i, null);
						if(remove <= 0)
							break checkInvs;
					}
				}
			}
		}

		return retStack;
	}

	/**
	 * Gets if the slot passed in can be extracted from by a spark.
	 */
	public static boolean isValidSlot(IInventory inv, int slot) {
		return true;
	}
	
	/**
	 * Gets if two stacks match.
	 */
	public static boolean stacksMatch(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
		return stack1 != null && stack2 != null && stack1.isItemEqual(stack2) && (!checkNBT || ItemStack.areItemStackTagsEqual(stack1, stack2));
	}

	/**
	 * Clears the cached networks, called once per tick, should not be called outside
	 * of the botania code.
	 */
	public static void clearCache() {
		cachedNetworks.clear();
	}
}
