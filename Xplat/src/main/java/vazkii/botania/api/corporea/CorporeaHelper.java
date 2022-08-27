/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.ServiceUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface CorporeaHelper {
	CorporeaHelper INSTANCE = ServiceUtil.findService(CorporeaHelper.class, () -> new CorporeaHelper() {});

	static CorporeaHelper instance() {
		return INSTANCE;
	}

	/**
	 * Gets all the nodes on this spark network. This is memoized for use once every tick.
	 * The order of the nodes in this set are unspecified.
	 */
	default Set<ICorporeaNode> getNodesOnNetwork(ICorporeaSpark spark) {
		return Collections.emptySet();
	}

	/**
	 * Gets a Map mapping nodes to the number of matching items.
	 * If you have it computed, prefer {@link #getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher, List)} to avoid
	 * recomputation of the list.
	 */
	default Map<ICorporeaNode, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, ICorporeaSpark spark) {
		return Collections.emptyMap();
	}

	/**
	 * Gets a Map mapping nodes to the number of matching items.
	 */
	default Map<ICorporeaNode, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, Set<ICorporeaNode> inventories) {
		return Collections.emptyMap();
	}

	/**
	 * Create a ICorporeaRequestMatcher from an ItemStack and NBT-checkness.
	 */
	default ICorporeaRequestMatcher createMatcher(ItemStack stack, boolean checkNBT) {
		return ICorporeaRequestMatcher.Dummy.INSTANCE;
	}

	/**
	 * Create a ICorporeaRequestMatcher from a String.
	 */
	default ICorporeaRequestMatcher createMatcher(String name) {
		return ICorporeaRequestMatcher.Dummy.INSTANCE;
	}

	/**
	 * Bridge for requestItem() using an ItemStack.
	 */
	default ICorporeaResult requestItem(ItemStack stack, ICorporeaSpark spark, boolean checkNBT, boolean doit) {
		return requestItem(createMatcher(stack, checkNBT), stack.getCount(), spark, doit);
	}

	/**
	 * Bridge for requestItem() using a String and an item count.
	 */
	default ICorporeaResult requestItem(String name, int count, ICorporeaSpark spark, boolean doit) {
		return requestItem(createMatcher(name), count, spark, doit);
	}

	/**
	 * Requests list of ItemStacks of the type passed in from the network, or tries to, checking NBT or not.
	 * This will remove the items from the adequate inventories unless the "doit" parameter is false.
	 * Returns a new list of ItemStacks of the items acquired or an empty list if none was found.
	 * Case itemCount is -1 it'll find EVERY item it can.
	 * <br>
	 * <br>
	 * When requesting counting of items, individual stacks may exceed maxStackSize for
	 * purposes of counting huge amounts.
	 * 
	 * @return Triple of stacks extracted, number of items matched, and number of items extracted
	 */
	default ICorporeaResult requestItem(ICorporeaRequestMatcher matcher, int itemCount, ICorporeaSpark spark, boolean doit) {
		return ICorporeaResult.Dummy.INSTANCE;
	}

	/**
	 * Gets the spark attached to the block in the coords passed in. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	@Nullable
	default ICorporeaSpark getSparkForBlock(Level world, BlockPos pos) {
		return null;
	}

	/**
	 * Gets if the block in the coords passed in has a spark attached. Note that the coords passed
	 * in are for the block that the spark will be on, not the coords of the spark itself.
	 */
	default boolean doesBlockHaveSpark(Level world, BlockPos pos) {
		return getSparkForBlock(world, pos) != null;
	}

	/**
	 * Returns the comparator strength for a corporea request that corporea crystal cubes and retainers use, following
	 * the usual "each step up requires double the items" formula.
	 */
	default int signalStrengthForRequestSize(int requestSize) {
		return 0;
	}

	default <T extends ICorporeaRequestMatcher> void registerRequestMatcher(ResourceLocation id, Class<T> clazz, Function<CompoundTag, T> deserializer) {}
}
