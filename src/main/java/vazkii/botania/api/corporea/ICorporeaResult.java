package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;

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
