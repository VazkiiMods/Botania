/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.api.corporea.CorporeaRequestor;
import vazkii.botania.api.corporea.CorporeaSpark;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.FilterHelper;
import vazkii.botania.common.helper.InventoryHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class CorporeaFunnelBlockEntity extends BaseCorporeaBlockEntity implements CorporeaRequestor {
	private static final int[] ROTATION_TO_STACK_SIZE = { 1, 2, 4, 8, 16, 32, 48, 64 };

	public CorporeaFunnelBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.CORPOREA_FUNNEL, pos, state);
	}

	public void doRequest() {
		CorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null) {
			WeightedRandomList<FilterHelper.WeightedItemStack> filter = FilterHelper.getWeightedFiltersOnBlock(level, getBlockPos(),
					(itemFrame, stack) -> stack.copyWithCount(ROTATION_TO_STACK_SIZE[itemFrame.getRotation()]),
					(itemFrame, stack) -> stack.getCount());
			if (!filter.isEmpty()) {
				ItemStack stack = filter.getRandom(level.getRandom())
						.map(FilterHelper.WeightedItemStack::stack)
						.orElse(ItemStack.EMPTY);

				if (!stack.isEmpty()) {
					var matcher = CorporeaHelper.instance().createMatcher(stack, true);
					doCorporeaRequest(matcher, stack.getCount(), spark, null);
				}
			}
		}
	}

	@Override
	public void doCorporeaRequest(CorporeaRequestMatcher request, int count, CorporeaSpark spark, @Nullable LivingEntity entity) {
		BlockPos invPos = getInvPos();

		List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, entity, true).stacks();
		spark.onItemsRequested(stacks);
		for (ItemStack reqStack : stacks) {
			if (invPos != null
					&& XplatAbstractions.INSTANCE.insertToInventory(level, invPos, Direction.UP, reqStack, true).isEmpty()) {
				InventoryHelper.checkEmpty(
						XplatAbstractions.INSTANCE.insertToInventory(level, invPos, Direction.UP, reqStack, false)
				);
			} else {
				ItemEntity item = new ItemEntity(level, spark.entity().getX(), spark.entity().getY(), spark.entity().getZ(), reqStack);
				level.addFreshEntity(item);
			}
		}
	}

	@Nullable
	private BlockPos getInvPos() {
		BlockPos downOne = worldPosition.below();
		if (XplatAbstractions.INSTANCE.hasInventory(level, downOne, Direction.UP)) {
			return downOne;
		}

		BlockPos downTwo = worldPosition.below(2);
		if (XplatAbstractions.INSTANCE.hasInventory(level, downTwo, Direction.UP)) {
			return downTwo;
		}

		return null;
	}

}
