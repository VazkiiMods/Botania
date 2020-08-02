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
import net.minecraftforge.items.IItemHandler;

import vazkii.botania.api.corporea.ICorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;

import java.util.ArrayList;
import java.util.List;

public class InventoryCorporeaNode extends AbstractCorporeaNode {

	protected final IItemHandler inv;

	public InventoryCorporeaNode(World world, BlockPos pos, IItemHandler inv, ICorporeaSpark spark) {
		super(world, pos, spark);
		this.inv = inv;
	}

	@Override
	public List<ItemStack> countItems(ICorporeaRequest request) {
		return iterateOverSlots(request, false);
	}

	@Override
	public List<ItemStack> extractItems(ICorporeaRequest request) {
		return iterateOverSlots(request, true);
	}

	protected List<ItemStack> iterateOverSlots(ICorporeaRequest request, boolean doit) {
		List<ItemStack> stacks = new ArrayList<>();

		for (int i = inv.getSlots() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if (request.getMatcher().isStackValid(stackAt)) {
				int rem = Math.min(stackAt.getCount(), request.getStillNeeded() == -1 ? stackAt.getCount() : request.getStillNeeded());
				request.trackFound(stackAt.getCount());

				if (rem > 0) {
					ItemStack copy = stackAt.copy();
					stacks.add(inv.extractItem(i, rem, !doit));
					if (doit && spark != null) {
						spark.onItemExtracted(copy);
					}
				}

				request.trackExtracted(rem);

				request.trackSatisfied(rem);
			}
		}

		return stacks;
	}

}
