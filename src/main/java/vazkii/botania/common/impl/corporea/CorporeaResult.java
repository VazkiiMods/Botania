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
