/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * An interface for a Corporea Request matcher. Accepts an ItemStack and returns whether it fulfills the request.
 */
public interface CorporeaRequestMatcher extends Predicate<ItemStack> {
	/**
	 * Coarse-grained test to see whether an item (not a stack) matches the criteria.
	 * <p>
	 * This is for use on the client-side ONLY. (So, you can access lang information and the like.)
	 * The server-side will use the list of item IDs this returns, and `or` it with the test on the
	 * item stacks in the system, to get items.
	 * <p>
	 * This is how we can handle languages being per client while also getting renamed items.
	 */
	default boolean testItem(Item item) {
		return false;
	}

	/**
	 * Returns whether the given stack matches the request's criteria.
	 */
	@Override
	default boolean test(ItemStack stack) {
		return false;
	}

	/**
	 * Serialize to NBT data, for the Corporea Retainer's benefit.
	 */
	default void writeToNBT(CompoundTag tag) {
	}

	/**
	 * Returns the pretty name of the requested item, for printing request feedback on Corporea Indexes.
	 */
	default Component getRequestName() {
		return Component.literal("missingno");
	}

	enum Dummy implements CorporeaRequestMatcher {
		INSTANCE
	}
}
