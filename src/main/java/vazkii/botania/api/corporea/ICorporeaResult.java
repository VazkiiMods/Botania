/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Represents a completed {@link ICorporeaRequest}
 */
public interface ICorporeaResult {
	/**
	 * @return The itemstacks found. Not a fresh copy each time called.
	 */
	default List<ItemStack> getStacks() {
		return Collections.emptyList();
	}

	/**
	 * @return The number of items matching the request's matcher.
	 */
	default int getMatchedCount() {
		return 0;
	}

	/**
	 * @return The number of items extracted.
	 */
	default int getExtractedCount() {
		return 0;
	}

	enum Dummy implements ICorporeaResult {
		INSTANCE
	}
}
