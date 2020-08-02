/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import vazkii.botania.api.corporea.ICorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;

import java.util.List;

public class ForgeCapCorporeaNode extends AbstractCorporeaNode {

	protected final IItemHandler inv;

	public ForgeCapCorporeaNode(World world, BlockPos pos, IItemHandler inv, ICorporeaSpark spark) {
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
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

		for (int i = inv.getSlots() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if (request.getMatcher().test(stackAt)) {
				request.trackFound(stackAt.getCount());

				int rem = Math.min(stackAt.getCount(), request.getStillNeeded() == -1 ? stackAt.getCount() : request.getStillNeeded());
				if (rem > 0) {
					request.trackSatisfied(rem);

					if (doit) {
						ItemStack copy = stackAt.copy();
						builder.addAll(breakDownBigStack(inv.extractItem(i, rem, false)));
						getSpark().onItemExtracted(copy);
						request.trackExtracted(rem);
					} else {
						builder.add(inv.extractItem(i, rem, true));
					}
				}
			}
		}

		return builder.build();
	}

}
