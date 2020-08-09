package vazkii.botania.common.impl.corporea;

import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vazkii.botania.api.corporea.ICorporeaRequest;
import vazkii.botania.api.corporea.ICorporeaSpark;

import java.util.List;

public class SidedVanillaCorporeaNode extends AbstractCorporeaNode {
	private final SidedInventory inv;
	private final Direction side;

	public SidedVanillaCorporeaNode(World world, BlockPos pos, ICorporeaSpark spark, SidedInventory inv, Direction side) {
		super(world, pos, spark);
		this.inv = inv;
		this.side = side;
	}

	@Override
	public List<ItemStack> countItems(ICorporeaRequest request) {
		return examineInventory(request, false);
	}

	@Override
	public List<ItemStack> extractItems(ICorporeaRequest request) {
		return examineInventory(request, true);
	}

	protected List<ItemStack> examineInventory(ICorporeaRequest request, boolean doit) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

		int[] slots = inv.getAvailableSlots(side);
		for (int i = slots.length - 1; i >= 0; i--) {
			int slot = slots[i];
			ItemStack stack = inv.getStack(slot);
			boolean canTake = inv.canExtract(slot, stack, side);

			if (canTake && request.getMatcher().test(stack)) {
				request.trackFound(stack.getCount());

				int rem = Math.min(stack.getCount(), request.getStillNeeded() == -1 ? stack.getCount() : request.getStillNeeded());
				if (rem > 0) {
					request.trackSatisfied(rem);

					ItemStack copy = stack.copy();
					if (doit) {
						builder.addAll(breakDownBigStack(inv.removeStack(i, rem)));
						getSpark().onItemExtracted(copy);
						request.trackExtracted(rem);
					} else {
						copy.setCount(rem);
						builder.add(copy);
					}
				}
			}
		}

		return builder.build();
	}
}
