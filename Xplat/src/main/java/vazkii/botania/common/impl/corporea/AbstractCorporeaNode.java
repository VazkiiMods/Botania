/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.corporea.CorporeaNode;
import vazkii.botania.api.corporea.CorporeaSpark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCorporeaNode implements CorporeaNode {

	private final Level world;
	private final BlockPos pos;
	private final CorporeaSpark spark;

	public AbstractCorporeaNode(Level world, BlockPos pos, CorporeaSpark spark) {
		this.world = world;
		this.pos = pos;
		this.spark = spark;
	}

	@Override
	public Level getWorld() {
		return world;
	}

	@Override
	public BlockPos getPos() {
		return pos;
	}

	@Override
	public CorporeaSpark getSpark() {
		return spark;
	}

	/**
	 * Breaks down the oversized {@code stack} into multiple stacks within their stack limit.
	 * Used when performing extractions with {@link #extractItems}. Not necessary for {@link #countItems}.
	 */
	protected static Collection<ItemStack> breakDownBigStack(ItemStack stack) {
		if (stack.getCount() <= stack.getMaxStackSize()) {
			return Collections.singleton(stack);
		}

		List<ItemStack> stacks = new ArrayList<>();

		int additionalStacks = stack.getCount() / stack.getMaxStackSize();
		ItemStack fullStack = stack.copyWithCount(stack.getMaxStackSize());
		for (int i = 0; i < additionalStacks; i++) {
			stacks.add(fullStack.copy());
		}

		int lastStackSize = stack.getCount() % stack.getMaxStackSize();
		ItemStack lastStack = stack.copyWithCount(lastStackSize);
		stacks.add(lastStack);

		return stacks;
	}
}
