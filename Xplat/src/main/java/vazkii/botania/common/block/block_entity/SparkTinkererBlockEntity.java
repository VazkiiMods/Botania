/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.item.SparkAugmentItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SparkTinkererBlockEntity extends ExposedSimpleInventoryBlockEntity {
	public SparkTinkererBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.SPARK_CHANGER, pos, state, true);
	}

	public void doSwap() {
		if (level.isClientSide()) {
			return;
		}

		ItemStack changeStack = getItemHandler().getItem(0);
		List<SparkAttachable> attachables = new ArrayList<>();
		Map<SparkAttachable, ManaSpark> attachedSparks = new LinkedHashMap<>();
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			var pos = worldPosition.relative(dir);
			var attach = XplatAbstractions.INSTANCE.findSparkAttachable(level, pos, level.getBlockState(pos), level.getBlockEntity(pos), dir.getOpposite());
			if (attach != null) {
				ManaSpark spark = SparkAttachable.getAttachedSpark(level, pos);
				if (spark != null) {
					SparkUpgradeType upg = spark.getUpgrade();
					SparkUpgradeType newUpg = changeStack.isEmpty() ? SparkUpgradeType.NONE : ((SparkAugmentItem) changeStack.getItem()).type;
					if (upg != newUpg) {
						attachables.add(attach);
						attachedSparks.put(attach, spark);
					}
				}
			}
		}

		if (!attachables.isEmpty()) {
			SparkAttachable attach = attachables.get(level.getRandom().nextInt(attachables.size()));
			ManaSpark spark = attachedSparks.get(attach);
			SparkUpgradeType upg = spark.getUpgrade();
			ItemStack sparkStack = SparkAugmentItem.getByType(upg);
			SparkUpgradeType newUpg = changeStack.isEmpty() ? SparkUpgradeType.NONE : ((SparkAugmentItem) changeStack.getItem()).type;
			spark.setUpgrade(newUpg);
			getItemHandler().setItem(0, sparkStack);
		}
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof SparkAugmentItem;
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide()) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

}
