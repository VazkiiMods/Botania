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

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaResult;

import java.util.List;

public record CorporeaResult(List<ItemStack> stacks, int matchedCount, int extractedCount,
		Object2IntMap<ICorporeaNode> matchCountsByNode) implements ICorporeaResult {
}
