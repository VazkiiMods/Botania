/**
 * This class was created by <Vindex>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class WrappedInventoryBase implements IWrappedInventory {

	protected final ICorporeaSpark spark;

	public WrappedInventoryBase(ICorporeaSpark spark) {
		this.spark = spark;
	}

	@Override
	public ICorporeaSpark getSpark() {
		return spark;
	}

	protected Collection<? extends ItemStack> breakDownBigStack(ItemStack stack) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		int additionalStacks = stack.getCount() / stack.getMaxStackSize();
		int lastStackSize = stack.getCount() % stack.getMaxStackSize();
		if(additionalStacks > 0) {
			ItemStack fullStack = stack.copy();
			fullStack.setCount(stack.getMaxStackSize());
			for (int i = 0; i < additionalStacks; i++) {
				stacks.add(fullStack.copy());
			}
		}
		ItemStack lastStack = stack.copy();
		lastStack.setCount(lastStackSize);
		stacks.add(lastStack);

		return stacks;
	}
}
