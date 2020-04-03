/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import net.minecraft.item.ItemStack;

import vazkii.botania.api.corporea.ICorporeaResult;

import java.util.List;

public class CorporeaResult implements ICorporeaResult {
	private final List<ItemStack> stacks;
	private final int matched;
	private final int extracted;

	public CorporeaResult(List<ItemStack> stacks, int matched, int extracted) {
		this.stacks = stacks;
		this.matched = matched;
		this.extracted = extracted;
	}

	@Override
	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public int getMatchedCount() {
		return matched;
	}

	@Override
	public int getExtractedCount() {
		return extracted;
	}
}
