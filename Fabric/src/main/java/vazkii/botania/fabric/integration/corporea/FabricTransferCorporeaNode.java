package vazkii.botania.fabric.integration.corporea;

import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.corporea.CorporeaRequest;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.common.impl.corporea.AbstractCorporeaNode;

import java.util.List;

public class FabricTransferCorporeaNode extends AbstractCorporeaNode {
	protected final Storage<ItemVariant> inv;

	public FabricTransferCorporeaNode(Level world, BlockPos pos, Storage<ItemVariant> inv, CorporeaSpark spark) {
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

		try (Transaction trans = Transaction.openOuter()) {
			for (var storageView : inv) {
				if (storageView.isResourceBlank()) {
					continue;
				}
				// TODO hack: truncate to INT_MAX, we probably won't be able to handle higher values anyway
				var count = (int) Math.min(Integer.MAX_VALUE, storageView.getAmount());
				var item = storageView.getResource();
				var stack = item.toStack(count);
				if (request.getMatcher().test(stack)) {
					request.trackFound(count);

					int rem = Math.min(count, request.getStillNeeded() == -1 ? count : request.getStillNeeded());
					if (rem > 0) {
						request.trackSatisfied(rem);

						if (doit) {
							builder.addAll(breakDownBigStack(item.toStack((int) inv.extract(item, rem, trans))));
							getSpark().onItemExtracted(stack);
							request.trackExtracted(rem);
						} else {
							builder.add(item.toStack((int) inv.simulateExtract(item, rem, trans)));
						}
					}
				}
			}
			if (doit && !getSpark().isCreative()) {
				// only persist changes for non-creative sparks
				trans.commit();
			}
		}

		return builder.build();
	}

}
