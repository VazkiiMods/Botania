/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.item.ItemSparkUpgrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TileSparkChanger extends TileExposedSimpleInventory {
	public TileSparkChanger() {
		super(ModTiles.SPARK_CHANGER);
	}

	public void doSwap() {
		if (world.isClient) {
			return;
		}

		ItemStack changeStack = getItemHandler().getStack(0);
		List<ISparkAttachable> attachables = new ArrayList<>();
		for (Direction dir : Direction.Type.HORIZONTAL) {
			BlockEntity tile = world.getBlockEntity(pos.offset(dir));
			if (tile instanceof ISparkAttachable) {
				ISparkAttachable attach = (ISparkAttachable) tile;
				ISparkEntity spark = attach.getAttachedSpark();
				if (spark != null) {
					SparkUpgradeType upg = spark.getUpgrade();
					SparkUpgradeType newUpg = changeStack.isEmpty() ? SparkUpgradeType.NONE : ((ItemSparkUpgrade) changeStack.getItem()).type;
					if (upg != newUpg) {
						attachables.add(attach);
					}
				}
			}
		}

		if (attachables.size() > 0) {
			ISparkAttachable attach = attachables.get(world.random.nextInt(attachables.size()));
			ISparkEntity spark = attach.getAttachedSpark();
			SparkUpgradeType upg = spark.getUpgrade();
			ItemStack sparkStack = ItemSparkUpgrade.getByType(upg);
			SparkUpgradeType newUpg = changeStack.isEmpty() ? SparkUpgradeType.NONE : ((ItemSparkUpgrade) changeStack.getItem()).type;
			spark.setUpgrade(newUpg);
			Collection<ISparkEntity> transfers = spark.getTransfers();
			if (transfers != null) {
				transfers.clear();
			}
			getItemHandler().setStack(0, sparkStack);
		}
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(1) {
			@Override
			public int getMaxCountPerStack() {
				return 1;
			}

			@Override
			public boolean isValid(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof ItemSparkUpgrade;
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

}
