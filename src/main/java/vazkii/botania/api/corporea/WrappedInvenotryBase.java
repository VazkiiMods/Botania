package vazkii.botania.api.corporea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;

public abstract class WrappedInvenotryBase implements IWrappedInventory {

	protected ICorporeaSpark spark;

	@Override
	public ICorporeaSpark getSpark() {
		return spark;
	}

	protected boolean isMatchingItemStack(Object matcher, boolean checkNBT, ItemStack stackAt) {
		return matcher instanceof ItemStack ? CorporeaHelper.stacksMatch((ItemStack) matcher, stackAt, checkNBT)
				: matcher instanceof String ? CorporeaHelper.stacksMatch(stackAt, (String) matcher) : false;
	}

	protected Collection<? extends ItemStack> breakDownBigStack(ItemStack stack) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		int additionalStacks = stack.stackSize / stack.getMaxStackSize();
		int lastStackSize = stack.stackSize % stack.getMaxStackSize();
		if (additionalStacks > 0) {
			ItemStack fullStack = stack.copy();
			fullStack.stackSize = stack.getMaxStackSize();
			for (int i = 0; i < additionalStacks; i++) {
				stacks.add(fullStack.copy());
			}
		}
		ItemStack lastStack = stack.copy();
		lastStack.stackSize = lastStackSize;
		stacks.add(lastStack);
	
		return stacks;
	}
}
