/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.corporea.CorporeaNode;
import vazkii.botania.api.corporea.CorporeaResult;

import java.util.List;

public record CorporeaResultImpl(List<ItemStack> stacks, int matchedCount, int extractedCount,
		Object2IntMap<CorporeaNode> matchCountsByNode) implements CorporeaResult {
}
