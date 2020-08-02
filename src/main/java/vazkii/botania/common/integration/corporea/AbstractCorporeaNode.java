/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaSpark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCorporeaNode implements ICorporeaNode {

	private final World world;
	private final BlockPos pos;
	protected final ICorporeaSpark spark;

	public AbstractCorporeaNode(World world, BlockPos pos, ICorporeaSpark spark) {
		this.world = world;
		this.pos = pos;
		this.spark = spark;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public BlockPos getPos() {
		return pos;
	}

	@Override
	public ICorporeaSpark getSpark() {
		return spark;
	}

	protected Collection<? extends ItemStack> breakDownBigStack(ItemStack stack) {
		List<ItemStack> stacks = new ArrayList<>();
		int additionalStacks = stack.getCount() / stack.getMaxStackSize();
		int lastStackSize = stack.getCount() % stack.getMaxStackSize();
		if (additionalStacks > 0) {
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
