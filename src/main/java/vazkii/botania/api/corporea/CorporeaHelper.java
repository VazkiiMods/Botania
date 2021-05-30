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

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BlockPistonRelay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

public final class CorporeaHelper {

	private static final List<InvWithLocation> empty = ImmutableList.of();
	private static final WeakHashMap<List<ICorporeaSpark>, List<InvWithLocation>> cachedNetworks = new WeakHashMap<>();
	private static final Map<BlockPistonRelay.DimWithPos, ICorporeaSpark> cachedSparks = new HashMap<>();
	private static final List<ICorporeaAutoCompleteController> autoCompleteControllers = new ArrayList<>();

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

	public static final String[] WILDCARD_STRINGS = { "...", "~", "+", "?" , "*" };

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
	public static List<InvWithLocation> getInventoriesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if(master == null)
			return empty;
		List<ICorporeaSpark> network = master.getConnections();

		if(cachedNetworks.containsKey(network)) {
			List<InvWithLocation> cache = cachedNetworks.get(network);
			if(cache != null)
				return cache;
		}

		List<InvWithLocation> inventories = new ArrayList<>();
		if(network != null)
			for(ICorporeaSpark otherSpark : network)
				if(otherSpark != null) {
					InvWithLocation inv = otherSpark.getSparkInventory();
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
		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);
		return getCountInNetwork(stack, inventories, checkNBT);
	}

	/**
	 * Gets the amount of available items in the network of the type passed in, checking NBT or not.
	 * The higher level function that use a Map< IInventory, Integer > should be
	 * called instead if the context for this exists to avoid having to get the value again.
	 */
	public static int getCountInNetwork(ItemStack stack, List<InvWithLocation> inventories, boolean checkNBT) {
		Map<InvWithLocation, Integer> map = getInventoriesWithItemInNetwork(stack, inventories, checkNBT);
		return getCountInNetwork(stack, map, checkNBT);
	}

	/**
	 * Gets the amount of available items in the network of the type passed in, checking NBT or not.
	 */
	public static int getCountInNetwork(ItemStack stack, Map<InvWithLocation, Integer> inventories, boolean checkNBT) {
		int count = 0;

		for(InvWithLocation inv : inventories.keySet())
			count += inventories.get(inv);

		return count;
	}

	/**
	 * Gets a Map mapping IInventories to the amount of items of the type passed in that exist
	 * The higher level function that use a List< IInventory > should be
	 * called instead if the context for this exists to avoid having to get the value again.
	 */
	public static Map<InvWithLocation, Integer> getInventoriesWithItemInNetwork(ItemStack stack, ICorporeaSpark spark, boolean checkNBT) {
		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);
		return getInventoriesWithItemInNetwork(stack, inventories, checkNBT);
	}

	/**
	 * Gets a Map mapping IInventories to the amount of items of the type passed in that exist
	 * The deeper level function that use a List< IInventory > should be
	 * called instead if the context for this exists to avoid having to get the value again.
	 */
	public static Map<InvWithLocation, Integer> getInventoriesWithItemInNetwork(ItemStack stack, List<InvWithLocation> inventories, boolean checkNBT) {
		Map<InvWithLocation, Integer> countMap = new HashMap<>();
		List<IWrappedInventory> wrappedInventories = BotaniaAPI.internalHandler.wrapInventory(inventories);
		for (IWrappedInventory inv : wrappedInventories) {
			CorporeaRequest request = new CorporeaRequest(stack, checkNBT, -1);
			inv.countItems(request);
			if (request.foundItems > 0) {
				countMap.put(inv.getWrappedObject(), request.foundItems);
			}
		}

		return countMap;
	}

	/**
	 * Bridge for requestItem() using an ItemStack.
	 */
	public static List<ItemStack> requestItem(ItemStack stack, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		return requestItem(stack, stack.getCount(), spark, checkNBT, doit);
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
	 * <br><br>
	 * When requesting counting of items, individual stacks may exceed maxStackSize for
	 * purposes of counting huge amounts.
	 */
	public static List<ItemStack> requestItem(Object matcher, int itemCount, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		List<ItemStack> stacks = new ArrayList<>();
		CorporeaRequestEvent event = new CorporeaRequestEvent(matcher, itemCount, spark, checkNBT, doit);
		if(MinecraftForge.EVENT_BUS.post(event))
			return stacks;

		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);

		List<IWrappedInventory> inventoriesW = BotaniaAPI.internalHandler.wrapInventory(inventories);
		Map<ICorporeaInterceptor, ICorporeaSpark> interceptors = new HashMap<ICorporeaInterceptor, ICorporeaSpark>();

		CorporeaRequest request = new CorporeaRequest(matcher, checkNBT, itemCount);
		for(IWrappedInventory inv : inventoriesW) {
			ICorporeaSpark invSpark = inv.getSpark();

			InvWithLocation originalInventory = inv.getWrappedObject();
			if(originalInventory.world.getTileEntity(originalInventory.pos) instanceof ICorporeaInterceptor) {
				ICorporeaInterceptor interceptor = (ICorporeaInterceptor) originalInventory.world.getTileEntity(originalInventory.pos);
				interceptor.interceptRequest(matcher, itemCount, invSpark, spark, stacks, inventories, doit);
				interceptors.put(interceptor, invSpark);
			}

			if(doit) {
				stacks.addAll(inv.extractItems(request));
			} else {
				stacks.addAll(inv.countItems(request));
			}
		}

		for(ICorporeaInterceptor interceptor : interceptors.keySet())
			interceptor.interceptRequestLast(matcher, itemCount, interceptors.get(interceptor), spark, stacks, inventories, doit);

		lastRequestMatches = request.foundItems;
		lastRequestExtractions = request.extractedItems;

		return stacks;
	}

	/**
	 * Gets the spark attached to the inventory passed case it's a TileEntity.
	 */
	public static ICorporeaSpark getSparkForInventory(InvWithLocation inv) {
		return getSparkForBlock(inv.world, inv.pos);
	}

	/**
	 * Gets the spark attached to the block in the coords passed in. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	public static ICorporeaSpark getSparkForBlock(World world, BlockPos pos) {
		if (world.isRemote) {
			// This should never be called on client, but safe than sorry
			return getSparkForBlockFromWorld(world, pos);
		}
		int dimension = world.provider.getDimension();
		BlockPistonRelay.DimWithPos key = new BlockPistonRelay.DimWithPos(dimension, pos);
		ICorporeaSpark spark = cachedSparks.get(key);
		
		if (spark != null && ((Entity) spark).getEntityWorld() == world && ((Entity) spark).isEntityAlive()) {
			return spark;
		}
		spark = getSparkForBlockFromWorld(world, pos);
		cachedSparks.put(key, spark);
		return spark;
	}

	private static ICorporeaSpark getSparkForBlockFromWorld(World world, BlockPos pos) {
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.add(1, 2, 1)),
				Predicates.instanceOf(ICorporeaSpark.class));
		if (sparks.isEmpty()) {
			return null;
		}
		return (ICorporeaSpark) sparks.get(0);
	}

	/**
	 * Gets if the block in the coords passed in has a spark attached. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	public static boolean doesBlockHaveSpark(World world, BlockPos pos) {
		return getSparkForBlock(world, pos) != null;
	}

	/**
	 * Gets if two stacks match.
	 */
	public static boolean stacksMatch(ItemStack stack1, ItemStack stack2, boolean checkNBT) {
		return !stack1.isEmpty() && !stack2.isEmpty() && stack1.isItemEqual(stack2) && (!checkNBT || ItemStack.areItemStackTagsEqual(stack1, stack2));
	}

	/**
	 * Gets if the name of a stack matches the string passed in.
	 */
	public static boolean stacksMatch(ItemStack stack, String s) {
		if(stack.isEmpty())
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
		cachedSparks.clear();
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
	
	/** 
	 * Returns the comparator strength for a corporea request that corporea crystal cubes and retainers use, following the usual "each step up requires double the items" formula.
	 */
	public static int signalStrengthForRequestSize(int requestSize) {
		if(requestSize <= 0) return 0;
		else if (requestSize >= 16384) return 15;
		else return Math.min(15, MathHelper.log2(requestSize) + 1);
	}
}
