/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.handler;

import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AxeStrippingData {
	private static final Map<Block, Block> CUSTOM_STRIPPABLES = new HashMap<>();

	/**
	 * Provides a map of strippable blocks that need custom handling.
	 * 
	 * @return Unmodifiable mapping of strippable blocks to their respective output blocks.
	 */
	@Nullable
	public static Block getCustomStrippable(Block input) {
		return CUSTOM_STRIPPABLES.get(input);
	}

	public static Set<Block> getCustomStrippables() {
		return CUSTOM_STRIPPABLES.keySet();
	}

	/**
	 * Use for blocks that should be strippable via axe, but require a custom implementation to support stripping.
	 * 
	 * @param input  Block that can be stripped.
	 * @param output Block that results from stripping the input block.
	 */
	public static void addCustomStrippable(Block input, Block output) {
		CUSTOM_STRIPPABLES.put(input, output);
	}
}
