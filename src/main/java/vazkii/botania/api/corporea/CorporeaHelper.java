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
import java.util.regex.Pattern;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public final class CorporeaHelper {

	private static final List<IInventory> empty = Collections.unmodifiableList(new ArrayList());
	private static final WeakHashMap<List<ICorporeaSpark>, List<IInventory>> cachedNetworks = new WeakHashMap();
	private static final List<ICorporeaAutoCompleteController> autoCompleteControllers = new ArrayList<ICorporeaAutoCompleteController>();

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

	public static final String[] WILDCARD_STRINGS = new String[] {
		"...", "~", "+", "?" , "*"
	};

	/**
	 * How many items were matched in the last request. If java had "out" params like C# this wouldn't be needed :V
	 */
	public static int lastRequestMatches = 0;
	/**
	 * How many items were extracted in the last request.
	 */
	public static int lastRequestExtractions = 0;

	/**
	 * Gets a list of all the inventories on this spark network. This list is cached for use once every tick,
	 * and if something changes during that tick it'll still have the first result.
	 */
	public static List<IInventory> getInventoriesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if(master == null)
			return empty;
		List<ICorporeaSpark> network = master.getConnections();

		if(cachedNetworks.containsKey(network)) {
			List<IInventory> cache = cachedNetworks.get(network);
			if(cache != null)
				return cache;
		}

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
	 * Bridge for requestItem() using an ItemStack.
	 */
	public static List<ItemStack> requestItem(ItemStack stack, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		return requestItem(stack, stack.stackSize, spark, checkNBT, doit);
	}

	/**
	 * Bridge for requestItem() using a String and an item count.
	 */
	public static List<ItemStack> requestItem(String name, int count, ICorporeaSpark spark, boolean doit) {
		return requestItem(name, count, spark, false, doit);
	}

	/**
	 * Requests list of ItemStacks of the type passed in from the network, or tries to, checking NBT or not.
	 * This will remove the items from the adequate inventories unless the "doit" parameter is false.
	 * Returns a new list of ItemStacks of the items acquired or an empty list if none was found.
	 * Case itemCount is -1 it'll find EVERY item it can.
	 * <br><br>
	 * The "matcher" parameter has to be an ItemStack or a String, if the first it'll check if the
	 * two stacks are similar using the "checkNBT" parameter, else it'll check if the name of the item
	 * equals or matches (case a regex is passed in) the matcher string.
	 */
	public static List<ItemStack> requestItem(Object matcher, int itemCount, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		List<ItemStack> stacks = new ArrayList();
		CorporeaRequestEvent event = new CorporeaRequestEvent(matcher, itemCount, spark, checkNBT, doit);
		if(MinecraftForge.EVENT_BUS.post(event))
			return stacks;

		List<IInventory> inventories = getInventoriesOnNetwork(spark);
		Map<ICorporeaInterceptor, ICorporeaSpark> interceptors = new HashMap();

		lastRequestMatches = 0;
		lastRequestExtractions = 0;

		int count = itemCount;
		for(IInventory inv : inventories) {
			boolean removedAny = false;
			ICorporeaSpark invSpark = getSparkForInventory(inv);

			if(inv instanceof ICorporeaInterceptor) {
				ICorporeaInterceptor interceptor = (ICorporeaInterceptor) inv;
				interceptor.interceptRequest(matcher, itemCount, invSpark, spark, stacks, inventories, doit);
				interceptors.put(interceptor, invSpark);
			}

			for(int i = inv.getSizeInventory() - 1; i >= 0; i--) {
				if(!isValidSlot(inv, i))
					continue;

				ItemStack stackAt = inv.getStackInSlot(i);
				if(matcher instanceof ItemStack ? stacksMatch((ItemStack) matcher, stackAt, checkNBT) : matcher instanceof String ? stacksMatch(stackAt, (String) matcher) : false) {
					int rem = Math.min(stackAt.stackSize, count == -1 ? stackAt.stackSize : count);

					if(rem > 0) {
						ItemStack copy = stackAt.copy();
						if(rem < copy.stackSize)
							copy.stackSize = rem;
						stacks.add(copy);
					}

					lastRequestMatches += stackAt.stackSize;
					lastRequestExtractions += rem;
					if(doit && rem > 0) {
						inv.decrStackSize(i, rem);
						removedAny = true;
						if(invSpark != null)
							invSpark.onItemExtracted(stackAt);
					}
					if(count != -1)
						count -= rem;
				}
			}

			if(removedAny)
				inv.markDirty();
		}

		for(ICorporeaInterceptor interceptor : interceptors.keySet())
			interceptor.interceptRequestLast(matcher, itemCount, interceptors.get(interceptor), spark, stacks, inventories, doit);

		return stacks;
	}

	/**
	 * Gets the spark attached to the inventory passed case it's a TileEntity.
	 */
	public static ICorporeaSpark getSparkForInventory(IInventory inv) {
		if(!(inv instanceof TileEntity))
			return null;

		TileEntity tile = (TileEntity) inv;
		return getSparkForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}

	/**
	 * Gets the spark attached to the block in the coords passed in. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	public static ICorporeaSpark getSparkForBlock(World world, int x, int y, int z) {
		List<ICorporeaSpark> sparks = world.getEntitiesWithinAABB(ICorporeaSpark.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));
		return sparks.isEmpty() ? null : sparks.get(0);
	}

	/**
	 * Gets if the block in the coords passed in has a spark attached. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	public static boolean doesBlockHaveSpark(World world, int x, int y, int z) {
		return getSparkForBlock(world, x, y, z) != null;
	}

	/**
	 * Gets if the slot passed in can be extracted from by a spark.
	 */
	public static boolean isValidSlot(IInventory inv, int slot) {
		return !(inv instanceof ISidedInventory) || arrayHas(((ISidedInventory) inv).getAccessibleSlotsFromSide(ForgeDirection.UP.ordinal()), slot) && ((ISidedInventory) inv).canExtractItem(slot, inv.getStackInSlot(slot), ForgeDirection.UP.ordinal());
	}

	/**
	 * Gets if two stacks match.
	 */
	public static boolean stacksMatch(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
		return stack1 != null && stack2 != null && stack1.isItemEqual(stack2) && (!checkNBT || ItemStack.areItemStackTagsEqual(stack1, stack2));
	}

	/**
	 * Gets if the name of a stack matches the string passed in.
	 */
	public static boolean stacksMatch(ItemStack stack, String s) {
		if(stack == null)
			return false;

		boolean contains = false;
		for(String wc : WILDCARD_STRINGS) {
			if(s.endsWith(wc)) {
				contains = true;
				s = s.substring(0, s.length() - wc.length());
			}
			else if(s.startsWith(wc)) {
				contains = true;
				s = s.substring(wc.length());
			}

			if(contains)
				break;
		}


		String name = stripControlCodes(stack.getDisplayName().toLowerCase().trim());
		return equalOrContain(name, s, contains) || equalOrContain(name + "s", s, contains) || equalOrContain(name + "es", s, contains) || name.endsWith("y") && equalOrContain(name.substring(0, name.length() - 1) + "ies", s, contains);
	}

	/**
	 * Clears the cached networks, called once per tick, should not be called outside
	 * of the botania code.
	 */
	public static void clearCache() {
		cachedNetworks.clear();
	}

	/**
	 * Helper method to check if an int array contains an int.
	 */
	public static boolean arrayHas(int[] arr, int val) {
		for (int element : arr)
			if(element == val)
				return true;

		return false;
	}

	/**
	 * Helper method to make stacksMatch() less messy.
	 */
	public static boolean equalOrContain(String s1, String s2, boolean contain) {
		return contain ? s1.contains(s2) : s1.equals(s2);
	}

	/**
	 * Registers a ICorporeaAutoCompleteController
	 */
	public static void registerAutoCompleteController(ICorporeaAutoCompleteController controller) {
		autoCompleteControllers.add(controller);
	}

	/**
	 * Returns if the auto complete helper should run
	 */
	public static boolean shouldAutoComplete() {
		for(ICorporeaAutoCompleteController controller : autoCompleteControllers)
			if(controller.shouldAutoComplete())
				return true;
		return false;
	}

	// Copy from StringUtils
	public static String stripControlCodes(String str) {
		return patternControlCode.matcher(str).replaceAll("");
	}
}
