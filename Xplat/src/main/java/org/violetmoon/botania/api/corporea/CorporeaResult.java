/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Represents a completed {@link CorporeaRequest}
 */
public interface CorporeaResult {
	/**
	 * @return The itemstacks found. Not a fresh copy each time called. When doing a count,
	 *         and not extracting, the stacks in this list may be oversized.
	 */
	default List<ItemStack> stacks() {
		return Collections.emptyList();
	}

	/**
	 * @return The number of items matching the request's matcher.
	 */
	default int matchedCount() {
		return 0;
	}

	/**
	 * @return The number of items extracted.
	 */
	default int extractedCount() {
		return 0;
	}

	/**
	 * @return A map of corporea nodes that contributed to this result, and how many items each contributed.
	 *         Should not be modified.
	 */
	default Object2IntMap<CorporeaNode> matchCountsByNode() {
		return Object2IntMaps.emptyMap();
	}

	enum Dummy implements CorporeaResult {
		INSTANCE
	}
}
