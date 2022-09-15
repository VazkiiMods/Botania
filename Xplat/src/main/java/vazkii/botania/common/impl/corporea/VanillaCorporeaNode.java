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

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.CorporeaSpark;

import java.util.List;

public class VanillaCorporeaNode extends AbstractCorporeaNode {

	protected final Container inv;

	public VanillaCorporeaNode(Level world, BlockPos pos, Container inv, CorporeaSpark spark) {
		super(world, pos, spark);
		this.inv = inv;
	}

	@Override
	public List<ItemStack> countItems(CorporeaRequest request) {
		return iterateOverSlots(request, false);
	}

	@Override
	public List<ItemStack> extractItems(CorporeaRequest request) {
		return iterateOverSlots(request, true);
	}

	protected List<ItemStack> iterateOverSlots(CorporeaRequest request, boolean doit) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

		for (int i = inv.getContainerSize() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getItem(i);
			if (request.getMatcher().test(stackAt)) {
				request.trackFound(stackAt.getCount());

				int rem = Math.min(stackAt.getCount(), request.getStillNeeded() == -1 ? stackAt.getCount() : request.getStillNeeded());
				if (rem > 0) {
					request.trackSatisfied(rem);

					if (doit) {
						ItemStack copy = stackAt.copy();
						copy.setCount(rem);
						if (getSpark().isCreative()) {
							builder.add(copy);
						} else {
							builder.addAll(breakDownBigStack(inv.removeItem(i, rem)));
						}
						getSpark().onItemExtracted(copy);
						request.trackExtracted(rem);
					} else {
						ItemStack copy = stackAt.copy();
						copy.setCount(rem);
						builder.add(copy);
					}
				}
			}
		}

		return builder.build();
	}

}
